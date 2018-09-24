package com.unsullied.chottabheem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.activity.SelectOperatorActivity;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SectionRecyclerViewAdapter;
import com.unsullied.chottabheem.utils.dataModel.OperatorSelectionModel;

import java.util.List;

public class OperationSelectionAdapter extends /*SectionedRecyclerViewAdapter*/ SectionRecyclerViewAdapter {
    private Context mContext;
    private List<OperatorSelectionModel> mData;
    private Activity mActivity;

    public OperationSelectionAdapter(Context mContext, List<OperatorSelectionModel> mData, Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
    }
    //  OnItemClickListener onItemClickListener;

    @Override
    protected int getSectionCount() {
        return mData.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return mData.get(section).isOpen() ? mData.get(section).getCircleList().size() : 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new CircleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_adapter, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).mSubheaderText.setText(mData.get(section).getOperatorName().trim());
            ((HeaderHolder) holder).mSubheaderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData.get(section).isClickable()) {
                        if (mActivity instanceof SelectOperatorActivity) {
                            ((SelectOperatorActivity) mActivity).callPrevIntent(section, -1);
                        }
                    }else {
                        mData.get(holder.getAdapterPosition()).setOpen(!mData.get(holder.getAdapterPosition()).isOpen());
                        notifyDataSetChanged();
                    }
                }
            });

            ((HeaderHolder) holder).mArrow.setImageResource(mData.get(section).isOpen() ? R.drawable.ic_expand_less : R.drawable.ic_keyboard_arrow_down_black_24dp);
            /*((HeaderHolder) holder).mArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if (mData.get(holder.getAdapterPosition()).isOpen()){
                    mData.get(holder.getAdapterPosition()).setOpen(!mData.get(holder.getAdapterPosition()).isOpen());
                    notifyDataSetChanged();
                    //}
                }
            });*/

            ((HeaderHolder) holder).mArrow.setVisibility(mData.get(section).getCircleList().size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        if (holder instanceof CircleHolder) {
            ((CircleHolder) holder).circleTV.setText(mData.get(section).getCircleList().get(position).getCircleName());
            ((CircleHolder) holder).circleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActivity instanceof SelectOperatorActivity) {
                        ((SelectOperatorActivity) mActivity).callPrevIntent(section, position);
                    }
                }
            });
        }
    }

    /*
        public interface OnItemClickListener {
            void onItemClicked(OperatorSelectionModel model);
            void onSubheaderClicked(int position);
        }*/


 /*   @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new CircleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_adapter, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {
        if (holder instanceof CircleHolder){
            ((CircleHolder) holder).circleTV.setText();
        }

    }

    @Override
    public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {
if (subheaderHolder instanceof HeaderHolder){
    boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

    if (isSectionExpanded) {
        ((HeaderHolder) subheaderHolder).mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
    } else {
        ((HeaderHolder) subheaderHolder).mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
    }

    ((HeaderHolder) subheaderHolder).itemView.setOnClickListener(v -> onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition()));
}
    }

    @Override
    public int getItemSize() {
        return mData.size();
    }*/


    private class HeaderHolder extends RecyclerView.ViewHolder {

        // private static Typeface meduiumTypeface = null;

        CustomTextView mSubheaderText;
        ImageView mArrow;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = itemView.findViewById(R.id.subheaderText);
            this.mArrow = itemView.findViewById(R.id.arrow);

        }

    }

    private class CircleHolder extends RecyclerView.ViewHolder {
        private CustomTextView circleTV;

        public CircleHolder(View itemView) {
            super(itemView);
            circleTV = itemView.findViewById(R.id.circleTV);
        }
    }

}
