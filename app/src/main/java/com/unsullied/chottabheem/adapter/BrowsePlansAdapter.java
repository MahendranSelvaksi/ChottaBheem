package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.dataModel.BrowsePlansChildModel;

import java.util.List;

public class BrowsePlansAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BrowsePlansChildModel> data;

    public BrowsePlansAdapter(Context mContext, List<BrowsePlansChildModel> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.plans_adapter, parent, false);
        return new PlansViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class PlansViewHolder extends RecyclerView.ViewHolder {
        public PlansViewHolder(View itemView) {
            super(itemView);
        }
    }
}
