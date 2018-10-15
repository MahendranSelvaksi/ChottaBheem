package com.unsullied.chottabheem.utils.mvp;

import org.json.JSONObject;

public interface ProfileMVP {

    interface View{
        void updateProfile(JSONObject profileJSON);
        void showError(int code,String errorMsg);
        void showSuccess(int code ,String message);
    }

    interface Presenter{
        void callGetProfileAPI(String userId,String accessToken,String deviceOs,String versionCode);

        void callContactUsAPI(String apiName,int userId,String accessToken,String name,String email,String deviceOs,String versionCode,
                              String mobileNumber,String message);

        void updateProfile(int userID,String accessToken,String name,String email,String mobileNumber);
    }
}
