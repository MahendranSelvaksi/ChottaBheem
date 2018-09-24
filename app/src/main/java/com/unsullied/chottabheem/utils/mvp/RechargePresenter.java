package com.unsullied.chottabheem.utils.mvp;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONObject;

public class RechargePresenter implements RechargeMVP.Presenter {

    private Context mContext;
    private RechargeMVP.RechargeView mRechargeView;
    private Utility myUtility;

    public RechargePresenter(Context mContext, RechargeMVP.RechargeView mRechargeView) {
        this.mContext = mContext;
        this.mRechargeView = mRechargeView;
        myUtility=new Utility();
    }

    @Override
    public void callRechargeAPI(String url) {
        if (ConnectivityReceiver.isConnected()) {
            ANRequest request = AndroidNetworking.get(url)
                    .setTag(AppConstants.APP_NAME)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                            JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            if (responseJSON.getInt(AppConstants.RES_CODE_KEY) == AppConstants.RES_CODE_VALUE) {

                            } else {
                           //     selectedCircleIdFromApi = false;
                           //     Toast.makeText(RechargeActivity.this, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError error) {
                    if (error.getErrorCode() != 0) {
                        // received error from server
                        // error.getErrorCode() - the error code from server
                        // error.getErrorBody() - the error body from server
                        // error.getErrorDetail() - just an error detail
                        myUtility.printLogcat("onError errorCode : " + error.getErrorCode());
                        myUtility.printLogcat("onError errorBody : " + error.getErrorBody());
                        myUtility.printLogcat("onError errorDetail : " + error.getErrorDetail());
                        // get parsed error object (If ApiError is your class)

                    } else {
                        // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                        myUtility.printLogcat("onError errorDetail : " + error.getErrorDetail());

                    }
                    error.printStackTrace();
                }

            });
        } else {
          //  Toast.makeText(this, "" + AppConstants.NO_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
