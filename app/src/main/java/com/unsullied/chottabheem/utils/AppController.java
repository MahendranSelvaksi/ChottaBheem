package com.unsullied.chottabheem.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.internal.ANRequestQueue;
import com.facebook.accountkit.AccountKit;
import com.unsullied.chottabheem.utils.paymentgateway.AppEnvironment;
import com.unsullied.chottabheem.utils.push_notification.RegistrationIntentService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class AppController extends Application {

    private static AppController mInstance;
    AppEnvironment appEnvironment;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appEnvironment = AppEnvironment.SANDBOX;
        AccountKit.initialize(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(mInstance, okHttpClient);
        Intent idIntent = new Intent(this, RegistrationIntentService.class);
        startService(idIntent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void clearQueue(String tag) {
        ANRequestQueue.getInstance().cancelRequestWithGivenTag(tag, false);
        Log.w("Success", "" + tag + " Queue Cleared");
    }

    public void clearAllQueue() {
        ANRequestQueue.getInstance().cancelAll(false);
        Log.w("Success", "All Queue Cleared");
    }

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

}
