package com.unsullied.chottabheem.utils.mvp;

public interface LoginMVP {

    interface View{
        void showSuccess(String message);
        void showError(int code,String errorMsg);
    }

    interface Presenter{
        void callLoginAPI(String apiName,String versioncode,String accountid,String phone,
                          String loginType,String deviceId,String osName,String deviceType);
        void callUpdateLoginAPI(String apiName,int userId,String accessToken,String versioncode,String paymentid,String name,
                          String email,String paymentAmount,String deviceOs);
    }
}
