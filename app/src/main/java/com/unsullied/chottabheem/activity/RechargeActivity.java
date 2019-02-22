package com.unsullied.chottabheem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
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
import com.crashlytics.android.Crashlytics;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.razorpay.PaymentResultListener;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.AppController;
import com.unsullied.chottabheem.utils.AppPermissions;
import com.unsullied.chottabheem.utils.ConnectivityReceiver;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.SmileyRemover;
import com.unsullied.chottabheem.utils.SymbolsRemover;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayMVP;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayPresenter;
import com.unsullied.chottabheem.utils.mvp.RechargeMVP;
import com.unsullied.chottabheem.utils.mvp.RechargePresenter;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.fabric.sdk.android.Fabric;

import static com.unsullied.chottabheem.utils.AppConstants.PICK_CONTACT;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener, RechargeMVP.RechargeView, PaymentGatewayMVP.View, PaymentResultListener {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout, amountLayout;
    private CustomEditText operatorET, mobileNumberET, amountET;
    private Button operatorSelectBtn, contactBtn;
    private CustomTextView browsePlansTV, rechargeBtn, rechargeTitleTV;
    private TextInputLayout mobileNumberTIL;
    private ImageView rechargeIconIV;

    private String intentTitleStr, intentHintStr, intentSelectStr, cNumber, optionValue1 = "", optionValue2 = "", optionValue3 = "", optionValue4 = "";
    private int pageIcon;
    private Utility myUtility;

    private int selectedCircleId = 0, selectedOperatorId = 0, rechargeAmount = 0;
    private String selectedLocation, selectedServiceProvider, selectedMobileNumber;
    private boolean selectedCircleIdFromApi = false;
    private SmileyRemover smileyRemover;
    private SymbolsRemover symbolsRemover;
    private Activity mActivity;
    private Context mContext;

    private AppPreference mAppPreference;
    //private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private String nameStr, emailIdStr;
    private SessionManager mSessionManager;
    private AppPermissions mRuntimePermission;
    private RechargePresenter mRechargePresenter;
    private PaymentGatewayPresenter mPaymentGatewayPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_recharge);

        mActivity = this;
        mContext = getApplicationContext();

        myUtility = new Utility();
        smileyRemover = new SmileyRemover();
        symbolsRemover = new SymbolsRemover();
        mAppPreference = new AppPreference();
        mSessionManager = new SessionManager();
        mRechargePresenter = new RechargePresenter(mContext, this);
        mRuntimePermission = new AppPermissions(mActivity);
        mPaymentGatewayPresenter = new PaymentGatewayPresenter(mContext, mActivity, this);

        intentTitleStr = getIntent().getStringExtra(AppConstants.TITLE_INTENT_KEY);
        intentHintStr = getIntent().getStringExtra(AppConstants.HINT_INTENT_KEY);
        pageIcon = getIntent().getIntExtra(AppConstants.ICON_INTENT_KEY, 0);
        intentSelectStr = getIntent().getStringExtra(AppConstants.SELECTED_INTENT_KEY);

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

        mobileNumberET.setFilters(new InputFilter[]{smileyRemover, symbolsRemover});
        operatorET.setFilters(new InputFilter[]{smileyRemover, symbolsRemover});
        amountET.setFilters(new InputFilter[]{smileyRemover, symbolsRemover});

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
        contactBtn.setVisibility(intentSelectStr.equalsIgnoreCase(AppConstants.PREPAID_VALIE) ? View.VISIBLE : View.GONE);
        rechargeBtn.setOnClickListener(this);
        browsePlansTV.setOnClickListener(this);
        operatorET.setOnClickListener(this);
        operatorSelectBtn.setOnClickListener(this);
        operatorLayout.setOnClickListener(this);
        contactBtn.setOnClickListener(this);

    }


    private void setHintForEditText(String hint) {
        //mobileNumberET.setHint(hint.trim());
        mobileNumberTIL.setHint(hint);
    }


    private void getOperatorBackground(String mobileNumber) {
        if (ConnectivityReceiver.isConnected()) {
            String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.OPERATOR_CHECK_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
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
                // mPaymentGatewayPresenter.launchPayUMoneyFlow(String.valueOf(rechargeAmount), selectedMobileNumber, emailIdStr);
               /* mPaymentGatewayPresenter.generateHashFromServer(selectedMobileNumber,
                        mSessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.FB_ID_KEY),
                        nameStr,emailIdStr, String.valueOf(rechargeAmount),
                        "Recharge","Pay Now","Recharge");*/
                long time = System.currentTimeMillis();
                try {
                    String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                            AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + selectedMobileNumber +
                            AppConstants.AMOUNT_KEY + rechargeAmount + AppConstants.OPERATOR_ID_KEY + selectedOperatorId +
                            AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + "Recharge" +
                            AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");
                    myUtility.printLogcat("API::::" + url);

                    mRechargePresenter.callRechargeAPI(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == browsePlansTV) {
            if (selectedOperatorId > 0) {
                Intent browsePlansIntent = new Intent(mActivity, BrowsePlansActivity.class);
                browsePlansIntent.putExtra(AppConstants.JSON_OPERATOR_ID_KEY, selectedOperatorId);
                browsePlansIntent.putExtra(AppConstants.USER_MOBILE_KEY, selectedMobileNumber);
                browsePlansIntent.putExtra(AppConstants.JSON_CIRCLE_ID_KEY, selectedCircleId);
                browsePlansIntent.putExtra(AppConstants.USER_EMAIL_ID_KEY, emailIdStr);
                startActivity(browsePlansIntent);
            } else {
                Toast.makeText(this, "Please choose operator..", Toast.LENGTH_SHORT).show();
            }
        } else if (v == operatorET || v == operatorLayout || v == operatorSelectBtn) {
            myUtility.hideKeyboard(mActivity, operatorET);
            Intent operatorsIntent = new Intent(mActivity, SelectOperatorActivity.class);
            operatorsIntent.putExtra(AppConstants.SELECTED_INTENT_KEY, intentSelectStr);
            startActivityForResult(operatorsIntent, AppConstants.OPERATOR_INTENT_REQUEST_CODE);
        } else if (v == contactBtn) {
            if (mRuntimePermission.hasPermission(AppConstants.READ_CONTACTS_PERMISSIONS)) {
                callContactIntent();
            } else {
                mRuntimePermission.requestPermission(AppConstants.READ_CONTACTS_PERMISSIONS, AppConstants.READ_CONTACTS_REQUEST_CODE);
            }
        }
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

                                mPaymentGatewayPresenter.startPayment(mContext,mActivity,"Recharge",String.valueOf(rechargeAmount),
                                        mSessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.USER_EMAIL_ID_KEY),
                                        mSessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.USER_MOBILE_KEY));
                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                myUtility.printLogcat("Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                myUtility.printLogcat("Both objects are null!");
            }
        } else if (requestCode == AppConstants.PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {


                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        cNumber = phones.getString(phones.getColumnIndex("data1"));
                        cNumber = cNumber.replaceAll(" ", "");
                        System.out.println("number is:" + cNumber);
                        phoneNumberWithOutCountryCode(cNumber);
                    }
                    // String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                }
            }
        } else if (requestCode == AppConstants.OPERATOR_INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    intentHintStr = data.getStringExtra(AppConstants.HINT_INTENT_KEY);
                    optionValue1 = data.getStringExtra(AppConstants.OPTION_VALUE_1_INTENT_KEY);
                    optionValue2 = data.getStringExtra(AppConstants.OPTION_VALUE_2_INTENT_KEY);
                    optionValue3 = data.getStringExtra(AppConstants.OPTION_VALUE_3_INTENT_KEY);
                    optionValue4 = data.getStringExtra(AppConstants.OPTION_VALUE_4_INTENT_KEY);
                    selectedServiceProvider = data.getStringExtra(AppConstants.JSON_OPERATOR_NAME_KEY);
                    selectedOperatorId = data.getIntExtra(AppConstants.JSON_OPERATORID_KEY, 0);

                    myUtility.printLogcat("Hint::" + intentHintStr);
                    myUtility.printLogcat("optionValue1::" + optionValue1);
                    myUtility.printLogcat("optionValue2::" + optionValue2);
                    myUtility.printLogcat("optionValue3::" + optionValue3);
                    myUtility.printLogcat("optionValue4::" + optionValue4);
                    amountLayout.setVisibility(View.VISIBLE);
                    operatorET.setText(selectedServiceProvider.trim());
                    contactBtn.setVisibility(intentSelectStr.equalsIgnoreCase(AppConstants.POSTPAID_VALUE) ? View.VISIBLE : View.GONE);
                }
            }
        }
    }

    @Override
    public void getSuccessfulHash(PayUmoneySdkInitializer.PaymentParam mPaymentParam) {
        if (AppPreference.selectedTheme != -1) {
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParam, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
        } else {
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParam, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
        }
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void paymentGatewayStatus(int statusCode, String statusMessage) {
        if (statusCode == 0) {
            long time = System.currentTimeMillis();
            try {
                String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                        AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + selectedMobileNumber +
                        AppConstants.AMOUNT_KEY + "10" + AppConstants.OPERATOR_ID_KEY + selectedOperatorId +
                        AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + URLEncoder.encode(intentTitleStr, "utf-8") +
                        AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");
                myUtility.printLogcat("API::::" + url);

                mRechargePresenter.callRechargeAPI(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showSuccess(JSONObject successJSON) {

    }

    @Override
    public void clearView() {
        mobileNumberET.setText("");
        operatorET.setText("");
        amountET.setText("");
    }

    /**
     * This AsyncTask generates hash from server.
     */
   /* private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
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
                        */

    /**
     * This hash is mandatory and needs to be generated from merchant's server side
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
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.READ_CONTACTS_REQUEST_CODE) {
            String permission = permissions[0];
            int grantResult = grantResults[0];
            if (permission.equals(AppConstants.READ_CONTACTS_PERMISSIONS)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    callContactIntent();
                } else {
                    mRuntimePermission.requestPermission(AppConstants.READ_CONTACTS_PERMISSIONS, AppConstants.READ_CONTACTS_REQUEST_CODE);
                }
            }
        }

    }

    private void callContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void phoneNumberWithOutCountryCode(String phoneNumberWithCountryCode) {
        String phoneNumberWithoutCountryCode = "";
        if (phoneNumberWithCountryCode.startsWith("+") && phoneNumberWithCountryCode.length() == 13) {
            String[] phonenUmber = phoneNumberWithCountryCode.split("\\+91");
            myUtility.printLogcat("Split 0::" + phonenUmber[0]);
            myUtility.printLogcat("Split 1::" + phonenUmber[1]);
            phoneNumberWithoutCountryCode = phonenUmber[1];
        } else {
            phoneNumberWithoutCountryCode = phoneNumberWithCountryCode;
        }
        Log.e("number is", phoneNumberWithoutCountryCode);
        mobileNumberET.setText(phoneNumberWithoutCountryCode);
        getOperatorBackground(phoneNumberWithoutCountryCode.substring(0, 4));

    }


    @Override
    public void onPaymentSuccess(String s) {
        paymentGatewayStatus(0, s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        paymentGatewayStatus(i, s);
    }
}
