package com.unsullied.chottabheem.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.activity.BrowsePlansActivity;
import com.unsullied.chottabheem.adapter.BrowsePlansAdapter;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ClickListener;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.RecyclerTouchListener;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.dataModel.BrowsePlansChildModel;
import com.unsullied.chottabheem.utils.mvp.BrowsPlansPresenter;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayMVP;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayPresenter;
import com.unsullied.chottabheem.utils.mvp.PlansMVP;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FTTFragment extends Fragment implements PlansMVP.PlansView,PaymentGatewayMVP.View{

    private RelativeLayout includeLayout;
    private RecyclerView mRecyclerView;
    private CustomTextView mNoDataTV;

    private Context mContext;
    private List<BrowsePlansChildModel> mPlansData;
    private BrowsePlansAdapter mPlansAdapter;
    private BrowsPlansPresenter mPlansPresenter;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private Utility myUtility;
    private PaymentGatewayPresenter mPaymentGatewayPresenter;
    private String rechargeAmount;
    private AppPreference mAppPreference;



    public FTTFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_ftt, container, false);

        mContext = getActivity();
        mActivity = getActivity();
        myUtility = new Utility();
        mAppPreference = new AppPreference();
        mPlansData = new ArrayList<>();
        mPlansPresenter = new BrowsPlansPresenter(mContext, this);
        mPaymentGatewayPresenter = new PaymentGatewayPresenter(mContext, mActivity, this);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
      //  mProgressDialog.show();

        includeLayout = rootView.findViewById(R.id.fttPlansLayout);
        mRecyclerView = includeLayout.findViewById(R.id.commonRecyclerView);
        mNoDataTV = includeLayout.findViewById(R.id.commonNoDataTV);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPlansAdapter = new BrowsePlansAdapter(mContext, mPlansData);
        mRecyclerView.setAdapter(mPlansAdapter);
        String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_PLAN_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.TYPE_KEY + AppConstants.FTT_VALUE +
                AppConstants.OPERATOR_ID_KEY + BrowsePlansActivity.operatorId;
        mPlansPresenter.getPlans(url, AppConstants.DATA_VALUE);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                rechargeAmount = mPlansData.get(position).getAmount();
                mPaymentGatewayPresenter.launchPayUMoneyFlow(rechargeAmount, BrowsePlansActivity.selectedMobileNumber,
                        BrowsePlansActivity.emailIdStr);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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

    @Override
    public void getSuccessfulHash(PayUmoneySdkInitializer.PaymentParam mPaymentParams) {
        if (AppPreference.selectedTheme != -1) {
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
        } else {
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
        }
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                new AlertDialog.Builder(mActivity)
                        .setCancelable(false)
                        .setTitle("Payment Status")
                        .setMessage("Successfully Added payment....")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                long time = System.currentTimeMillis();
                                try {
                                    String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                                            AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + BrowsePlansActivity.selectedMobileNumber +
                                            AppConstants.AMOUNT_KEY + rechargeAmount + AppConstants.OPERATOR_ID_KEY + BrowsePlansActivity.operatorId +
                                            AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + URLEncoder.encode("", "utf-8") +
                                            AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");
                                    myUtility.printLogcat("API::::" + url);

                                    //mRechargePresenter.callRechargeAPI(url);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                myUtility.printLogcat("Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                myUtility.printLogcat("Both objects are null!");
            }
        }
    }

}
