package com.unsullied.chottabheem.utils;

import android.Manifest;
import android.util.Base64;

import com.unsullied.chottabheem.BuildConfig;

import java.io.UnsupportedEncodingException;

public class AppConstants {

    public static final String APP_NAME = "Chotta bheem";

    public static final boolean DEBUG = true;
    public static final String TAG = "Chotta Bheem";

    public static final String LIVE_URL = "http://api.rechapi.com/";
    public static final String FORMAT_KEY = "format=";
    public static final String FORMAT_JSON_VALUE = "json";
    public static final String TOKEN_KEY = "&token=";
    public static final String TOKEN_VALUE = "vQmnbEmhg8JfwT7mVmwycrRw9BXg4S";
    public static final String MOBILE_KEY = "&mobile=";
    public static final String RES_CODE_KEY = "error_code";//error code from rechapi server
    public static final int RES_CODE_VALUE = 200;
    public static final String RES_TEXT_KEY = "resText";
    public static final String RESPONSE_JSON_OBJECT_KEY = "data";
    public static final String RES_TEXT_SUCCESS_VALUE = "SUCCESS";
    public static final String RES_TEXT_PENDING_VALUE = "PENDING";
    public static final String RES_TEXT_FAILED_VALUE = "FAILED";
    public static final String JSON_CIRCLE_ID_KEY = "circleId";
    public static final String JSON_OPERATOR_ID_KEY = "opId";
    public static final String JSON_SERVICE_KEY = "service";//Operator name
    public static final String JSON_LOCATION_KEY = "location";//Service provider locaion Like Tamil Nadu, Kerala...
    public static final String AMOUNT_KEY = "&amount=";
    public static final String OPERATOR_ID_KEY = "&opid=";
    public static final String UNIQUE_ID_KEY = "&urid=";
    public static final String BILL_AMOUNR_LEY = "billAmount"; //In case of electrity and gas bill payment if you had provide wroung bill amount it will show you correct bill amount.
    public static final String RECHARGE_STATUS_KEY = "status"; //Recharge status (SUCCESS/PENDING/FAILED)
    public static final String OPERATOR_TRANSACTION_ID_KEY = "optid";//Operator transaction id if recharge is success
    public static final String ORDER_ID_KEY = "Orderid"; //RechApi unique recharge order id , multiple orderid supported like 1234,3434,1234 etc but supported for dispute.
    public static final String OPIONAL_VALUE1_KEY = "&opvalue1=";
    public static final String OPIONAL_VALUE2_KEY = "&opvalue2=";
    public static final String TYPE_KEY = "&type="; //Mobile Recharge Type . Possible values  2G , 3G , FTT , LSC , OTR , RMG , SMS , TUP
    public static final String CIRCLE_CODE = "&circleCode=";


    /* API Name */
    public static final String RECHARGE_API = "recharge.php?";//Failed Response::: {"data":{"orderId":86591857,"status":"FAILED","mobile":"9790205451","amount":"50","operatorId":0,"error_code":143,"service":0,"bal":"0.0000","creditUsed":null,"resText":"Invalid opvalue 1","billAmount":null,"billName":null}}
    public static final String OPERATOR_CHECK_API = "mob_details.php?";
    public static final String RECHARGE_PLAN_API = "rech_plan.php?";


    /*Pay Bill Activity*/
    public static final String TITLE_INTENT_KEY = "IntentTitle";
    public static final String HINT_INTENT_KEY = "IntentHint";
    public static final String ICON_INTENT_KEY = "IntentIcon";

    public static final int OPERATOR_INTENT_REQUEST_CODE = 10001;

    public static final String FB_ID_KEY = "fbId";
    public static final String USER_NAME_KEY = "UserName";
    public static final String USER_EMAIL_ID_KEY = "EmailID";
    public static final String USER_MOBILE_KEY = "MobileNumber";
    public static final String USER_SESSION_NAME="userSession";

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
