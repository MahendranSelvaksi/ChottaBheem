package com.unsullied.chottabheem.utils.mvp;

import android.content.Context;
import android.util.Base64;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.unsullied.chottabheem.BuildConfig;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.RedeemModel;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedeemPresenter implements RedeemMVP.Presenter {

    private Context mContext;
    private ProfileMVP.View mView;
    private RedeemMVP.View mRedeemView;
    private SessionManager sessionManager;
    private Utility myUtility;

    public RedeemPresenter(Context mContext, ProfileMVP.View mView, RedeemMVP.View mRedeemView) {
        this.mContext = mContext;
        this.mView = mView;
        this.mRedeemView = mRedeemView;
        myUtility = new Utility();
        sessionManager = new SessionManager();
    }

    public RedeemPresenter(Context mContext, ProfileMVP.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        this.mRedeemView = mRedeemView;
        myUtility = new Utility();
        sessionManager = new SessionManager();
    }

    @Override
    public void getRedeemList() {
        if (ConnectivityReceiver.isConnected()) {
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.GET_REDEEM_LIST_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, BuildConfig.VERSION_NAME)
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, sessionManager.getValueFromSessionByKey(mContext,
                            AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY))
                    .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(sessionManager.isLogged(mContext)))
                    .addBodyParameter(AppConstants.DEVICE_OS_KEY, AppConstants.OS_NAME_VALUE)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Get Redeem API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            myUtility.printLogcat("Come here");
                            List<RedeemModel> mDataList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                RedeemModel model = new RedeemModel();
                                model.setRequestId(object.getString(AppConstants.REDEEM_REQUEST_ID_KEY));
                                model.setRequestName(object.getString(AppConstants.REDEEM_REQUEST_NAME_KEY));
                                model.setRequestTime(object.getString(AppConstants.REDEEM_REQUEST_TIME_KEY));
                                model.setRequestStatus(object.getString(AppConstants.REDEEM_REQUEST_STATUS_KEY));
                                //    model.setMessage(object.getString(AppConstants.API_MESSAGE_KEY));
                                mDataList.add(model);
                            }
                            mRedeemView.updateAdapter(mDataList);
                        } else {
                            mView.showError(1, response.getString(AppConstants.API_MESSAGE_KEY));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    myUtility.printLogcat("ErrorCode:::" + anError.getErrorCode());
                    myUtility.printLogcat("ErrorMessage:::" + anError.getErrorDetail());
                    myUtility.printLogcat("ErrorBody:::" + anError.getErrorBody());
                    mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });
        } else {
            mView.showError(0, AppConstants.NO_CONNECTION_ERROR);
        }

    }

    @Override
    public void addRedeem(String redeemName, String phone, String operator, String redeemValue) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("VersionCode::::" + BuildConfig.VERSION_NAME);
            myUtility.printLogcat("accessToken::::" + sessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY));
            myUtility.printLogcat("userId::::" + sessionManager.isLogged(mContext));
            myUtility.printLogcat("deviceOs::::" + AppConstants.OS_NAME_VALUE);
            myUtility.printLogcat("redeemName::::" + redeemName);
            myUtility.printLogcat("phone::::" + phone);
            myUtility.printLogcat("redeemValue::::" + redeemValue);

            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.ADD_REDEEM_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, BuildConfig.VERSION_NAME)
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, sessionManager.getValueFromSessionByKey(mContext,
                            AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY))
                    .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(sessionManager.isLogged(mContext)))
                    .addBodyParameter(AppConstants.OS_NAME_KEY, AppConstants.OS_NAME_VALUE)

                    .addBodyParameter(AppConstants.API_REDEEM_NAME_KEY, redeemName)
                    .addBodyParameter(AppConstants.API_REDEEM_VALUE_KEY, redeemValue)
                    .addBodyParameter(AppConstants.API_OPERATOR_KEY, operator)
                    .addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Add Redeem API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            mView.showSuccess(1, response.getString(AppConstants.API_MESSAGE_KEY));
                        } else {
                            mView.showError(1, response.getString(AppConstants.API_MESSAGE_KEY));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    myUtility.printLogcat("ErrorCode:::" + anError.getErrorCode());
                    myUtility.printLogcat("ErrorMessage:::" + anError.getErrorDetail());
                    myUtility.printLogcat("ErrorBody:::" + anError.getErrorBody());
                    mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });
        } else {
            mView.showError(0, AppConstants.NO_CONNECTION_ERROR);
        }
    }
}
