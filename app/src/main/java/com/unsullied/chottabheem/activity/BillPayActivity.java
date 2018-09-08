package com.unsullied.chottabheem.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomEditText;

public class BillPayActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView tittleTV;
    private FrameLayout operatorLayout, numberLayout, dobLayout;
    private CustomEditText dobET, operatorET, mobileNumberET;
    private Button operatorSelectBtn, contactBtn, dobBtn, payBtn;
    private TextInputLayout mobileNumberTIL;

    private String intentTitleStr, intentHintStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay);

        intentTitleStr = getIntent().getStringExtra(AppConstants.TITLE_INTENT_KEY);
        intentHintStr = getIntent().getStringExtra(AppConstants.HINT_INTENT_KEY);

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
        mobileNumberTIL = findViewById(R.id.mobileNumberTIL);
        dobET = findViewById(R.id.dobET);
        operatorET = findViewById(R.id.operatorET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
        operatorSelectBtn = findViewById(R.id.operatorSelectBtn);
        contactBtn = findViewById(R.id.contactBtn);
        dobBtn = findViewById(R.id.dobBtn);
        payBtn = findViewById(R.id.payBtn);

        setHintForEditText(intentHintStr);


        dobLayout.setVisibility(View.GONE);


    }


    private void setHintForEditText(String hint) {
      //  mobileNumberET.setHint(hint.trim());
        mobileNumberTIL.setHint(hint);
    }
}
