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

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.Utility;

public class BillPayActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout, dobLayout, amountLayout;
    private CustomEditText dobET, operatorET, mobileNumberET, amountET;
    private Button operatorSelectBtn, contactBtn, dobBtn;
    private TextInputLayout mobileNumberTIL;
    private CustomTextView payBtn, billTitleTV;
    private ImageView billIconIV;

    private String intentTitleStr, intentHintStr;
    private int pageIcon;
    private Utility myUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay);

        myUtility = new Utility();

        intentTitleStr = getIntent().getStringExtra(AppConstants.TITLE_INTENT_KEY);
        intentHintStr = getIntent().getStringExtra(AppConstants.HINT_INTENT_KEY);
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
        dobLayout = findViewById(R.id.dobLayout);
        amountLayout = findViewById(R.id.amountLayout);
        mobileNumberTIL = findViewById(R.id.mobileNumberTIL);
        dobET = findViewById(R.id.dobET);
        operatorET = findViewById(R.id.operatorET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
        amountET = findViewById(R.id.amountET);
        operatorSelectBtn = findViewById(R.id.operatorSelectBtn);
        contactBtn = findViewById(R.id.contactBtn);
        dobBtn = findViewById(R.id.dobBtn);
        payBtn = findViewById(R.id.payBtn);
        billTitleTV = findViewById(R.id.billTitleTV);
        billIconIV = findViewById(R.id.billIconIV);

        myUtility.printLogcat("Image ::::" + pageIcon);
        setHintForEditText(intentHintStr);
        billTitleTV.setText(intentTitleStr.trim());
        billIconIV.setImageResource(pageIcon);


        dobLayout.setVisibility(View.GONE);


    }


    private void setHintForEditText(String hint) {
        //  mobileNumberET.setHint(hint.trim());
        mobileNumberTIL.setHint(hint);
    }
}
