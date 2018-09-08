package com.unsullied.chottabheem.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.Utility;

import org.json.JSONArray;

public class RechargeActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout;
    private CustomEditText operatorET, mobileNumberET;
    private Button operatorSelectBtn, contactBtn;
    private CustomTextView browsePlansTV, rechargeBtn, rechargeTitleTV;
    private TextInputLayout mobileNumberTIL;
    private ImageView rechargeIconIV;

    private String intentTitleStr, intentHintStr;
    private int pageIcon;
    private Utility myUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        myUtility = new Utility();

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
        mobileNumberTIL = findViewById(R.id.mobileNumberTIL);
        operatorET = findViewById(R.id.operatorET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
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

    }


    private void setHintForEditText(String hint) {
        //mobileNumberET.setHint(hint.trim());
        mobileNumberTIL.setHint(hint);
    }


    private void getOperatorBackgroundly() {
        AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }


}
