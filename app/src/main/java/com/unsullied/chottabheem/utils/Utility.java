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
      /*  InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        Log.w(AppConstants.tag, "Called hideKayboard");
        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null) {
            Log.w(AppConstants.tag, "Called hideKayboard IF");
            return;
        }
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/

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
