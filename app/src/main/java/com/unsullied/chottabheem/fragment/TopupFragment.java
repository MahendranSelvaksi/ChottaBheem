package com.unsullied.chottabheem.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.activity.BrowsePlansActivity;
import com.unsullied.chottabheem.adapter.BrowsePlansAdapter;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.dataModel.BrowsePlansChildModel;
import com.unsullied.chottabheem.utils.mvp.BrowsPlansPresenter;
import com.unsullied.chottabheem.utils.mvp.PlansMVP;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopupFragment extends Fragment implements PlansMVP.PlansView {
    private RelativeLayout includeLayout;
    private RecyclerView mRecyclerView;
    private CustomTextView mNoDataTV;

    private Context mContext;
    private List<BrowsePlansChildModel> mPlansData;
    private BrowsePlansAdapter mPlansAdapter;
    private BrowsPlansPresenter mPlansPresenter;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;

    public TopupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_topup, container, false);

        mContext = getActivity();
        mActivity = getActivity();
        mPlansData = new ArrayList<>();
        mPlansPresenter = new BrowsPlansPresenter(mContext, this);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
   //     mProgressDialog.show();

        includeLayout = rootView.findViewById(R.id.topupPlansLayout);
        mRecyclerView = includeLayout.findViewById(R.id.commonRecyclerView);
        mNoDataTV = includeLayout.findViewById(R.id.commonNoDataTV);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPlansAdapter = new BrowsePlansAdapter(mContext, mPlansData);
        mRecyclerView.setAdapter(mPlansAdapter);
        String url = AppConstants.LIVE_URL + AppConstants.RECHARGE_PLAN_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.TYPE_KEY + AppConstants.TUP_VALUE +
                AppConstants.OPERATOR_ID_KEY + BrowsePlansActivity.operatorId;
        mPlansPresenter.getPlans(url, AppConstants.DATA_VALUE);

        return rootView;
    }

    @Override
    public void loadPlansData(List<BrowsePlansChildModel> mData) {
        mProgressDialog.dismiss();
        mPlansData.addAll(mData);
        if (mPlansData.size() > 0) {
            mPlansAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void errorShow(String errorMsg) {
        mProgressDialog.dismiss();
        mPlansAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.GONE);
        mNoDataTV.setVisibility(View.VISIBLE);
        mNoDataTV.setText(errorMsg.trim());
    }

}
