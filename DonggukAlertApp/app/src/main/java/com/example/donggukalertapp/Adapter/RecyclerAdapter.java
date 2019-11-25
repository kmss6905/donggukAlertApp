package com.example.donggukalertapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donggukalertapp.DataModel.dataModel;
import com.example.donggukalertapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {
    private ArrayList<dataModel> dataModelArrayList;
    private Context mContext;



    //아이템 클릭시 실행 함수
    private RecyclerAdapter.ItemClick itemClick;


    public interface ItemClick {
        public void onClick(View view, int position, int id);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(RecyclerAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public RecyclerAdapter(ArrayList<dataModel> dataModelArrayList, Context mContext) {
        this.dataModelArrayList = dataModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_item_view, parent, false);
        return new viewHolder(view);
    }


    // 받아온 정보 뷰홀더에 바인드
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.title.setText(dataModelArrayList.get(position).getTitle());
        holder.date.setText(dataModelArrayList.get(position).getTime());


        if(dataModelArrayList.get(position).isNew()){
            holder.newImg.setVisibility(View.VISIBLE);
        }else{
            holder.newImg.setVisibility(View.GONE);
        }

        if(dataModelArrayList.get(position).isHavingFile()){
            holder.newFileImg.setVisibility(View.VISIBLE);
        }else{
            holder.newFileImg.setVisibility(View.INVISIBLE);
        }


        // 아이템 클릭
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, position, 1);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        ImageView newImg;
        ImageView newFileImg;
        LinearLayout itemLayout;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            date = itemView.findViewById(R.id.date);
            newImg = itemView.findViewById(R.id.newImg);
            newFileImg = itemView.findViewById(R.id.newFile);
        }
    }
}
