package com.unsullied.chottabheem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.adapter.OperationSelectionAdapter;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.dataModel.CircleModel;
import com.unsullied.chottabheem.utils.dataModel.OperatorSelectionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class SelectOperatorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private CustomTextView noDataTV, tittleTV;
    private String intentSelectStr;
    private Utility myUtility;
    private int selectedCircleId = 0, selectedOperatorId = 0;
    private String selectedLocation, selectedServiceProvider;
    private Activity mActivity;
    private Context mContext;
    private OperationSelectionAdapter mOperationSelectionAdapter;
    private List<OperatorSelectionModel> mOperatorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_select_operator);
        mActivity = this;
        mContext = getApplicationContext();
        mOperatorsList = new ArrayList<>();
        mOperationSelectionAdapter = new OperationSelectionAdapter(mContext, mOperatorsList, mActivity);
        myUtility = new Utility();

        intentSelectStr = getIntent().getStringExtra(AppConstants.SELECTED_INTENT_KEY);

        toolbar = (Toolbar) findViewById(R.id.operatorToolbar);
        tittleTV = toolbar.findViewById(R.id.toolbar_title);

        tittleTV.setText("Operators");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = findViewById(R.id.commonRecyclerView);
        noDataTV = findViewById(R.id.commonNoDataTV);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mOperationSelectionAdapter);
        setJSON();

    }

    @Override
    public void onBackPressed() {
        Intent prevIntent = new Intent();
        setResult(RESULT_CANCELED, prevIntent);
        finish();
    }

    public void callPrevIntent(int position, int childPosition) {
        Intent prevIntent = new Intent();
        myUtility.printLogcat("Hint:::" + mOperatorsList.get(position).getHint().trim());
        prevIntent.putExtra(AppConstants.HINT_INTENT_KEY, mOperatorsList.get(position).getHint().trim());
        if (childPosition != -1) {
            String operatorNameWithCircleName = mOperatorsList.get(position).getOperatorName().concat("(".concat(mOperatorsList.get(position).getCircleList().get(childPosition).getCircleName()).concat(")"));
            prevIntent.putExtra(AppConstants.JSON_OPERATOR_NAME_KEY, operatorNameWithCircleName);
        } else {
            prevIntent.putExtra(AppConstants.JSON_OPERATOR_NAME_KEY, mOperatorsList.get(position).getOperatorName().trim());
        }
        prevIntent.putExtra(AppConstants.JSON_OPERATORID_KEY, mOperatorsList.get(position).getOperatorId());
        prevIntent.putExtra(AppConstants.OPTION_VALUE_1_INTENT_KEY, mOperatorsList.get(position).getOptionValue1().trim());
        prevIntent.putExtra(AppConstants.OPTION_VALUE_2_INTENT_KEY, mOperatorsList.get(position).getOptionValue2().trim());
        prevIntent.putExtra(AppConstants.OPTION_VALUE_3_INTENT_KEY, mOperatorsList.get(position).getOptionValue3().trim());
        prevIntent.putExtra(AppConstants.OPTION_VALUE_4_INTENT_KEY, mOperatorsList.get(position).getOptionValue4().trim());
        setResult(RESULT_OK, prevIntent);
        finish();
    }

    private void updateAdapter(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                OperatorSelectionModel model = new OperatorSelectionModel();
                model.setOperatorId(object.getInt(AppConstants.JSON_OPERATORID_KEY));
                model.setOperatorName(object.getString(AppConstants.JSON_OPERATOR_NAME_KEY));
                model.setHint(object.getString(AppConstants.JSON_HINT_KEY));
                model.setOpen(false);
                model.setOptionValue1(object.getString(AppConstants.OPTION_VALUE_1_INTENT_KEY));
                model.setOptionValue2(object.getString(AppConstants.OPTION_VALUE_2_INTENT_KEY));
                model.setOptionValue3(object.getString(AppConstants.OPTION_VALUE_3_INTENT_KEY));
                model.setOptionValue4(object.getString(AppConstants.OPTION_VALUE_4_INTENT_KEY));
                model.setClickable(object.getBoolean("clickable"));
                List<CircleModel> circleList = new ArrayList<>();
                if (object.has(AppConstants.CIRCLE_JSON_KEY)) {
                    JSONArray jsonArray1 = object.getJSONArray(AppConstants.CIRCLE_JSON_KEY);
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject object1 = jsonArray1.getJSONObject(j);
                        CircleModel circleModel = new CircleModel();
                        circleModel.setCircleId(object1.getInt(AppConstants.JSON_CIRCLEID_KEY));
                        circleModel.setCircleName(object1.getString(AppConstants.JSON_CIRCLE_NAME_KEY));
                        circleList.add(circleModel);
                    }
                }
                model.setCircleList(circleList);
                mOperatorsList.add(model);
            }
            mOperationSelectionAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setJSON() {
        switch (intentSelectStr) {
            case AppConstants.POSTPAID_VALUE:
                updateAdapter(AppConstants.POSTPAID_JSON);
                break;
            case AppConstants.EB_VALUE:
                updateAdapter(AppConstants.EB_JSON);
                break;
            case AppConstants.GAS_VALUE:
                updateAdapter(AppConstants.GAS_JSON);
                break;
            case AppConstants.INSURANCE_VALUE:
                updateAdapter(AppConstants.INSURANCE_JSON);
                break;
            case AppConstants.LANDLINE_VALUE:
            case AppConstants.BROADBAND_VLAUE:
                updateAdapter(AppConstants.LANDLINE_JSON);
                break;
            case AppConstants.PREPAID_VALIE:
                updateAdapter(AppConstants.PREPAID_OPERATORS_JSON);
                break;
            case AppConstants.DTH_VALUE:
                updateAdapter(AppConstants.DTH_JSON);
                break;
            case AppConstants.DATA_CARD_VALUE:
                updateAdapter(AppConstants.DATA_CARD_JSON);
                break;

        }
    }
}
