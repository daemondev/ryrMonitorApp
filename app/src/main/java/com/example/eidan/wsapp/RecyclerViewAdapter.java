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
    List<Agent> lstAgent;

    public RecyclerViewAdapter(Context mContext, List<Agent> lstAgent) {
        this.mContext = mContext;
        this.lstAgent = lstAgent;
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
        holder.tv_agent_calling_number.setText(lstAgent.get(position).getExten());
        holder.tv_calltype.setText(lstAgent.get(position).getCalltype());
        holder.tv_callerid.setText(lstAgent.get(position).getCallerid()+"");
        holder.img_status.setImageResource(lstAgent.get(position).getState());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Agent_Activity.class);
                intent.putExtra("callerid", lstAgent.get(position).getCallerid());
                intent.putExtra("exten", lstAgent.get(position).getExten());
                intent.putExtra("state", lstAgent.get(position).getState());
                intent.putExtra("calltype", lstAgent.get(position).getCalltype());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAgent.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_agent_calling_number, tv_callerid, tv_calltype;
        ImageView img_status;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_agent_calling_number = (TextView) itemView.findViewById(R.id.agent_calling_number);
            tv_calltype = (TextView) itemView.findViewById(R.id.agent_calltype);
            tv_callerid = (TextView) itemView.findViewById(R.id.agent_id);
            img_status = (ImageView) itemView.findViewById(R.id.agent_img_status);
            cardView = (CardView) itemView.findViewById(R.id.cardview_agent_id);
        }
    }
}
