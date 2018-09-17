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
import com.unsullied.chottabheem.utils.dataModel.BrowsePlansChildModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowsPlansPresenter implements PlansMVP.PlansPresenter {
    private Context mContext;
    private PlansMVP.PlansView mPlansView;
    private Utility myUtility;

    public BrowsPlansPresenter(Context mContext, PlansMVP.PlansView mPlansView) {
        this.mContext = mContext;
        this.mPlansView = mPlansView;
        myUtility = new Utility();
    }

    @Override
    public void getPlans(String url, final String hint) {
        if (ConnectivityReceiver.isConnected()) {

            myUtility.printLogcat("API NAME::::" + url);
            // AppController.getInstance().clearAllQueue();
            ANRequest request = AndroidNetworking.get(url)
                    .setTag(AppConstants.APP_NAME)
                    .setPriority(Priority.HIGH)
                    .build();
            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("RESPONSE:::" + response.toString());
                        JSONObject resultJSON = new JSONObject(response.toString());

                        if (resultJSON.getInt(AppConstants.RESULT_CODE_KEY) == AppConstants.RES_CODE_VALUE) {
                            if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                                JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                List<BrowsePlansChildModel> data = new ArrayList<>();
                                JSONArray jsonArray = responseJSON.getJSONObject("data").getJSONArray(hint);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    BrowsePlansChildModel model = new BrowsePlansChildModel();
                                    model.setAmount(object.getString("amount"));
                                    model.setTalktime(object.getString("talktime"));
                                    model.setValidity(object.getString("validity"));
                                    model.setDetail(object.getString("detail"));
                                    data.add(model);
                                }
                                mPlansView.loadPlansData(data);
                            }

                        } else {
                            mPlansView.errorShow(resultJSON.getString(AppConstants.RES_TEXT_KEY));
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

        }
    }
}
