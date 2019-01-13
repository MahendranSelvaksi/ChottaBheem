package com.unsullied.chottabheem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.unsullied.chottabheem.R;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Value Stream Technologies on 14-08-2018.
 */
public class PrivatePolicyActivity extends AppCompatActivity {
    Toolbar toolbar;
    private TextView tittleTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.fragment_terms_and_conditions);

        toolbar = (Toolbar) findViewById(R.id.termsAndConditionToolbar);
        tittleTV = toolbar.findViewById(R.id.toolbar_title);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setVisibility(View.VISIBLE);
        tittleTV.setText(getString(R.string.privacy_policyTitle));
    }
}
