package com.unsullied.chottabheem.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.razorpay.PaymentResultListener;
import com.unsullied.chottabheem.BuildConfig;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.material_spinnar.MaterialSpinner;
import com.unsullied.chottabheem.utils.material_spinnar.MaterialSpinnerAdapter;
import com.unsullied.chottabheem.utils.mvp.LoginMVP;
import com.unsullied.chottabheem.utils.mvp.LoginPresenter;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayMVP;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayPresenter;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener, LoginMVP.View, PaymentGatewayMVP.View, PaymentResultListener {

    Toolbar toolbar;
    private Button submitBtn;
    private LinearLayout agreeCBLayout, subscriptionCBLayout;
    private CheckBox agreeCB, subscriptionCB;
    private CustomTextView agreeCBTV, subscriptionCBTV;
    private TextView tittleTV;
    private CustomEditText userNameET, emailET, referralCodeET;
    private TextInputLayout referralCodeTIL;
    private MaterialSpinner materialSpinner;
    private AppPreference mAppPreference;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private Utility myUtility;
    private String nameStr, mobileNumberStr, emailIdStr, accountId;
    private SessionManager mSessionManager;
    private int userId;
    private Context mContext;
    private Activity mActivity;
    private ProgressDialog pd;
    private LoginPresenter mLoginPresenter;
    private String paymentId = "jbfkdf", paymentMessage = "";
    private PaymentGatewayPresenter mPaymentGatewayPresenter;
    private String referralCodePass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_verification);

        mContext = getApplicationContext();
        mActivity = this;
        mAppPreference = new AppPreference();
        myUtility = new Utility();
        mSessionManager = new SessionManager();
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        mLoginPresenter = new LoginPresenter(mContext, this, mActivity);
        mPaymentGatewayPresenter = new PaymentGatewayPresenter(mContext, mActivity, this);

        accountId = getIntent().getStringExtra(AppConstants.ACCOUNT_ID_KEY);
        userId = mSessionManager.isLogged(mContext);
        nameStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY);
        //mobileNumberStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY);
        mobileNumberStr = getIntent().getStringExtra(AppConstants.USER_MOBILE_KEY);
        emailIdStr = mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY);

        toolbar = (Toolbar) findViewById(R.id.verificationToolbar);
        tittleTV = toolbar.findViewById(R.id.toolbar_title);

        tittleTV.setText("Verification");

        agreeCBLayout = findViewById(R.id.agreeCBLayout);
        agreeCB = findViewById(R.id.agreeCB);
        agreeCBTV = findViewById(R.id.agreeCBTV);
        subscriptionCBLayout = findViewById(R.id.subscriptionCBLayout);
        subscriptionCB = findViewById(R.id.subscriptionCB);
        subscriptionCBTV = findViewById(R.id.subscriptionCBTV);
        submitBtn = findViewById(R.id.verify_submit_btn);
        userNameET = findViewById(R.id.verify_username_et);
        emailET = findViewById(R.id.verify_email_et);
        referralCodeET = findViewById(R.id.verify_referral_code_et);
        materialSpinner = findViewById(R.id.referralCodeSP);
        referralCodeTIL = findViewById(R.id.referralCodeTIL);
        submitBtn.setOnClickListener(this);
        agreeCBTV.setOnClickListener(this);
        agreeCBLayout.setOnClickListener(this);

        agreeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        emailET.setText(emailIdStr.trim());
        subscriptionCBTV.setClickable(false);
        subscriptionCBLayout.setClickable(false);
        subscriptionCB.setClickable(false);
        subscriptionCB.setChecked(true);
    }


    @Override
    public void onClick(View v) {
        if (v == submitBtn) {
            nameStr = userNameET.getText().toString().trim();
            emailIdStr = emailET.getText().toString().trim();
            if (nameStr.length() == 0 || emailIdStr.length() == 0) {
                if (nameStr.length() == 0 && emailIdStr.length() == 0) {
                    Toast.makeText(this, "Please fill All values...", Toast.LENGTH_SHORT).show();
                } else if (nameStr.length() == 0) {
                    Toast.makeText(this, "Please fill Name field...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please fill email field...", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (Patterns.EMAIL_ADDRESS.matcher(emailIdStr).matches()) {
                    if (!agreeCB.isChecked()) {
                        Toast.makeText(this, "Please accept terms and conditions...", Toast.LENGTH_SHORT).show();
                    } else {
                        //launchPayUMoneyFlow();
                       /* mPaymentGatewayPresenter.generateHashFromServer(mobileNumberStr, accountId, nameStr, emailIdStr, "99",
                                "Subscription", "Pay Now", "Subscription");*/
                        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
                        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        String deviceType = Build.DEVICE;
                        referralCodePass = referralCodeET.getVisibility() == View.VISIBLE ? referralCodeET.getText().toString().trim() : referralCodePass;
                        myUtility.printLogcat("Referral:::" + referralCodePass);
                        pd.setMessage(AppConstants.REGISTER_API_CALL_DIALOG_MSG);
                        pd.show();
                        mLoginPresenter.callUpdateLoginAPI(AppConstants.REGISTER_LOGIN_API, accountId, mobileNumberStr, versionCode, paymentId,
                                nameStr, emailIdStr, "99", AppConstants.OS_NAME_VALUE, deviceId, deviceType, referralCodePass);
                        // mPaymentGatewayPresenter.startPayment(mContext,mActivity,"Subscription","10000",emailIdStr,mobileNumberStr);

                      /*  mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                AppConstants.USER_NAME_KEY, nameStr);
                        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                AppConstants.USER_MOBILE_KEY, mobileNumberStr);
                        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                AppConstants.USER_EMAIL_ID_KEY, emailIdStr);
                        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                AppConstants.FB_ID_KEY, accountId);
                        mSessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY, "1bc44c8b644fa15e7e519957f8f32d64");
                        mSessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_POINT_KEY, "100");
                        mSessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY, "9");
                        mSessionManager.addValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY, "10");
                        startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                        finish();*/
                    }
                } else {
                    Toast.makeText(this, "Give valid email address...", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == agreeCBTV || v == agreeCBLayout) {
            agreeCB.setChecked(!agreeCB.isChecked());
        }
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        myUtility.printLogcat("MainActivity" + "request code " + requestCode + " resultcode " + resultCode);
        // myUtility.printLogcat("Data:::"+data.toString());
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

                myUtility.printLogcat("Payu's Data : " + payuResponse);
                myUtility.printLogcat("Merchant's Data: " + merchantResponse);
                try {
                    JSONObject payResponse = new JSONObject(payuResponse);
                    JSONObject resultObject = payResponse.getJSONObject("result");
                    paymentId = String.valueOf(resultObject.getString("txnid"));
                    paymentMessage = resultObject.getString("error_Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AlertDialog.Builder(VerificationActivity.this)
                        .setCancelable(false)
                        .setTitle("Payment Status")
                        .setMessage("You are successfully subscribed!!!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                        AppConstants.USER_NAME_KEY, nameStr);
                                mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                        AppConstants.USER_MOBILE_KEY, mobileNumberStr);
                                mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                        AppConstants.USER_EMAIL_ID_KEY, emailIdStr);
                                mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                                        AppConstants.FB_ID_KEY, accountId);
                                startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                                finish();
                                String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
                                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                String deviceType = Build.DEVICE;
                                referralCodePass = referralCodeET.getVisibility() == View.VISIBLE ? referralCodeET.getText().toString().trim() : referralCodePass;
                                myUtility.printLogcat("Referral:::"+referralCodePass);
                                pd.setMessage(AppConstants.LOGIN_API_CALL_DIALOG_MSG);
                                pd.show();
                                mLoginPresenter.callUpdateLoginAPI(AppConstants.REGISTER_LOGIN_API, accountId, mobileNumberStr, versionCode, paymentId,
                                        nameStr, emailIdStr, "99", AppConstants.OS_NAME_VALUE, deviceId, deviceType, referralCodePass);
                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                myUtility.printLogcat("Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                myUtility.printLogcat("Both objects are null!");
            }
        }
    }*/

    @Override
    public void showSuccess(int code, String message) {
        closeProgressDialog();
        if (code == 1001) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerificationActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void showError(int code, String errorMsg) {
        closeProgressDialog();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showListOfReferralCode(List<Object> referralList) {
        referralCodeET.setVisibility(View.GONE);
        referralCodeTIL.setVisibility(View.GONE);
        materialSpinner.setVisibility(View.VISIBLE);
        materialSpinner.setAdapter(new MaterialSpinnerAdapter<Object>(mContext, referralList));
        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String selected = String.valueOf(item);
                myUtility.printLogcat("Selected Referral::::" + selected);
                referralCodePass = selected.split("-")[1];
                myUtility.printLogcat("After split::::" + referralCodePass);
            }
        });
    }

    @Override
    public void callPayment() {
        mPaymentGatewayPresenter.startPayment(mContext, mActivity, "Subscription", "9900", emailIdStr, mobileNumberStr);
    }

    private void closeProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void getSuccessfulHash(PayUmoneySdkInitializer.PaymentParam mPaymentParam) {
        //  if (AppPreference.selectedTheme != -1) {
        //  PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParam, mActivity, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
        //} else {
        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParam, mActivity, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
        //}
    }

    @Override
    public void showError(String errorMsg) {
        closeProgressDialog();
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void paymentGatewayStatus(int statusCode, String statusMessage) {
      //  pd.show();
        /*mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                AppConstants.USER_NAME_KEY, nameStr);
        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                AppConstants.USER_MOBILE_KEY, mobileNumberStr);
        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                AppConstants.USER_EMAIL_ID_KEY, emailIdStr);
        mSessionManager.addValueToSession(getApplicationContext(), AppConstants.USER_SESSION_NAME,
                AppConstants.FB_ID_KEY, accountId);
        startActivity(new Intent(VerificationActivity.this, MainActivity.class));
        finish();
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceType = Build.DEVICE;
        referralCodePass = referralCodeET.getVisibility() == View.VISIBLE ? referralCodeET.getText().toString().trim() : referralCodePass;
        myUtility.printLogcat("Referral:::"+referralCodePass);
        pd.setMessage(AppConstants.LOGIN_API_CALL_DIALOG_MSG);
        pd.show();
        paymentId=statusMessage;
        mLoginPresenter.callUpdateLoginAPI(AppConstants.REGISTER_LOGIN_API, accountId, mobileNumberStr, versionCode, paymentId,
                nameStr, emailIdStr, "99", AppConstants.OS_NAME_VALUE, deviceId, deviceType, referralCodePass);*/
        myUtility.printLogcat("statusCode::::"+statusCode);
        String paymentStatus="0";
        if (statusCode==100001){
            paymentStatus="1";
        }else if (statusCode==0){
            paymentStatus="0";
        }else {
            paymentStatus="2";
        }
        myUtility.printLogcat("paymentStatus::::"+paymentStatus);
        mPaymentGatewayPresenter.updatePaymentStatus(String.valueOf(mSessionManager.getIntValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY)),
                mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY),
                "99", statusMessage, "Subscription", statusMessage, paymentStatus);
    }

    @Override
    public void onPaymentSuccess(String s) {
        myUtility.printLogcat("Payment Success:::::" + s);
        mSessionManager.addIntValueToSession(mContext,AppConstants.USER_SESSION_NAME,AppConstants.PAYMENT_STATUS_KEY,1);
        paymentGatewayStatus(100001, s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        myUtility.printLogcat("Payment Error:::::" + s);
        myUtility.printLogcat("Payment Error code:::::" + i);
        mSessionManager.addIntValueToSession(mContext,AppConstants.USER_SESSION_NAME,AppConstants.PAYMENT_STATUS_KEY,0);
        paymentGatewayStatus(i, s);
    }
}
