package com.wood.ocr_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private ArrayList<ToolLayoutModel> toolModelList;
    private MyAdapter.RecyclerViewItemClickListener recyclerViewItemClickListener;

    public MyAdapter(ArrayList<ToolLayoutModel> myDataSet, MyAdapter.RecyclerViewItemClickListener listener)
    {
        toolModelList = myDataSet;
        recyclerViewItemClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tool_TV;
        private ImageView tool_IV;
        private CardView tool_CV;

        public MyViewHolder(View v)//在MyViewHolder內定義每個list cell上會有的view components(定義一列有幾項)
        {
            super(v);

            tool_TV = v.findViewById(R.id.toolTextView);
            tool_IV = v.findViewById(R.id.toolImageView);
            tool_CV = v.findViewById(R.id.toolCardView);

            v.setOnClickListener(this);
        }

        public void setData(ToolLayoutModel tool_Model)
        {
            tool_TV.setText(tool_Model.text);
            tool_IV.setImageResource(tool_Model.drawable);
            tool_CV.setBackgroundColor(Color.parseColor(tool_Model.color));
        }

        @Override
        public void onClick(View v)//Dialog中每個item被點擊時，執行動作寫在此處
        {
            recyclerViewItemClickListener.clickOnItem(this.getAdapterPosition());
        }
    }

    @NonNull
    @Override
    //產生介面
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.tool_item, parent, false);

        MyViewHolder tool_item_holder = new MyViewHolder(v);

        return tool_item_holder;
    }

    @Override
    //處理資料的順序
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position)
    {
        holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
    }

    @Override
    //看總共有幾筆資料
    public int getItemCount()
    {
        return toolModelList.size();
    }

    //RecyclerViewItemClickListener(抽象)介面，需在UserActivity中實作clickOnItem抽象方法
    public interface RecyclerViewItemClickListener
    {
        void clickOnItem(int dataIndex);//抽象方法，dataIndex為選單(tools)對應的位置
    }
}