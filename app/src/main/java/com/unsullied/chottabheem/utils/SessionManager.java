package com.unsullied.chottabheem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {

    public void addValueToSession(Context mContext, String sessionName, String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(sessionName, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValueFromSessionByKey(Context mContext, String sessionName, String key) {
        String value = "";
        SharedPreferences prefs = mContext.getSharedPreferences(sessionName, MODE_PRIVATE);
        value = prefs.getString(key, "");
        return value;
    }

    public void addIntValueToSession(Context mContext, String sessionName, String key, int value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(sessionName, MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntValueFromSessionByKey(Context mContext, String sessionName, String key) {
        int value = 0;
        SharedPreferences prefs = mContext.getSharedPreferences(sessionName, MODE_PRIVATE);
        value = prefs.getInt(key, 0);
        return value;
    }

    public int isLogged(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(AppConstants.USER_SESSION_NAME, MODE_PRIVATE);
        return prefs.getInt(AppConstants.USER_ID_KEY, 0);
    }

    public void logoutSession(Context mContext, String sessionName) {
        SharedPreferences prefs = mContext.getSharedPreferences(sessionName, MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public void saveFCMToken(Context mContext,String token){
        SharedPreferences.Editor editor = mContext.getSharedPreferences("FCM", MODE_PRIVATE).edit();
        editor.putString(AppConstants.DEVICE_ID_KEY, token.trim());
        editor.apply();
    }


}
