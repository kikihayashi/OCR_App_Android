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

    //存之後需要用到的View
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView tool_CV;
        private ImageView tool_IV;
        private TextView tool_TV;

        public MyViewHolder(View v)//定義tool_dialog.xml中RecyclerView上的view(tool_item.xml)
        {
            super(v);
            tool_CV = v.findViewById(R.id.toolCardView);
            tool_IV = v.findViewById(R.id.toolImageView);
            tool_TV = v.findViewById(R.id.toolTextView);
            v.setOnClickListener(this);
        }

        public void setData(ToolLayoutModel toolModel)//將toolModel的資料設置到tool_item.xml裡的CardView、ImageView、TextView上
        {
            tool_CV.setBackgroundColor(Color.parseColor(toolModel.color));
            tool_IV.setImageResource(toolModel.drawable);
            tool_TV.setText(toolModel.text);
        }

        @Override
        public void onClick(View v)//Dialog中每個item被點擊時，要執行的動作寫在此處
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
    //設置ViewHolder裡的View
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position)
    {
        holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
    }

    @Override
    //RecyclerView總共有幾筆(組)資料
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