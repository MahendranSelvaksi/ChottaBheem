package com.unsullied.chottabheem.utils.holderClass;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.CustomTextView;

public class AdminRequestHolder extends RecyclerView.ViewHolder {
    public CustomTextView requestNameTV, requestTimeTV, requestStatusTV,requestStatusKeyTV;


    public AdminRequestHolder(View itemView) {
        super(itemView);
        requestNameTV = itemView.findViewById(R.id.requestNameTV);
        requestTimeTV = itemView.findViewById(R.id.requestTimeTV);
        requestStatusTV = itemView.findViewById(R.id.requestStatusTV);
        requestStatusKeyTV = itemView.findViewById(R.id.requestStatusKeyTV);
    }
}
