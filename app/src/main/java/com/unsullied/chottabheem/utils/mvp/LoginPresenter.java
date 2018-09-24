package com.unsullied.chottabheem.utils.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONObject;

import okhttp3.Response;

public class LoginPresenter implements LoginMVP.Presenter {
    private Context mContext;
    private LoginMVP.View mView;
    private Utility myUtility;
    private Activity mActivity;

    public LoginPresenter(Context mContext, LoginMVP.View mView, Activity mActivity) {
        this.mContext = mContext;
        this.mView = mView;
        this.mActivity = mActivity;
        myUtility = new Utility();
    }

    @Override
    public void callLoginAPI(String apiName, String versioncode, String accountid, String phone, String loginType, String deviceId, String osName,
                             String deviceType) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("VersionCode::::"+versioncode);
            myUtility.printLogcat("accountid::::"+accountid);
            myUtility.printLogcat("phone::::"+phone);
            myUtility.printLogcat("loginType::::"+loginType);
            myUtility.printLogcat("deviceId::::"+deviceId);
            myUtility.printLogcat("osName::::"+osName);
            myUtility.printLogcat("deviceType::::"+deviceType);
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode (loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + apiName)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, versioncode)
                    .addBodyParameter(AppConstants.ACCOUNT_ID_KEY, accountid)
                    //.addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .addBodyParameter(AppConstants.LOGIN_TYPE_KEY, loginType)
                    .addBodyParameter(AppConstants.DEVICE_ID_KEY, deviceId)
                    .addBodyParameter(AppConstants.OS_NAME_KEY, osName)
                    .addBodyParameter(AppConstants.DEVICE_TYPE_KEY, deviceType)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                @Override
                public void onResponse(Response okHttpResponse, JSONObject response) {
                   myUtility.printLogcat("RESPOSE CODE:::"+ okHttpResponse.code());
                    myUtility.printLogcat("Login API Response:::" + response.toString());
                }

                @Override
                public void onError(ANError anError) {

                }
            });
/*
            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        request.get
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {

                }
            });*/
        } else {
            mView.showError(0, AppConstants.NO_CONNECTION_ERROR);
        }
    }

    @Override
    public void callUpdateLoginAPI(String apiName, int userId, String accessToken, String versioncode, String paymentid, String name, String email, String paymentAmount, String deviceOs) {

    }
}
