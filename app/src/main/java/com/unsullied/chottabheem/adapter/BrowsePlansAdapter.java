package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.CustomTextView;
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
        if (holder instanceof PlansViewHolder) {
            ((PlansViewHolder) holder).validityLayout.setVisibility(data.get(position).getAmount().equalsIgnoreCase("NA") ? View.GONE : View.VISIBLE);
            ((PlansViewHolder) holder).planDetailTV.setText(data.get(position).getDetail().trim());
            ((PlansViewHolder) holder).planAmountTV.setText(mContext.getString(R.string.Rs).concat(" ").concat(data.get(position).getAmount().trim()));
            ((PlansViewHolder) holder).validityValueTV.setText(data.get(position).getValidity());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class PlansViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView planDetailTV, validityValueTV, planAmountTV;
        private LinearLayout validityLayout;

        PlansViewHolder(View itemView) {
            super(itemView);
            planDetailTV = itemView.findViewById(R.id.planDetailTV);
            validityValueTV = itemView.findViewById(R.id.validityValueTV);
            planAmountTV = itemView.findViewById(R.id.planAmountTV);
            validityLayout = itemView.findViewById(R.id.validityLayout);
        }
    }
}
