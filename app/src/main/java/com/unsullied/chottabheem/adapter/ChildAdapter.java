package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.dataModel.ChildModel;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter {
    List<ChildModel> childList;
    private Context mContext;

    public ChildAdapter(Context mContext, List<ChildModel> childList) {
        this.mContext = mContext;
        this.childList = childList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.child_adapter, parent, false);
        return new ChildHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildHolder) {
            ((ChildHolder) holder).childNameTV.setText(childList.get(position).getChildName().trim());
            ((ChildHolder) holder).childReferralCodeTV.setText(childList.get(position).getChildReferralCode().trim());
            ((ChildHolder) holder).childEmailTV.setText(childList.get(position).getChildEmailId().trim());
        }

    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    private class ChildHolder extends RecyclerView.ViewHolder {
        private CustomTextView childNameTV, childEmailTV, childReferralCodeTV;

        public ChildHolder(View itemView) {
            super(itemView);
            childNameTV = itemView.findViewById(R.id.childNameTV);
            childEmailTV = itemView.findViewById(R.id.childEmailTV);
            childReferralCodeTV = itemView.findViewById(R.id.childReferralCodeTV);
        }
    }
}
