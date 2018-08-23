package com.unsullied.chottabheem.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;




public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    View parentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout = findViewById(android.R.id.content);
        showFirstSnack(ConnectivityReceiver.isConnected());
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        Log.w("Success", "Called Show snack");
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    private void showFirstSnack(boolean isConnected) {
        Log.w("Success", "Called Show snackgfdgfg");
        if (!isConnected) {
            String message = "Sorry! Not connected to internet";
            int color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(parentLayout, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
