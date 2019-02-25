package com.unsullied.chottabheem.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.razorpay.PaymentResultListener;
import com.unsullied.chottabheem.BuildConfig;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.PatternEditableBuilder;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.mvp.LoginMVP;
import com.unsullied.chottabheem.utils.mvp.LoginPresenter;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayMVP;
import com.unsullied.chottabheem.utils.mvp.PaymentGatewayPresenter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements LoginMVP.View, PaymentGatewayMVP.View, PaymentResultListener {
    Button loginBtn;
    public static int APP_REQUEST_CODE = 99;
    Toolbar toolbar;
    private TextView tittleTV, termsTV;
    private Utility myUtility;
    private String deviceId, accountId, versionCode, phoneNumberString, deviceType;
    private LoginPresenter mLoginPresenter;
    private PaymentGatewayPresenter mPaymentGatewayPresenter;
    private ProgressDialog pd;
    private Context mContext;
    private Activity mActivity;
    private SessionManager mSessionManager;

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();
        mActivity = this;
        myUtility = new Utility();
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        mSessionManager = new SessionManager();
        mPaymentGatewayPresenter = new PaymentGatewayPresenter(mContext, mActivity, this);

        toolbar = (Toolbar) findViewById(R.id.loginToolBar);
        tittleTV = toolbar.findViewById(R.id.toolbar_title);


        loginBtn = findViewById(R.id.loginBtn);

        getHAshKey();

        tittleTV.setText("Login");
        termsTV = (TextView) findViewById(R.id.loginTermsTV);
        termsTV.setText(getString(R.string.termsCondition));
        new PatternEditableBuilder().
                addPattern(Pattern.compile(getString(R.string.terms_and_conditionsTitle)), ContextCompat.getColor(this, R.color.black_overlay), new PatternEditableBuilder.SpannableClickedListener() {
                    @Override
                    public void onSpanClicked(String text) {
                        startActivity(new Intent(LoginActivity.this, TermsAndConditionActivity.class));
                    }
                }).
                addPattern(Pattern.compile(getString(R.string.privacy_policyTitle)), ContextCompat.getColor(this, R.color.black_overlay), new PatternEditableBuilder.SpannableClickedListener() {
                    @Override
                    public void onSpanClicked(String text) {
                        startActivity(new Intent(LoginActivity.this, PrivatePolicyActivity.class));
                    }
                }).

                into(termsTV);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();*/
                phoneLogin();
                /*AccessToken accessToken = AccountKit.getCurrentAccessToken();

                if(accessToken !=null)

                {
                    //Handle Returning User

                } else

                {
                    //Handle new or logged out user

                }
*/
              /*  Intent intent = new Intent(LoginActivity.this,WebViewActivity.class);
                intent.putExtra(AvenuesParams.ACCESS_CODE, "AVFZ82FL33BI74ZFIB");
                intent.putExtra(AvenuesParams.MERCHANT_ID,"201889");
                intent.putExtra(AvenuesParams.ORDER_ID, "100044");
                intent.putExtra(AvenuesParams.CURRENCY, "INR");
                intent.putExtra(AvenuesParams.AMOUNT, "1.0");
                intent.putExtra(AvenuesParams.REDIRECT_URL, "http://122.182.6.216/merchant/ccavResponseHandler.jsp");
                intent.putExtra(AvenuesParams.CANCEL_URL, "http://122.182.6.216/merchant/ccavResponseHandler.jsp");
                intent.putExtra(AvenuesParams.RSA_KEY_URL, "http://adhavanassociates.com/CB_api/api/getRSA");

                startActivity(intent);*/
            }
        });

        mLoginPresenter = new LoginPresenter(mContext, this, mActivity);
    }

    public void phoneLogin() {
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    /*public void emailLogin(final View view) {
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.EMAIL,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }*/


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            Log.w("Success", "AccountKitLoginResult::::" + loginResult.getAuthorizationCode());
            String toastMessage = "";
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                /*if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));

                }*/
                myUtility.printLogcat(toastMessage);
                getAccount();

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                //goToMyLoggedInActivity();
            }

            // Surface the result to your user in an appropriate way.
            /*Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();*/
        }
    }


    private void getHAshKey() {
        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }
    }

    /**
     * Gets current account from Facebook Account Kit which include user's phone number.
     */
    private void getAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @SuppressLint("HardwareIds")
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();


                phoneNumberString = phoneNumber.toString().substring(3, 13);
                myUtility.printLogcat("FB ID::::" + account.getId());
                myUtility.printLogcat("Phone Number::" + phoneNumberString);
                myUtility.printLogcat("Email:::" + account.getEmail());
                accountId = account.getId();
                versionCode = String.valueOf(BuildConfig.VERSION_CODE);
                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                deviceType = Build.DEVICE;
                pd.setMessage(AppConstants.LOGIN_API_CALL_DIALOG_MSG);
                pd.show();
                mLoginPresenter.callLoginAPI(AppConstants.LOGIN_API, versionCode, accountId, phoneNumberString, AppConstants.LOGIN_TYPE_VALUE, deviceId,
                        AppConstants.OS_NAME_VALUE, deviceType);

            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e("AccountKit", error.toString());
                // Handle Error
            }
        });
    }

    private void callNextActivity(boolean navigateToHome) {
        myUtility.printLogcat("NavigateToHome:::" + navigateToHome);
        Intent verifyIntent = new Intent(LoginActivity.this, navigateToHome ? MainActivity.class : VerificationActivity.class);
        verifyIntent.putExtra(AppConstants.ACCOUNT_ID_KEY, accountId);
        verifyIntent.putExtra(AppConstants.USER_MOBILE_KEY, phoneNumberString);
        startActivity(verifyIntent);
        finish();
    }

    @Override
    public void getSuccessfulHash(PayUmoneySdkInitializer.PaymentParam mPaymentParam) {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void paymentGatewayStatus(int statusCode, String statusMessage) {
        mPaymentGatewayPresenter.updatePaymentStatus(String.valueOf(mSessionManager.getIntValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY)),
                mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY),
                "99", statusMessage, "Subscription", statusMessage, statusCode == 100001 ? "1" : "2");
    }

    @Override
    public void showSuccess(int code, String message) {
        closeProgressDialog();
        myUtility.printLogcat("Code:::" + code);
      //  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (code == 1001)
            callNextActivity(true);
        else if (code ==1)
            callNextActivity(false);
    }

    @Override
    public void showError(int code, String errorMsg) {
        closeProgressDialog();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showListOfReferralCode(List<Object> referralList) {

    }

    @Override
    public void callPayment() {
        mPaymentGatewayPresenter.startPayment(mContext, mActivity, "Subscription", "9900",
                mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_EMAIL_ID_KEY),
                mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY));
    }

    private void closeProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        pd.show();
        myUtility.printLogcat("Payment Success:::::" + s);
        mSessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.PAYMENT_STATUS_KEY, 1);
        paymentGatewayStatus(100001, s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        pd.show();
        myUtility.printLogcat("Payment Error:::::" + s);
        myUtility.printLogcat("Payment Error code:::::" + i);
        mSessionManager.addIntValueToSession(mContext, AppConstants.USER_SESSION_NAME, AppConstants.PAYMENT_STATUS_KEY, 2);
        paymentGatewayStatus(2, s);
    }
}
