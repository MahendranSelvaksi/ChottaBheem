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
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONObject;

public class ProfilePresenter implements ProfileMVP.Presenter {
    private Context mContext;
    private ProfileMVP.View mView;
    private Utility myUtility;

    public ProfilePresenter(Context mContext, ProfileMVP.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        myUtility = new Utility();
    }

    @Override
    public void callGetProfileAPI(String userId, String accessToken, String deviceOs, String versionCode) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("VersionCode::::" + versionCode);
            myUtility.printLogcat("accessToken::::" + accessToken);
            myUtility.printLogcat("userId::::" + userId);
            myUtility.printLogcat("deviceOs::::" + deviceOs);

            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.GET_PROFILE_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, versionCode)
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, accessToken)
                    //.addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .addBodyParameter(AppConstants.USER_ID_KEY, userId)
                    .addBodyParameter(AppConstants.DEVICE_OS_KEY, deviceOs)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            mView.updateProfile(response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY));
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
    public void callContactUsAPI(String apiName, int userId, String accessToken, String name, String email, String deviceOs,
                                 String versionCode, String mobileNumber, String message) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("VersionCode::::" + versionCode);
            myUtility.printLogcat("accessToken::::" + accessToken);
            myUtility.printLogcat("userId::::" + userId);
            myUtility.printLogcat("deviceOs::::" + deviceOs);
            myUtility.printLogcat("Name::::" + name);
            myUtility.printLogcat("Email::::" + email);
            myUtility.printLogcat("mobile number::::" + mobileNumber);
            myUtility.printLogcat("message::::" + message);

            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + apiName)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, versionCode)
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, accessToken)
                    .addBodyParameter(AppConstants.PHONE_KEY, mobileNumber)
                    .addBodyParameter(AppConstants.NAME_KEY, name)
                    .addBodyParameter(AppConstants.EMAIL_KEY, email)
                    .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(userId))
                    .addBodyParameter(AppConstants.DEVICE_OS_KEY, deviceOs)
                    .addBodyParameter(AppConstants.API_MESSAGE_KEY, message)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            mView.showSuccess(0, response.getString(AppConstants.API_MESSAGE_KEY));
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
    public void updateProfile(int userId, String accessToken, String name, String email, String mobileNumber) {
        if (ConnectivityReceiver.isConnected()) {
            myUtility.printLogcat("accessToken::::" + accessToken);
            myUtility.printLogcat("userId::::" + userId);
            //myUtility.printLogcat("deviceOs::::" + deviceOs);
            myUtility.printLogcat("Name::::" + name);
            myUtility.printLogcat("Email::::" + email);
            myUtility.printLogcat("mobile number::::" + mobileNumber);

            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.UPDATE_PROFILE_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, BuildConfig.VERSION_NAME)
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, accessToken)
                    .addBodyParameter(AppConstants.PHONE_KEY, mobileNumber)
                    .addBodyParameter(AppConstants.NAME_KEY, name)
                    .addBodyParameter(AppConstants.EMAIL_KEY, email)
                    .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(userId))
                    //.addBodyParameter(AppConstants.DEVICE_OS_KEY, deviceOs)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            mView.showSuccess(0, response.getString("data"));
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
}
