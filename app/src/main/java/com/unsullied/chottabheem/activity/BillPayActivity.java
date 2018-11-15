package com.unsullied.chottabheem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
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
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
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

import static com.unsullied.chottabheem.utils.AppConstants.PICK_CONTACT;

public class BillPayActivity extends AppCompatActivity implements View.OnClickListener, RechargeMVP.RechargeView, PaymentGatewayMVP.View {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout, optionValue1Layout, optionValue2Layout, optionValue3Layout, optionValue4Layout, amountLayout, idLayout;
    private CustomEditText optionValue1ET, optionValue2ET, optionValue3ET, optionValue4ET, operatorET, mobileNumberET, amountET, consumerNumberET;
    private Button operatorSelectBtn, contactBtn, dobBtn;
    private TextInputLayout mobileNumberTIL, consumerNumberTIL, optionValue1TIL, optionValue2TIL, optionValue3TIL, optionValue4TIL;
    private CustomTextView payBtn, billTitleTV;
    private ImageView billIconIV;

    private String intentTitleStr, intentHintStr, cNumber, intentSelectStr, optionValue1 = "", optionValue2 = "", optionValue3 = "", optionValue4 = "";
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
        setContentView(R.layout.activity_bill_pay);

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
        intentSelectStr = getIntent().getStringExtra(AppConstants.SELECTED_INTENT_KEY);
        pageIcon = getIntent().getIntExtra(AppConstants.ICON_INTENT_KEY, 0);

        toolbar = findViewById(R.id.billPayToolbar);
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
        optionValue1Layout = findViewById(R.id.optionValue1Layout);
        optionValue2Layout = findViewById(R.id.optionValue2Layout);
        optionValue3Layout = findViewById(R.id.optionValue3Layout);
        optionValue4Layout = findViewById(R.id.optionValue4Layout);
        idLayout = findViewById(R.id.idLayout);
        amountLayout = findViewById(R.id.amountLayout);
        mobileNumberTIL = findViewById(R.id.mobileNumberTIL);
        consumerNumberTIL = findViewById(R.id.consumerNumberTIL);
        optionValue1TIL = findViewById(R.id.optionValue1TIL);
        optionValue2TIL = findViewById(R.id.optionValue2TIL);
        optionValue3TIL = findViewById(R.id.optionValue3TIL);
        optionValue4TIL = findViewById(R.id.optionValue4TIL);
        optionValue1ET = findViewById(R.id.optionValue1ET);
        optionValue2ET = findViewById(R.id.optionValue2ET);
        optionValue3ET = findViewById(R.id.optionValue3ET);
        optionValue4ET = findViewById(R.id.optionValue4ET);
        operatorET = findViewById(R.id.operatorET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
        amountET = findViewById(R.id.amountET);
        optionValue1ET = findViewById(R.id.optionValue1ET);
        optionValue3ET = findViewById(R.id.optionValue3ET);
        optionValue2ET = findViewById(R.id.optionValue2ET);
        optionValue4ET = findViewById(R.id.optionValue4ET);
        consumerNumberET = findViewById(R.id.consumerNumberET);
        operatorSelectBtn = findViewById(R.id.operatorSelectBtn);
        contactBtn = findViewById(R.id.contactBtn);
        //   dobBtn = findViewById(R.id.dobBtn);
        payBtn = findViewById(R.id.payBtn);
        billTitleTV = findViewById(R.id.billTitleTV);
        billIconIV = findViewById(R.id.billIconIV);

        myUtility.printLogcat("Image ::::" + pageIcon);
        billTitleTV.setText(intentTitleStr.trim());
        billIconIV.setImageResource(pageIcon);


        optionValue1Layout.setVisibility(View.GONE);
        optionValue2Layout.setVisibility(View.GONE);
        optionValue3Layout.setVisibility(View.GONE);
        optionValue4Layout.setVisibility(View.GONE);
        idLayout.setVisibility(View.GONE);
        amountLayout.setVisibility(View.GONE);
        numberLayout.setVisibility(View.GONE);

        mobileNumberET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        operatorET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        amountET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        optionValue1ET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        optionValue2ET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        optionValue3ET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});
        optionValue4ET.setFilters(new InputFilter[]{smileyRemover,symbolsRemover});

        showHide();
        nameStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY);
        emailIdStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY);

        //mobileNumberET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        payBtn.setOnClickListener(this);
        operatorET.setOnClickListener(this);
        operatorSelectBtn.setOnClickListener(this);
        operatorLayout.setOnClickListener(this);
        contactBtn.setOnClickListener(this);

    }

    private void showHide() {
        switch (intentSelectStr) {
            case AppConstants.POSTPAID_VALUE:
                idLayout.setVisibility(View.GONE);
                //   numberLayout.setVisibility(View.VISIBLE);
                break;
            case AppConstants.EB_VALUE:
                numberLayout.setVisibility(View.GONE);
                break;
            case AppConstants.GAS_VALUE:
                numberLayout.setVisibility(View.GONE);
                break;
            case AppConstants.INSURANCE_VALUE:
                numberLayout.setVisibility(View.GONE);
                break;
            case AppConstants.LANDLINE_VALUE:
                numberLayout.setVisibility(View.GONE);
                break;
            case AppConstants.BROADBAND_VLAUE:
                numberLayout.setVisibility(View.GONE);
                break;

        }
    }


    private void setHintForEditText(String hint, TextInputLayout mTextInputLayout) {
        //  mobileNumberET.setHint(hint.trim());
        mTextInputLayout.setHint(hint);
    }

    @Override
    public void onClick(View v) {
        if (v == payBtn) {
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
                //mPaymentGatewayPresenter.launchPayUMoneyFlow(String.valueOf(rechargeAmount), selectedMobileNumber, emailIdStr);
                mPaymentGatewayPresenter.generateHashFromServer(selectedMobileNumber,
                        mSessionManager.getValueFromSessionByKey(mContext,AppConstants.USER_SESSION_NAME,AppConstants.FB_ID_KEY),
                        nameStr,emailIdStr, String.valueOf(rechargeAmount),
                        "Recharge","Pay Now","Recharge");
            }
        } else if (v == operatorET || v == operatorLayout || v == operatorSelectBtn) {
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
                                long time = System.currentTimeMillis();
                                try {
                                    String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                                            AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY + selectedMobileNumber +
                                            AppConstants.AMOUNT_KEY + rechargeAmount + AppConstants.OPERATOR_ID_KEY + selectedOperatorId +
                                            AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + URLEncoder.encode(intentTitleStr, "utf-8") +
                                            AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");
                                    myUtility.printLogcat("API::::" + url);

                                    //mRechargePresenter.callRechargeAPI(url);
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
                    idLayout.setVisibility(View.VISIBLE);
                    amountLayout.setVisibility(View.VISIBLE);
                    operatorET.setText(selectedServiceProvider.trim());
                    setHintForEditText(intentHintStr, consumerNumberTIL);
                    setHintForEditText(optionValue1, optionValue1TIL);
                    setHintForEditText(optionValue2, optionValue2TIL);
                    setHintForEditText(optionValue3, optionValue3TIL);
                    setHintForEditText(optionValue4, optionValue4TIL);
                    contactBtn.setVisibility(intentSelectStr.equalsIgnoreCase(AppConstants.POSTPAID_VALUE) ? View.VISIBLE : View.GONE);

                    optionValue1Layout.setVisibility(optionValue1.trim().length() > 0 ? View.VISIBLE : View.GONE);
                    optionValue2Layout.setVisibility(optionValue2.trim().length() > 0 ? View.VISIBLE : View.GONE);
                    optionValue3Layout.setVisibility(optionValue3.trim().length() > 0 ? View.VISIBLE : View.GONE);
                    optionValue4Layout.setVisibility(optionValue4.trim().length() > 0 ? View.VISIBLE : View.GONE);
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
    public void showSuccess(JSONObject successJSON) {

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
        consumerNumberET.setText(phoneNumberWithoutCountryCode);
        getOperatorBackground(phoneNumberWithoutCountryCode.substring(0, 4));
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
                                    Toast.makeText(mActivity, "" + responseJSON.getString(AppConstants.RES_TEXT_KEY).trim(), Toast.LENGTH_SHORT).show();
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
}
