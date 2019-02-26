package com.unsullied.chottabheem.utils.mvp;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class RechargePresenter implements RechargeMVP.Presenter {

    private Context mContext;
    private RechargeMVP.RechargeView mRechargeView;
    private Utility myUtility;
    private SessionManager mSessionManager;

    public RechargePresenter(Context mContext, RechargeMVP.RechargeView mRechargeView) {
        this.mContext = mContext;
        this.mRechargeView = mRechargeView;
        myUtility=new Utility();
        mSessionManager = new SessionManager();
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
                        myUtility.printLogcat(response.toString());
                        if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                            JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            mRechargeView.showSuccess(responseJSON);
                            if (responseJSON.getInt(AppConstants.RES_CODE_KEY) == AppConstants.RES_CODE_VALUE || responseJSON.getInt(AppConstants.RES_CODE_KEY)==201) {
                                Toast.makeText(mContext, "Recharge successfully", Toast.LENGTH_SHORT).show();
                                mRechargeView.clearView();
                            } else {
                               // selectedCircleIdFromApi = false;
                                Toast.makeText(mContext, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void updateStatus(String paymentId,String paymentStatus,String rechargeURL,JSONObject json) {
        if (ConnectivityReceiver.isConnected()) {
            try {
            myUtility.printLogcat("api::::" + AppConstants.RECHARGE_STATUS_API);
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = null;

                request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.RECHARGE_STATUS_API)
                        .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                        .addHeaders("Authorization", "Basic " + encoding)
                        .setTag(AppConstants.APP_NAME)
                        .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, mSessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.ACCESS_TOKEN_KEY))
                        .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(mSessionManager.isLogged(mContext)))
                        .addBodyParameter(AppConstants.PAYMENT_ID_KEY, paymentId)
                        .addBodyParameter(AppConstants.API_ORDER_ID_KEY, json.getString("orderId"))
                        .addBodyParameter(AppConstants.API_RECHARGE_PARAM_KEY, rechargeURL)
                        .addBodyParameter(AppConstants.API_RETURN_JSON_KEY, json.toString())
                        .addBodyParameter(AppConstants.API_RECHARGE_AMOUNT_KEY, json.getString("amount"))
                        .addBodyParameter(AppConstants.REACH_API_STATUS_KEY, json.getString("status"))
                        .addBodyParameter(AppConstants.API_PAYMENT_STATUS_KEY, paymentStatus)
                        .setPriority(Priority.HIGH)
                        .build();


            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    myUtility.printLogcat(response.toString());
                       /* if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == 200) {
                            //mView.showSuccess(Integer.parseInt(paymentStatus) == 2 ? 0 : 1001, "Successfully updated!!!");
                        } else {
                           // mView.showError(response.getString(AppConstants.API_MESSAGE_KEY));
                        }*/
                }

                @Override
                public void onError(ANError anError) {

                }
            });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
