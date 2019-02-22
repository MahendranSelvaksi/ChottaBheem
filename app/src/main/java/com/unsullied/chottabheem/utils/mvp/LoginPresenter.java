package com.unsullied.chottabheem.utils.mvp;

import android.app.Activity;
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
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginPresenter implements LoginMVP.Presenter {
    private Context mContext;
    private LoginMVP.View mView;
    private Utility myUtility;
    private Activity mActivity;
    private SessionManager sessionManager;

    public LoginPresenter(Context mContext, LoginMVP.View mView, Activity mActivity) {
        this.mContext = mContext;
        this.mView = mView;
        this.mActivity = mActivity;
        myUtility = new Utility();
        sessionManager = new SessionManager();
    }

    @Override
    public void callLoginAPI(String apiName, String versioncode, final String accountid, String phone, String loginType, String deviceId, String osName,
                             String deviceType) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("api::::" + apiName);
         /*   myUtility.printLogcat("VersionCode::::" + versioncode);
            myUtility.printLogcat("accountid::::" + accountid);
            myUtility.printLogcat("phone::::" + phone);
            myUtility.printLogcat("loginType::::" + loginType);
            myUtility.printLogcat("deviceId::::" + deviceId);
            myUtility.printLogcat("osName::::" + osName);
            myUtility.printLogcat("deviceType::::" + deviceType);*/
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + apiName)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, BuildConfig.VERSION_NAME)
                    .addBodyParameter(AppConstants.ACCOUNT_ID_KEY, accountid)
                    .addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .addBodyParameter(AppConstants.LOGIN_TYPE_KEY, loginType)
                    .addBodyParameter(AppConstants.DEVICE_ID_KEY, sessionManager.getValueFromSessionByKey(mContext,"FCM",AppConstants.DEVICE_ID_KEY))
                    .addBodyParameter(AppConstants.OS_NAME_KEY, osName)
                    .addBodyParameter(AppConstants.DEVICE_TYPE_KEY, deviceType)
                    .setPriority(Priority.HIGH)
                    .build();

            /*request.getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                @Override
                public void onResponse(Response okHttpResponse, JSONObject response) {
                    try {

                        myUtility.printLogcat("RESPOSE CODE:::" + okHttpResponse.code());
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        if (response != null) {
                            if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {

                                JSONObject userJsonObject = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                sessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY, userJsonObject.getInt(AppConstants.USER_ID_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY, userJsonObject.getString(AppConstants.NAME_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY, userJsonObject.getString(AppConstants.EMAIL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY, userJsonObject.getString(AppConstants.PHONE_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, userJsonObject.getString(AppConstants.ACCESS_TOKEN_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, userJsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, userJsonObject.getString(AppConstants.API_REDEEM_PROGRESS_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_OVERALL_REFERRAL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_CURRENT_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_CURRENT_REFERRAL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCOUNT_ID_KEY, accountid);
                                mView.showSuccess(1, "Login Successfully!!");
                            } else if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == 201) {
                               *//* JSONObject userJsonObject = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                sessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY, userJsonObject.getInt(AppConstants.USER_ID_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY, userJsonObject.getString(AppConstants.NAME_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY, userJsonObject.getString(AppConstants.EMAIL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY, userJsonObject.getString(AppConstants.PHONE_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, userJsonObject.getString(AppConstants.ACCESS_TOKEN_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, userJsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, userJsonObject.getString(AppConstants.API_REDEEM_PROGRESS_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_OVERALL_REFERRAL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_CURRENT_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_CURRENT_REFERRAL_KEY));*//*
                                mView.showSuccess(0, "Login Successfully!!");
                            }
                        } else {
                            mView.showError(1, AppConstants.COMMON_EXCEPTION);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });*/
            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        // myUtility.printLogcat("RESPOSE CODE:::" + okHttpResponse.code());
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        if (response != null) {
                            if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {

                                JSONObject userJsonObject = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                sessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY, userJsonObject.getInt(AppConstants.USER_ID_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY, userJsonObject.getString(AppConstants.NAME_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY, userJsonObject.getString(AppConstants.EMAIL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY, userJsonObject.getString(AppConstants.PHONE_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, userJsonObject.getString(AppConstants.ACCESS_TOKEN_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, userJsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, userJsonObject.getString(AppConstants.API_REDEEM_PROGRESS_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_OVERALL_REFERRAL_KEY));
                                //   sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_CURRENT_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_CURRENT_REFERRAL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCOUNT_ID_KEY, accountid);
                                mView.showSuccess(1, "Login Successfully!!");
                            } else if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == 201) {
                               /* JSONObject userJsonObject = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                sessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY, userJsonObject.getInt(AppConstants.USER_ID_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY, userJsonObject.getString(AppConstants.NAME_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY, userJsonObject.getString(AppConstants.EMAIL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY, userJsonObject.getString(AppConstants.PHONE_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, userJsonObject.getString(AppConstants.ACCESS_TOKEN_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, userJsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, userJsonObject.getString(AppConstants.API_REDEEM_PROGRESS_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_OVERALL_REFERRAL_KEY));
                                sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_CURRENT_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_CURRENT_REFERRAL_KEY));*/
                               //Call VerificationActivity and register api
                                mView.showSuccess(0, "Login Successfully!!");
                            }
                        } else {
                            mView.showError(1, AppConstants.COMMON_EXCEPTION);
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
    public void callUpdateLoginAPI(String apiName, String accountId, String phone, String versioncode, String paymentid, String name,
                                   String email, String paymentAmount, String osName, String deviceId, String deviceType, String referralCode) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("VersionCode::::" + versioncode);
            myUtility.printLogcat("accountid::::" + accountId);
            myUtility.printLogcat("paymentid::::" + paymentid);
            myUtility.printLogcat("paymentamount::::" + paymentAmount);
            myUtility.printLogcat("name::::" + name);
            myUtility.printLogcat("email::::" + email);
            myUtility.printLogcat("deviceos::::" + osName);
            myUtility.printLogcat("phone::::" + phone);
            myUtility.printLogcat("phone::::" + phone);
            myUtility.printLogcat("referralCode::::" + referralCode);
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + apiName)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, versioncode)
                    .addBodyParameter(AppConstants.ACCOUNT_ID_KEY, accountId)
                    .addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .addBodyParameter(AppConstants.REG_TYPE_KEY, "accountkitlogin")
                    .addBodyParameter(AppConstants.DEVICE_ID_KEY, sessionManager.getValueFromSessionByKey(mContext,"FCM",AppConstants.DEVICE_ID_KEY))
                    .addBodyParameter(AppConstants.OS_NAME_KEY, osName)
                    .addBodyParameter(AppConstants.DEVICE_TYPE_KEY, deviceType)
                    .addBodyParameter(AppConstants.NAME_KEY, name)
                    .addBodyParameter(AppConstants.EMAIL_KEY, email)
                    .addBodyParameter(AppConstants.API_REFERRAL_CODE_KEY, referralCode)
                    .addBodyParameter(AppConstants.PAYMENT_ID_KEY, paymentid)
                    .addBodyParameter(AppConstants.PAYMENT_AMOUNT_KEY, paymentAmount)
                    .addBodyParameter(AppConstants.PAYMENT_VIA_KEY, "payumoney")
                    .setPriority(Priority.HIGH)
                    .build();

           /* request.getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                @Override
                public void onResponse(Response okHttpResponse, JSONObject response) {

                }

                @Override
                public void onError(ANError anError) {
                    mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });*/
            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Registration API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            JSONObject userJsonObject = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            sessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY, userJsonObject.getInt(AppConstants.USER_ID_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY, userJsonObject.getString(AppConstants.NAME_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY, userJsonObject.getString(AppConstants.EMAIL_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY, userJsonObject.getString(AppConstants.PHONE_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, userJsonObject.getString(AppConstants.ACCESS_TOKEN_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, userJsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, userJsonObject.getString(AppConstants.API_REDEEM_PROGRESS_KEY));
                            sessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, userJsonObject.getString(AppConstants.API_OVERALL_REFERRAL_KEY));
                            mView.showSuccess(0, "Login Successfully!!");
                        } else if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == 202) {
                           mView.showError(2, response.getString(AppConstants.API_MESSAGE_KEY));
                            JSONArray jsonArray=response.getJSONArray(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            List<Object> list=new ArrayList<>();
                            list.add("Select Referral Code");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object=jsonArray.getJSONObject(i);
                                String spnrData=object.getString("name").concat("-").concat(object.getString("referral_code"));
                                list.add(spnrData);
                            }
                            mView.showListOfReferralCode(list);
                        } else if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == 201 ||
                                response.getInt(AppConstants.API_STATUS_CODE_KEY) == 204) {
                            mView.showError(2, response.getString(AppConstants.API_MESSAGE_KEY));
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
