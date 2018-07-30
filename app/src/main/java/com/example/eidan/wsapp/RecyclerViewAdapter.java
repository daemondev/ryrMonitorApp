package com.example.eidan.wsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eidan on 29/07/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    List<Agent> mData;

    public RecyclerViewAdapter(Context mContext, List<Agent> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_agent, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_phone.setText(mData.get(position).getPhone());
        holder.img_status.setImageResource(mData.get(position).getStatus());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Agent_Activity.class);
                intent.putExtra("callerID", mData.get(position).getCallerID());
                intent.putExtra("phone", mData.get(position).getPhone());
                intent.putExtra("status", mData.get(position).getStatus());
                intent.putExtra("callType", mData.get(position).getCallType());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_phone;
        ImageView img_status;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_phone = (TextView) itemView.findViewById(R.id.agent_id);
            img_status = (ImageView) itemView.findViewById(R.id.agent_img_status);
            cardView = (CardView) itemView.findViewById(R.id.cardview_agent_id);
        }
    }
}
