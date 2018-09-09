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

    public boolean isLogged(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(AppConstants.USER_SESSION_NAME, MODE_PRIVATE);
        return prefs.getString(AppConstants.FB_ID_KEY, "").length() > 0;
    }

    public void logoutSession(Context mContext, String sessionName) {
        SharedPreferences prefs = mContext.getSharedPreferences(sessionName, MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}
