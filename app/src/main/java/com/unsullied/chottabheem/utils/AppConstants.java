package com.unsullied.chottabheem.utils;

import android.Manifest;
import android.util.Base64;

import com.unsullied.chottabheem.BuildConfig;

import java.io.UnsupportedEncodingException;

public class AppConstants {

    public static final String APP_NAME = "Chotta bheem";

    public static final boolean DEBUG = true;
    public static final String TAG = "Chotta Bheem";

    /*Pay Bill Activity*/
    public static final String TITLE_INTENT_KEY = "IntentTitle";
    public static final String HINT_INTENT_KEY = "IntentHint";

    public static final int OPERATOR_INTENT_REQUEST_CODE = 10001;


    /*Network Library Error */
    public static final String TIMEOUT_ERROR = "Oops! Time out while connecting to server! Please try again";
    public static final String AUTHENDICATION_ERROR = "There is an authentication failure error while connecting to server. Please try again";
    public static final String SERVER_ERROR = "There is an unexpected server error while connecting to server. Please try again";
    public static final String NETWORK_ERROR = "There is a network error while connecting to server. Please try again";
    public static final String PARSE_ERROR = "There is an unexpected Parse error while connecting to server. Please try again";
    public static final String NO_CONNECTION_ERROR = "Network currently unavailable!";

    public static final String COMMON_EXCEPTION = "Something went wrong!!!";


    /*App Update Dialoge*/
    public static final String UPDATE_CONTENT = "A new version of " + AppConstants.APP_NAME + " is available.Please update to the version " + BuildConfig.VERSION_CODE + " now.";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.mindmade.graspclothings";
    //public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";


    /*Marshmallow Permissions*/
    public static final int CALL_PHONE_REQUEST_CODE = 1;
    public static final int ALL_REQUEST_CODE = 0;

    public static final String[] ALL_PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };
    public static final String CALL_PHONE_PERMISSIONS = Manifest.permission.CALL_PHONE;

    public static String convertBase64(String text) {
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

}
