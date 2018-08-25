package com.unsullied.chottabheem.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {

    public void printLogcat(String message) {
        if (AppConstants.DEBUG)
            Log.w(AppConstants.TAG, message);
    }

    public void hideKeyboard(Activity activity, View view) {
        printLogcat("Called hideKeyboard");
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) {
            printLogcat("Called hideKayboard IF");
            v = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
