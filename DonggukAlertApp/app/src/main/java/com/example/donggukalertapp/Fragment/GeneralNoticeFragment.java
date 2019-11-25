package com.example.donggukalertapp.Fragment;

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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donggukalertapp.Adapter.RecyclerAdapter;
import com.example.donggukalertapp.ContentAcitivty;
import com.example.donggukalertapp.DataModel.NoticeUrl;
import com.example.donggukalertapp.DataModel.dataModel;
import com.example.donggukalertapp.R;

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

public class GeneralNoticeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "GeneralNoticeFragment";

    private RecyclerAdapter recyclerAdapter;
    private ArrayList<dataModel> dataModelArrayList;
    private LinearLayoutManager linearLayoutManager; // 레이아웃 메니저
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static int page = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataModelArrayList = new ArrayList<>(); // 데이터 리스트
        new Descrption(page).execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_fragment, container, false);

        // 리사이클러뷰 세팅
        recyclerAdapter = new RecyclerAdapter(dataModelArrayList, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        //리사 뷰
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 새로고침 뷰
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);


        Log.d(TAG, "onCreateView: 생명주기");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 생명주기");


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
                        intent.putExtra("type", "일반공지");
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    // 웹 요청
    public class Descrption extends AsyncTask<Void, Void, Void> {
        int page;

        public Descrption(int page) {
            this.page = page;
        }

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //진행다일로그 시작 ( 커스텀해보자 )
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                if(page == 1){
                    Connection.Response execute = Jsoup.connect(NoticeUrl.getUrl("nomal")).execute();
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
                }else{ // 페이지 2 이상
                    Connection.Response execute = Jsoup.connect(NoticeUrl.getUrl("nomal") + "&spage=" + page).execute();
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
            //ArraList를 인자로 해서 어답터와 연결한다.
//            MyAdapter myAdapter = new MyAdapter(list);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(myAdapter);


            // 리스트 순환 중 notice 관련 공지 또는 비어 있는 타이틀의 공지는 순환하면 서 객체 삭제
            Iterator<dataModel> iter =dataModelArrayList.iterator();
            while (iter.hasNext()) {
                dataModel dataModel = iter.next();
                if (dataModel.isNotice() || dataModel.getTitle() == null) {
                    iter.remove();
                }
            }



            for(dataModel dataModel : dataModelArrayList){
                Log.d(TAG, "onPostExecute: " + dataModel.toString());
            }

            recyclerAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }



    }


    // 새로고침
    @Override
    public void onRefresh() {
        /**
         *  페이지 초기화
         *  리스트 지움
         *  리스트 갱신
         *  다시 받아옴
         */

        page = 1; // 페이지 초기화
        dataModelArrayList.clear(); // 리스트 지움
        recyclerAdapter.notifyDataSetChanged(); // 어텝터 갱신
        new Descrption(page).execute(); // 다시 받아옴




        swipeRefreshLayout.setRefreshing(false);
    }
}
