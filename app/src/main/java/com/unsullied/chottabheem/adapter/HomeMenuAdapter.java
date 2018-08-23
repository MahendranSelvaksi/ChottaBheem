package com.unsullied.chottabheem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unsullied.chottabheem.R;

import java.util.List;
import java.util.Random;

public class HomeMenuAdapter extends RecyclerView.Adapter {

    List<String> mData;
    private Context mContext;
    int[] mIcons;

    public HomeMenuAdapter(Context mContext, List<String> mData, int[] mIcons) {
        this.mContext = mContext;
        this.mData = mData;
        this.mIcons = mIcons;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.home_menu_adapter, parent, false);
        return new MenuHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MenuHolder) {
            ((MenuHolder) holder).titleTV.setText(mData.get(position));
            ((MenuHolder) holder).menuIconIV.setImageResource(mIcons[position]);


        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private int getDynamicColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private class MenuHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView menuIconIV;

        public MenuHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            menuIconIV = itemView.findViewById(R.id.menuIconIV);
        }
    }
}
