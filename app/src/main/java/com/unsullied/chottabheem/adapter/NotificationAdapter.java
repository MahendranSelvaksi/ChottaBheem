package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.RedeemModel;
import com.unsullied.chottabheem.utils.holderClass.AdminRequestHolder;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter {

    List<RedeemModel> mList;
    private Context mContext;

    public NotificationAdapter(Context mContext, List<RedeemModel> childList) {
        this.mContext = mContext;
        this.mList = childList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.admin_request_adapter, parent, false);
        return new AdminRequestHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdminRequestHolder) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,0,0,0);
            ((AdminRequestHolder) holder).requestNameTV.setGravity(Gravity.LEFT);
            ((AdminRequestHolder) holder).requestNameTV.setLayoutParams(params);

            ((AdminRequestHolder) holder).requestNameTV.setText(mList.get(position).getMessage().trim());
            ((AdminRequestHolder) holder).requestStatusTV.setVisibility(View.GONE);
            ((AdminRequestHolder) holder).requestStatusKeyTV.setVisibility(View.GONE);
            ((AdminRequestHolder) holder).requestTimeTV.setText(mList.get(position).getRequestTime().trim());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
