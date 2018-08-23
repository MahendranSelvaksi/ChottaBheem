package com.unsullied.chottabheem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.CustomTextView;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private Button submitBtn;
    private LinearLayout agreeCBLayout, subscriptionCBLayout;
    private CheckBox agreeCB, subscriptionCB;
    private CustomTextView agreeCBTV, subscriptionCBTV;
    private TextView tittleTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

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

        submitBtn.setOnClickListener(this);
        agreeCBTV.setOnClickListener(this);
        agreeCBLayout.setOnClickListener(this);

        agreeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        subscriptionCBTV.setClickable(false);
        subscriptionCBLayout.setClickable(false);
        subscriptionCB.setClickable(false);
        subscriptionCB.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        if (v == submitBtn) {
            if (!agreeCB.isChecked()) {
                Toast.makeText(this, "Please accept terms and conditions...", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                finish();
            }
        } else if (v == agreeCBTV || v == agreeCBLayout) {
            agreeCB.setChecked(!agreeCB.isChecked());
        }
    }
}
