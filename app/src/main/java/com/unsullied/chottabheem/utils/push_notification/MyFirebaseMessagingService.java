package com.unsullied.chottabheem.utils.push_notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 100;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msg =remoteMessage.getData().get("message");
        String title=remoteMessage.getData().get("title");

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("Success_Native", "key, " + key + " value " + value);
        }

        if (!remoteMessage.getData().isEmpty()) {
            for (String key : remoteMessage.getData().keySet()) {
                String value = remoteMessage.getData().get(key);
                Log.d("Success", "Key: " + key + " Value: " + value);
            }
        }

        Log.d("Success","Notification Message:::: "+remoteMessage.getData().keySet());
        Log.d("Success","Notification Title:::: "+title);

       // sendNotification(msg);
    }

  /*  private void sendNotification(String msg) {
        //int C3NOTIFICATION_ID = 100;
       // Log.d("Check", "Comes C3 login");
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent myintent = new Intent(this, CalendarActivity.class);
        myintent.putExtra("C3_login", getString(R.string.app_name));
        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        try{
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.reservado)
                            .setContentTitle(getString(R.string.app_name))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.reservado))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(msg);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            NOTIFICATION_ID++;
        }catch (Exception e){
            Log.e("Error","EEE"+e);
        }
    }*/
}
