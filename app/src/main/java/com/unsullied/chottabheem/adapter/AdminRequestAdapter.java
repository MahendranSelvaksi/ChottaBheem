package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.RedeemModel;

import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RedeemModel> mList;

    public AdminRequestAdapter(Context mContext, List<RedeemModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
            ((AdminRequestHolder) holder).requestNameTV.setText(mList.get(position).getRequestName().trim());
            ((AdminRequestHolder) holder).requestStatusTV.setText(mList.get(position).getRequestStatus().trim());
            ((AdminRequestHolder) holder).requestTimeTV.setText(mList.get(position).getRequestTime().trim());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class AdminRequestHolder extends RecyclerView.ViewHolder {
        private CustomTextView requestNameTV, requestTimeTV, requestStatusTV;

        public AdminRequestHolder(View itemView) {
            super(itemView);
            requestNameTV = itemView.findViewById(R.id.requestNameTV);
            requestTimeTV = itemView.findViewById(R.id.requestTimeTV);
            requestStatusTV = itemView.findViewById(R.id.requestStatusTV);
        }
    }
}
