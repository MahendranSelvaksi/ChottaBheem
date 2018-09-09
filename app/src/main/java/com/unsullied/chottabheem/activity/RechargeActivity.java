package com.unsullied.chottabheem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.AppController;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.SmileyRemover;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.paymentgateway.AppEnvironment;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout, amountLayout;
    private CustomEditText operatorET, mobileNumberET, amountET;
    private Button operatorSelectBtn, contactBtn;
    private CustomTextView browsePlansTV, rechargeBtn, rechargeTitleTV;
    private TextInputLayout mobileNumberTIL;
    private ImageView rechargeIconIV;

    private String intentTitleStr, intentHintStr;
    private int pageIcon;
    private Utility myUtility;

    private int selectedCircleId = 0, selectedOperatorId = 0, rechargeAmount = 0;
    private String selectedLocation, selectedServiceProvider, selectedMobileNumber;
    private boolean selectedCircleIdFromApi = false;
    private SmileyRemover smileyRemover;
    private Activity mActivity;
    private Context mContext;

    private AppPreference mAppPreference;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private String nameStr, emailIdStr;
    private SessionManager mSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        mActivity = this;
        mContext = getApplicationContext();
        myUtility = new Utility();
        smileyRemover = new SmileyRemover();
        mAppPreference = new AppPreference();
        mSessionManager = new SessionManager();
        intentTitleStr = getIntent().getStringExtra(AppConstants.TITLE_INTENT_KEY);
        intentHintStr = getIntent().getStringExtra(AppConstants.HINT_INTENT_KEY);
        pageIcon = getIntent().getIntExtra(AppConstants.ICON_INTENT_KEY, 0);

        toolbar = (Toolbar) findViewById(R.id.rechargeToolbar);
        tittleTV = toolbar.findViewById(R.id.toolbar_title);

        tittleTV.setText(intentTitleStr);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        operatorLayout = findViewById(R.id.operatorLayout);
        numberLayout = findViewById(R.id.numberLayout);
        amountLayout = findViewById(R.id.amountLayout);
        mobileNumberTIL = findViewById(R.id.mobileNumberTIL);
        operatorET = findViewById(R.id.operatorET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
        amountET = findViewById(R.id.amountET);
        operatorSelectBtn = findViewById(R.id.operatorSelectBtn);
        contactBtn = findViewById(R.id.contactBtn);
        rechargeBtn = findViewById(R.id.rechargeBtn);
        rechargeTitleTV = findViewById(R.id.rechargeTitleTV);
        browsePlansTV = findViewById(R.id.browsePlansTV);
        rechargeIconIV = findViewById(R.id.rechargeIconIV);

        myUtility.printLogcat("Image ::::" + pageIcon);
        setHintForEditText(intentHintStr);
        rechargeTitleTV.setText(intentTitleStr.trim());
        rechargeIconIV.setImageResource(pageIcon);

        operatorET.setFocusableInTouchMode(false);

        nameStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY);
        emailIdStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY);

        //mobileNumberET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myUtility.printLogcat("START:::" + start);
                myUtility.printLogcat("BEFORE:::" + before);
                myUtility.printLogcat("COUNT::::" + count);
                myUtility.printLogcat("LENGTH::::" + s.length());

                if (s.length() == 4 && !selectedCircleIdFromApi) {
                    getOperatorBackground(s.toString());
                } else if (s.length() < 4) {
                    selectedCircleIdFromApi = false;
                } else {
                    selectedMobileNumber = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (s.length() > 0) {
                    rechargeAmount = Integer.parseInt(s.toString());
                }*/
            }
        });

        operatorET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    myUtility.hideKeyboard(mActivity, operatorET);
                }
            }
        });

        operatorET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                myUtility.printLogcat("event::" + event.getAction());
                myUtility.printLogcat("Action Id:::" + actionId);

                return false;
            }
        });

        rechargeBtn.setOnClickListener(this);
        browsePlansTV.setOnClickListener(this);
        operatorET.setOnClickListener(this);

    }


    private void setHintForEditText(String hint) {
        //mobileNumberET.setHint(hint.trim());
        mobileNumberTIL.setHint(hint);
    }


    private void getOperatorBackground(String mobileNumber) {
        if (ConnectivityReceiver.isConnected()) {
            String url = AppConstants.LIVE_URL + AppConstants.OPERATOR_CHECK_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                    AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + mobileNumber;
            myUtility.printLogcat("API NAME::::" + url);
            AppController.getInstance().clearAllQueue();
            if (ConnectivityReceiver.isConnected()) {
                ANRequest request = AndroidNetworking.get(url)
                        .setTag(AppConstants.APP_NAME)
                        .setPriority(Priority.HIGH)
                        .build();

                request.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            myUtility.printLogcat("RESPONSE:::" + response.toString());
                            if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                                JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                                if (responseJSON.getInt(AppConstants.RES_CODE_KEY) == AppConstants.RES_CODE_VALUE) {
                                    selectedCircleIdFromApi = true;
                                    selectedCircleId = Integer.parseInt(responseJSON.getString(AppConstants.JSON_CIRCLE_ID_KEY).trim());
                                    selectedOperatorId = Integer.parseInt(responseJSON.getString(AppConstants.JSON_OPERATOR_ID_KEY).trim());
                                    selectedLocation = responseJSON.getString(AppConstants.JSON_LOCATION_KEY).trim();
                                    selectedServiceProvider = responseJSON.getString(AppConstants.JSON_SERVICE_KEY);
                                    updateServiceProviderUI();
                                } else {
                                    selectedCircleIdFromApi = false;
                                    Toast.makeText(RechargeActivity.this, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
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
            }
        } else {
            Toast.makeText(this, "" + AppConstants.NO_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateServiceProviderUI() {
        operatorET.setText(selectedServiceProvider.concat("(" + selectedLocation + ")"));
    }

    @Override
    public void onClick(View v) {
        if (v == rechargeBtn) {
            selectedMobileNumber = mobileNumberET.getText().toString().trim();
            rechargeAmount = amountET.getText().toString().trim().length() > 0 ? Integer.parseInt(amountET.getText().toString().trim()) : 0;
            if (selectedCircleId == 0 || selectedOperatorId == 0 || rechargeAmount == 0 || selectedMobileNumber.length() == 0) {
                if (selectedCircleId == 0 && selectedOperatorId == 0 && rechargeAmount == 0 && selectedMobileNumber.length() == 0) {
                    Toast.makeText(this, "Please give " + intentHintStr, Toast.LENGTH_SHORT).show();
                } else if (selectedOperatorId == 0 || selectedCircleId == 0) {
                    Toast.makeText(this, "Please choose operator..", Toast.LENGTH_SHORT).show();
                } else if (rechargeAmount == 0) {
                    Toast.makeText(this, "Please enter recharge amount...", Toast.LENGTH_SHORT).show();
                }
            } else {
                launchPayUMoneyFlow(String.valueOf(rechargeAmount));
            }
        } else if (v == browsePlansTV) {

        } else if (v == operatorET) {
            myUtility.hideKeyboard(mActivity, operatorET);
        }
    }


    private void callRechargeAPI(String url) {
        if (ConnectivityReceiver.isConnected()) {
            ANRequest request = AndroidNetworking.get(url)
                    .setTag(AppConstants.APP_NAME)
                    .setPriority(Priority.HIGH)
                    .build();

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.has(AppConstants.RESPONSE_JSON_OBJECT_KEY)) {
                            JSONObject responseJSON = response.getJSONObject(AppConstants.RESPONSE_JSON_OBJECT_KEY);
                            if (responseJSON.getInt(AppConstants.RES_CODE_KEY) == AppConstants.RES_CODE_VALUE) {

                            } else {
                                selectedCircleIdFromApi = false;
                                Toast.makeText(RechargeActivity.this, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "" + AppConstants.NO_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow(String rechargeAmount) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Pay now");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Subscription");

        payUmoneyConfig.disableExitConfirmation(true);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(rechargeAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = selectedMobileNumber;
        String productName = mAppPreference.getProductInfo();
        String firstName = mAppPreference.getFirstName();
        String email = emailIdStr;
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

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
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

            /*
             * Hash should always be generated from your server side.
             * */
            generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
           /* mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   payNowButton.setEnabled(true);
        }
    }


    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
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
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                new AlertDialog.Builder(mActivity)
                        .setCancelable(false)
                        .setTitle("Payment Status")
                        .setMessage("Successfully Added payment....")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                long time = System.currentTimeMillis();
                                try {
                                    String url = AppConstants.LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                                            AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + selectedMobileNumber +
                                            AppConstants.AMOUNT_KEY + rechargeAmount + AppConstants.OPERATOR_ID_KEY + selectedOperatorId +
                                            AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + URLEncoder.encode(intentTitleStr, "utf-8") +
                                            AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");
                                    myUtility.printLogcat("API::::" + url);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                myUtility.printLogcat("Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                myUtility.printLogcat("Both objects are null!");
            }
        }
    }

    /**
     * This AsyncTask generates hash from server.
     */
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
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
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
                Toast.makeText(mActivity, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }
            }
        }
    }
}
