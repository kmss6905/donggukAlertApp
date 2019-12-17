package com.dongguk.donggukalertapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dongguk.donggukalertapp.Adapter.RecyclerAdapter;
import com.dongguk.donggukalertapp.ContentAcitivty;
import com.dongguk.donggukalertapp.DataModel.NoticeUrl;
import com.dongguk.donggukalertapp.DataModel.dataModel;
import com.dongguk.donggukalertapp.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class EventNoticeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "EventNoticeFragment";

    private RecyclerAdapter recyclerAdapter;
    private ArrayList<dataModel> dataModelArrayList;
    private LinearLayoutManager linearLayoutManager; // 레이아웃 메니저
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 세팅
        dataModelArrayList = new ArrayList<>();
        new Descrption(page).execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);



        // 리사이클러뷰 세팅
        recyclerAdapter = new RecyclerAdapter(dataModelArrayList, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        //리사이클러뷰 뷰 세팅
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 새로고침
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisible >= totalItemCount - 1) {
                    page ++;
                    new Descrption(page).execute();
                    Log.d(TAG, "lastVisibled  , page " + page);
                }
            }
        });


        recyclerAdapter.setItemClick(new RecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position, int id) {
                switch (id){
                    case 1:
                        Intent intent = new Intent(getActivity(), ContentAcitivty.class);
                        intent.putExtra("title", dataModelArrayList.get(position).getTitle());
                        intent.putExtra("url", dataModelArrayList.get(position).getUrl());
                        intent.putExtra("type", "행사공지");
                        startActivity(intent);
                        break;
                }
            }
        });
    }



    public class Descrption extends AsyncTask<Void, Void, Void> {
        int page;
        private ProgressDialog progressDialog;

        public Descrption(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            //진행다일로그 시작 ( 커스텀해보자 )
            progressDialog = new ProgressDialog(getActivity());
            Log.d(TAG, "onPreExecute:  발동 ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                if(page == 1){
                    Connection.Response execute = Jsoup.connect(NoticeUrl.getUrl("event")).execute();
                    Document document = Jsoup.parse(execute.body());
//                    Elements elements = document.select("td");
                    Elements elements = document.getElementsByTag("tr");

                    SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
                    int i = 0;
                    for(Element element : elements){
                        i ++;
                        dataModel dataModel = new dataModel();
                        Log.d(TAG, "doInBackground: =================================================");
                        Elements elements1 = element.getElementsByTag("td");
                        Log.d(TAG, "doInBackground: " + i + elements1.toString());
                        for(Element element1 : elements1){
                            if(element1.is("td.title")){
                                Log.d(TAG, "doInBackground: " +i + "제목 : "  + element1.select("td.title").text());

                                dataModel.setTitle(element1.select("td.title").text());
                                dataModel.setUrl(element1.select("td.title a").attr("href"));
                                if(element1.select("td.title img").first() != null){
                                    dataModel.setNew(true);
                                }
                            }



                            if(element1.is("td")){
                                dateFormat.setLenient(true);
                                try{
                                    if(dataModel.getTime() == null){
                                        String date = dateFormat.parse(element1.select("td").text()).toString();
                                        Date date1 = dateFormat.parse(element1.select("td").text());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd"); // new format
                                        dataModel.setTime(simpleDateFormat.format(date1));
                                    }else{
                                        Date date1 = dateFormat.parse(element1.select("td").text());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                                        dataModel.setEndTime(simpleDateFormat.format(date1));
                                    }

                                }catch (Exception e){

                                }

                            }

                            if(element1.select("img").first() != null){
                                if(element1.select("td img").first().attr("src").equals("/Web-home/manager/images/mbsPreview/icon_notice.gif")){
                                    Log.d(TAG, "doInBackground: "+i + "이미지(notice) : " + element1.select("td img").first().attr("src"));
                                    dataModel.setNotice(true);
                                }else if(element1.select("td img").first().attr("src").equals("/mbs/kr/images/board/ico_file.gif")){
                                    Log.d(TAG, "doInBackground: "+i + "이미지(파일) : " + element1.select("td img").first().attr("src"));
                                    dataModel.setHavingFile(true);
                                }
                            }
                        }


                        Log.d(TAG, "doInBackground: " + i + "번째 : " + dataModel.toString());
                        dataModelArrayList.add(dataModel);
                    }

                }else{
                    Connection.Response execute = Jsoup.connect(NoticeUrl.getUrl("event") + "&spage=" + page).execute();
                    Document document = Jsoup.parse(execute.body());
//                    Elements elements = document.select("td");
                    Elements elements = document.getElementsByTag("tr");

                    SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
                    int i = 0;
                    for(Element element : elements){
                        i ++;
                        dataModel dataModel = new dataModel();
                        Log.d(TAG, "doInBackground: =================================================");
                        Elements elements1 = element.getElementsByTag("td");
                        Log.d(TAG, "doInBackground: " + i + elements1.toString());
                        for(Element element1 : elements1){
                            if(element1.is("td.title")){
                                Log.d(TAG, "doInBackground: " +i + "제목 : "  + element1.select("td.title").text());

                                dataModel.setTitle(element1.select("td.title").text());
                                dataModel.setUrl(element1.select("td.title a").attr("href"));
                                if(element1.select("td.title img").first() != null){
                                    dataModel.setNew(true);
                                }
                            }



                            if(element1.is("td")){
                                dateFormat.setLenient(true);
                                try{
                                    if(dataModel.getTime() == null){
                                        String date = dateFormat.parse(element1.select("td").text()).toString();
                                        Date date1 = dateFormat.parse(element1.select("td").text());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd"); // new format
                                        dataModel.setTime(simpleDateFormat.format(date1));
                                    }else{
                                        Date date1 = dateFormat.parse(element1.select("td").text());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                                        dataModel.setEndTime(simpleDateFormat.format(date1));
                                    }

                                }catch (Exception e){

                                }

                            }

                            if(element1.select("img").first() != null){
                                if(element1.select("td img").first().attr("src").equals("/Web-home/manager/images/mbsPreview/icon_notice.gif")){
                                    Log.d(TAG, "doInBackground: "+i + "이미지(notice) : " + element1.select("td img").first().attr("src"));
                                    dataModel.setNotice(true);
                                }else if(element1.select("td img").first().attr("src").equals("/mbs/kr/images/board/ico_file.gif")){
                                    Log.d(TAG, "doInBackground: "+i + "이미지(파일) : " + element1.select("td img").first().attr("src"));
                                    dataModel.setHavingFile(true);
                                }
                            }
                        }


                        Log.d(TAG, "doInBackground: " + i + "번째 : " + dataModel.toString());
                        dataModelArrayList.add(dataModel);
                    }

                }


            } catch (IOException ex) {
                ex.printStackTrace();
                Log.d(TAG, "doInBackground: " + ex.getMessage());
                Log.d(TAG, "doInBackground: " + ex.getCause());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // 리스트 순환 중 notice 관련 공지 또는 비어 있는 타이틀의 공지는 순환하면 서 객체 삭제
            Iterator<dataModel> iter =dataModelArrayList.iterator();
            while (iter.hasNext()) {
                dataModel dataModel = iter.next();
                if (dataModel.isNotice() || dataModel.getTitle() == null) {
                    iter.remove();
                }
            }

            recyclerAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        dataModelArrayList.clear();
        recyclerAdapter.notifyDataSetChanged();
        new Descrption(page).execute();

        swipeRefreshLayout.setRefreshing(false);
    }

}
