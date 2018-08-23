package com.unsullied.chottabheem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.unsullied.chottabheem.R;

public class TermsAndConditionActivity extends AppCompatActivity {
    Toolbar toolbar;
    private TextView tittleTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        tittleTV.setText(getString(R.string.terms_and_conditionsTitle));
    }
}
