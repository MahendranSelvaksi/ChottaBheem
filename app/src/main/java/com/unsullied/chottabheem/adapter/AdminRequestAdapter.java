package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unsullied.chottabheem.R;

public class AdminRequestAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public AdminRequestAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.admin_request_adapter, parent, false);
        return new AdminRequestHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class AdminRequestHolder extends RecyclerView.ViewHolder {
        public AdminRequestHolder(View itemView) {
            super(itemView);
        }
    }
}
