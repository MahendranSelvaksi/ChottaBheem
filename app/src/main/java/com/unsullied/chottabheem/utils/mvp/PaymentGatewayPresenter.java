package com.unsullied.chottabheem.utils.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.unsullied.chottabheem.BuildConfig;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaymentGatewayPresenter implements PaymentGatewayMVP.Presenter {

    AppPreference mAppPreference;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private Context mContext;
    private Activity mActivity;
    private Utility myUtility;
    private PaymentGatewayMVP.View mView;

    public PaymentGatewayPresenter(Context mContext, Activity mActivity, PaymentGatewayMVP.View mView) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mView = mView;
        mAppPreference = new AppPreference();
        myUtility = new Utility();
    }

    /*@Override
    public void launchPayUMoneyFlow(String payableAmount, String mobileNumber, String emailId) {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Pay now");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Subscription");

        payUmoneyConfig.disableExitConfirmation(true);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(payableAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = mobileNumber;
        String productName = mAppPreference.getProductInfo();
        String firstName = mAppPreference.getFirstName();
        String email = emailId;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppController.getInstance().getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            *//*
     * Hash should always be generated from your server side.
     * *//*
           // generateHashFromServer(mPaymentParams);

            *//*            *//**/

    /**
     * Do not use below code when going live
     * Below code is provided to generate hash from sdk.
     * It is recommended to generate hash from server side only.
     *//*
     *//* mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*//*

        } catch (Exception e) {
            // some exception occurred
            //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   payNowButton.setEnabled(true);
        }

        *//*"versioncode:1.0
accountid:subscibe
mobile_number:8105568432
login_type:accountkitlogin
device_id:
osname:Android
device_type:Android
payable_amount:99
product_info:Subscription
customer_name:Prashanth
customer_email:test@test.com"*//*
    }*/
    @Override
    public void generateHashFromServer(String mobileNumber, String accountId, String name, String email, String payableAmount, String productInfo, String buttonText,
                                       String pageTitle) {
        if (ConnectivityReceiver.isConnected()) {
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceType = Build.DEVICE;
            String loginInfo = AppConstants.APP_USER_NAME_VALUE + ":" + AppConstants.APP_PASSWORD_VALUE;
            byte[] encodingByte = Base64.encode(loginInfo.getBytes(), Base64.NO_WRAP);
            String encoding = new String(encodingByte);
            ANRequest request = AndroidNetworking.post(AppConstants.API_LIVE_URL + AppConstants.GENERATE_CHECKSUM_API)
                    .addHeaders(AppConstants.HEADER_API_KEY, AppConstants.HEADER_API_KEY_VALUE)
                    .addHeaders("Authorization", "Basic " + encoding)
                    .addBodyParameter(AppConstants.VERSIONCODE_KEY, BuildConfig.VERSION_NAME)
                    .addBodyParameter(AppConstants.ACCOUNT_ID_KEY, accountId)
                    .addBodyParameter("mobile_number", mobileNumber)
                    .addBodyParameter("login_type", "accountkitlogin")
                    .addBodyParameter(AppConstants.DEVICE_ID_KEY,new SessionManager().getValueFromSessionByKey(mContext,"FCM",AppConstants.DEVICE_ID_KEY))
                    .addBodyParameter(AppConstants.DEVICE_TYPE_KEY, deviceType)
                    .addBodyParameter(AppConstants.OS_NAME_KEY, AppConstants.OS_NAME_VALUE)
                    .addBodyParameter("payable_amount", payableAmount)
                    .addBodyParameter("product_info", productInfo)
                    .addBodyParameter("customer_name", name)
                    .addBodyParameter("customer_email", email)
                    .setTag(AppConstants.APP_NAME)
                    .setPriority(Priority.HIGH)
                    .build();

            myUtility.printLogcat("Version code:::" + BuildConfig.VERSION_NAME);
            myUtility.printLogcat("Mobile number:::" + mobileNumber);
            myUtility.printLogcat("Device id:::" + deviceId);
            myUtility.printLogcat("Device type:::" + deviceType);
            myUtility.printLogcat("Payable Amount:::" + payableAmount);
            myUtility.printLogcat("Product info:::" + productInfo);
            myUtility.printLogcat("Customer name:::" + name);
            myUtility.printLogcat("Customer email:::" + email);
            myUtility.printLogcat("account id:::" + accountId);
            myUtility.printLogcat("URL:::" + AppConstants.API_LIVE_URL + AppConstants.GENERATE_CHECKSUM_API);


            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Response::::" + response);

                        if (response.getInt(AppConstants.API_STATUS_CODE_KEY) == AppConstants.RES_CODE_VALUE) {
                            if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                                JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

                                //Use this to set your custom text on result screen button
                                payUmoneyConfig.setDoneButtonText(buttonText);

                                //Use this to set your custom title for the activity
                                payUmoneyConfig.setPayUmoneyActivityTitle(pageTitle);

                                payUmoneyConfig.disableExitConfirmation(true);

                                PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

                                double amount = 0;
                                try {
                                    amount = Double.parseDouble(payableAmount);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String udf1 = "";
                                String udf2 = "";
                                String udf3 = "";
                                String udf4 = "";
                                String udf5 = "";
                                String udf6 = "";
                                String udf7 = "";
                                String udf8 = "";
                                String udf9 = "";
                                String udf10 = "";

                                builder.setAmount(amount)
                                        .setTxnId(responseJSON.getString("tid"))
                                        .setPhone(mobileNumber)
                                        .setProductName(productInfo)
                                        .setFirstName(name)
                                        .setEmail(email)
                                        .setsUrl(responseJSON.getString("callback_url"))
                                        .setfUrl(responseJSON.getString("callback_url"))
                                        .setUdf1(udf1)
                                        .setUdf2(udf2)
                                        .setUdf3(udf3)
                                        .setUdf4(udf4)
                                        .setUdf5(udf5)
                                        .setUdf6(udf6)
                                        .setUdf7(udf7)
                                        .setUdf8(udf8)
                                        .setUdf9(udf9)
                                        .setUdf10(udf10)
                                        .setIsDebug(responseJSON.getBoolean("mode"))
                                        .setKey(responseJSON.getString("mkey"))
                                        .setMerchantId(responseJSON.getString("mid"));

                                try {
                                    mPaymentParams = builder.build();
                                   /* String hashSequence = responseJSON.getString("mkey")+"|"+ responseJSON.getString("tid") +"|"+ amount +"|"+ productInfo +"|"+ name +"|"+ email+" |"+ udf1 +"|"+ udf2 +"|"+ udf3 +"|"+ udf4 +"|"+ udf5 +"||||||"+responseJSON.getString("salt_key");
                                    mPaymentParams.setMerchantHash(hashCal("SHA-512", hashSequence));*/
                                    mPaymentParams.setMerchantHash(responseJSON.getString("hash"));
                                    mView.getSuccessfulHash(mPaymentParams);

               /* if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }*/
                                    //   mView.getSuccessfulHash(mPaymentParams);
                                   // String hashSequence = responseJSON.getString("mkey")+"|"+ responseJSON.getString("tid") +"|"+ amount +"|"+ productInfo +"|"+ name +"|"+ email+" |"+ udf1 +"|"+ udf2 +"|"+ udf3 +"|"+ udf4 +"|"+ udf5 +"||||||"+name;
                                 //   String serverCalculatedHash = hashCal("SHA-512", hashSequence);
             /*                                   *
                                     * Do not use below code when going live
                                     * Below code is provided to generate hash from sdk.
                                     * It is recommended to generate hash from server side only.
                                     *
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

                                } catch (Exception e) {
                                    // some exception occurred
                                    //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    //   payNowButton.setEnabled(true);
                                }

                            } else {
                                //     selectedCircleIdFromApi = false;
                                //     Toast.makeText(RechargeActivity.this, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError error) {
                    if (error.getErrorCode() != 0) {
                        // received error from server
                        // error.getErrorCode() - the error code from server
                        // error.getErrorBody() - the error body from server
                        // error.getErrorDetail() - just an error detail
                        myUtility.printLogcat("onError errorCode : " + error.getErrorCode());
                        myUtility.printLogcat("onError errorBody : " + error.getErrorBody());
                        myUtility.printLogcat("onError errorDetail : " + error.getErrorDetail());
                        // get parsed error object (If ApiError is your class)

                    } else {
                        // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                        myUtility.printLogcat("onError errorDetail : " + error.getErrorDetail());

                    }
                    error.printStackTrace();
                }

            });

            /*request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        myUtility.printLogcat("Login API Response:::" + response.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    myUtility.printLogcat("ErrorCode:::" + anError.getErrorCode());
                    myUtility.printLogcat("ErrorMessage:::" + anError.getErrorDetail());
                    myUtility.printLogcat("ErrorBody:::" + anError.getErrorBody());
                  //  mView.showError(anError.getErrorCode(), anError.getErrorDetail());
                }
            });
        } else {
            //  Toast.makeText(this, "" + AppConstants.NO_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }*/
        }


        /**
         * This method generates hash from server.
         *
         * @param// paymentParam payments params used for hash generation
         */
   /* private void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }*/
    /*private String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }*/


        /**
         * This AsyncTask generates hash from server.
         */
   /* @SuppressLint("StaticFieldLeak")
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        *//**
         * This hash is mandatory and needs to be generated from merchant's server side
         *
         *//*
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            // payNowButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                // Toast.makeText(mActivity, "Could not generate hash", Toast.LENGTH_SHORT).show();
                mView.showError("Could not generate hash");
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                *//*if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }*//*
                mView.getSuccessfulHash(mPaymentParams);
            }
        }
    }*/


    }
    public String hashCal(String type, String hashString){
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }
}
