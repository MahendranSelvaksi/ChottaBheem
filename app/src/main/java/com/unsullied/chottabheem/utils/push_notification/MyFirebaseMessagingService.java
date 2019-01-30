package com.unsullied.chottabheem.utils.push_notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.activity.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 100;
String TAG="Success";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msg =remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();


        /*for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.w("Success_Native", "key, " + key + " value " + value);
        }*/

        if (!remoteMessage.getData().isEmpty()) {
            for (String key : remoteMessage.getData().keySet()) {
                String value = remoteMessage.getData().get(key);
                Log.d("Success", "Key: " + key + " Value: " + value);
            }
        }

        Log.d("Success", "From: " + remoteMessage.getFrom());
        Log.d("Success", "title: " + title);
        Log.d("Success", "Notification Message Body: " + msg);

       sendNotification(msg,title);
    }

    private void sendNotification(String msg,String title) {
        //int C3NOTIFICATION_ID = 100;
       // Log.d("Check", "Comes C3 login");
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent myintent = new Intent(this, MainActivity.class);
       // myintent.putExtra("C3_login", getString(R.string.app_name));
        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        try{
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(msg);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            NOTIFICATION_ID++;
        }catch (Exception e){
            Log.e("Error","EEE"+e);
        }
    }
}
