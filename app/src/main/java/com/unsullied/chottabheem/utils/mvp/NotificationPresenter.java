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
import com.unsullied.chottabheem.utils.RedeemModel;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenter implements NotificationMVP.Presenter {

    private Context mContext;
    private RedeemMVP.View mView;
    private Utility myUtility;
    private Activity mActivity;
    private SessionManager sessionManager;

    public NotificationPresenter(Context mContext, RedeemMVP.View mView, Activity mActivity) {
        this.mContext = mContext;
        this.mView = mView;
        this.mActivity = mActivity;
        myUtility = new Utility();
        sessionManager = new SessionManager();
    }

    @Override
    public void getNotification() {
        if (ConnectivityReceiver.isConnected()) {
            //myUtility.printLogcat("api::::" + apiName);
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
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.NOTIFICATION_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .setTag(AppConstants.APP_NAME)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, String.valueOf(BuildConfig.VERSION_CODE))
                    .addBodyParameter(AppConstants.ACCESS_TOKEN_KEY, sessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.ACCESS_TOKEN_KEY))
                    //.addBodyParameter(AppConstants.PHONE_KEY, phone)
                    .addBodyParameter(AppConstants.USER_ID_KEY, String.valueOf(sessionManager.isLogged(mContext)))
                    .addBodyParameter(AppConstants.DEVICE_OS_KEY, AppConstants.OS_NAME_VALUE)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Login API Response:::" + response.toString());
                        List<RedeemModel> list=new ArrayList<>();
                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.API_STATUS_CODE_VALUE) {
                            JSONArray jsonArray=response.getJSONArray(AppConstants.RESPONSE_JSON_OBJECT_KEY);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                RedeemModel model = new RedeemModel();
                                model.setRequestId(object.getString("id"));
                                model.setMessage(object.getString("message"));
                                model.setRequestTime(object.getString("date"));
                                list.add(model);
                            }
                        }
                        mView.updateAdapter(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    myUtility.printLogcat("ErrorCode:::" + anError.getErrorCode());
                    myUtility.printLogcat("ErrorMessage:::" + anError.getErrorDetail());
                    myUtility.printLogcat("ErrorBody:::" + anError.getErrorBody());
                //    mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });
        } else {
           // mView.showError(0, AppConstants.NO_CONNECTION_ERROR);
        }

    }
}
