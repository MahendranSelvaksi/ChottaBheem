package com.unsullied.chottabheem.utils.mvp;

import org.json.JSONObject;

public interface RechargeMVP {

    public interface RechargeView{
        void showError(String errorMsg);
        void showSuccess(JSONObject successJSON);
    }
    public interface Presenter{
        void callRechargeAPI(String url);
    }
}
