package com.unsullied.chottabheem.utils.push_notification;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unsullied.chottabheem.utils.SessionManager;

import java.net.URLEncoder;


public class RegistrationIntentService extends FirebaseInstanceIdService {

    public static final String SESSION_NAME = "SESSION";
    public static final String SESSION_PLAYSTORE_KEY = "play_store";
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    String regId_str;
   /* RequestQueue queue;
    ConnectionManager networkCheck;*/
    SharedPreferences session;


    public RegistrationIntentService() {
       // super(TAG);
       // parser=new JsonParser(this);
        Log.w("Success","Comes RegistrationIntentService");

    }


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        regId_str = FirebaseInstanceId.getInstance().getToken();
        Log.w(TAG, "Refreshed token: " + regId_str);
        new SessionManager().saveFCMToken(getApplicationContext(),regId_str);
        /*networkCheck = new ConnectionManager(this);
        session=this.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        queue = Appcontroller.getInstance(getApplicationContext()).getRequestQueue();
        sendRegistrationToServer(regId_str);*/
    }
/*@Override
    protected void onHandleIntent(Intent intent) {
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]

            InstanceID instanceID = InstanceID.getInstance(this);
            regId_str = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + regId_str);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(regId_str);

            // Subscribe to topic channels
            //subscribeTopics(regId_str);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
           // sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
           // sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }*/

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        try {
            String url="http://www.petalskart.com/Mehendi/mobile/User/settings?devtoken="+urlEncoder(token);
          //  updateRegID(url);
            /*telephonyManager  =
                    (TelephonyManager)getSystemService( Context.TELEPHONY_SERVICE );
            uniqueID=telephonyManager.getDeviceId();*/
           /* uniqueID= Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            prefs=getSharedPreferences(getResources().getString(R.string.settingSession), Context.MODE_PRIVATE);
            editor=prefs.edit();

          // url=getResources().getString(R.string.live_url)+"device/create?device_id="+uniqueID+"&reg_id="+token+"&device_token=&os_type=android&language=english";

            Log.d("err","err "+url);
            parser.getJsonString(url);
            String notificationalert=prefs.getString(getApplicationContext().getResources().getString(R.string.notificationSetting),"null");
            String language=prefs.getString(getApplicationContext().getResources().getString(R.string.languageSetting),"null");
            if (language.equals(getString(R.string.englishStr))|| language.equals("null")||language.equals("")){
                language=getString(R.string.englishStr);
            }else if (language.equals(getResources().getString(R.string.arabicStr))){
                language=getString(R.string.arabicStr);
            }
            Log.d("Success","Lannnnn"+language);
            editor.putString(getString(R.string.device_id_str),uniqueID);
            editor.putString(getResources().getString(R.string.languageSetting),language);
            editor.apply();
            Log.d("Success","language "+language);
            String url=getString(R.string.live_url)+"device/settingsupdate?device_id="+uniqueID+"&reg_id="+token+"&language="+language+"&notification_status="+notificationalert+"&os_type=android";
            Log.d("Success","Reg "+url);
            parser.getJsonString(url);*/
            /*String updateUrl=getString(R.string.live_url);
            updateRegID(updateUrl);*/
        }catch (Exception ex){
            Log.d("Exception",""+ex);
        }
    }

    /*public void updateRegID(String url){
        if (networkCheck.isNetworkAvailable()){
            Log.d("Success","URL::: "+url);
            JsonObjectRequest updateRequest=new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                Log.d("Success","Response::: "+response);
                    try {
                        JSONArray jsonArray=response.getJSONArray("settings");
                        SharedPreferences.Editor editor = getSharedPreferences(SESSION_NAME, MODE_PRIVATE).edit();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            editor.putString(SESSION_PLAYSTORE_KEY,object.getString("playstore"));
                            editor.apply();
                        }
                    }catch (Exception ex){
                        Log.e("Exception",""+ex.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        //    showHall(url);
                       // Toast.makeText(getApplicationContext(), R.string.timeoutError, Toast.LENGTH_SHORT).show();
                        Log.e("Error",""+error);
                    } else if (error instanceof AuthFailureError) {
                     //   Toast.makeText(getApplicationContext(), R.string.authenticationError, Toast.LENGTH_SHORT).show();
                        Log.e("Error",""+error);
                    } else if (error instanceof ServerError) {
                      //  Toast.makeText(getApplicationContext(), R.string.serverError, Toast.LENGTH_SHORT).show();
                        Log.e("Error",""+error);
                    } else if (error instanceof NetworkError) {
                        //      showHall(url);
                       // Toast.makeText(getApplicationContext(), R.string.networkError, Toast.LENGTH_SHORT).show();
                        Log.e("Error",""+error);
                    } else if (error instanceof ParseError) {
                       // Toast.makeText(getApplicationContext(), R.string.parseError, Toast.LENGTH_SHORT).show();
                        Log.e("Error",""+error);
                    }
                }
            });
            int socketTimeout = 40000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            updateRequest.setRetryPolicy(policy);
            queue.add(updateRequest);
        }else {
            Log.e("Error","No network connection");
        }

    }*/

    public String urlEncoder(String data) {
        try {
            String encodedString = URLEncoder.encode(data, "UTF-8");
            return encodedString;
        } catch (Exception e) {

            Log.e("Exception", "" + e);
        }
        return null;
    }
}
