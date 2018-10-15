package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.adapter.ChildAdapter;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.SmileyRemover;
import com.unsullied.chottabheem.utils.dataModel.ChildModel;
import com.unsullied.chottabheem.utils.mvp.ProfileMVP;
import com.unsullied.chottabheem.utils.mvp.ProfilePresenter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener, ProfileMVP.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomEditText etName, etEmailId, etMobileNumber, etReferralCount, etParent;
    private TextInputLayout nameTIL, emailTIL, mobileNumberTIL;
    private CustomTextView nodataTV;
    private RecyclerView mRecyclerView;
    private Button btnSubmit;

    private View rootView;
    private SmileyRemover smileyRemover;
    private Activity mActivity;
    private Context mContext;
    private List<ChildModel> childList;
    private ChildAdapter mChildAdapter;
    private SessionManager sessionManager;
    private ProgressDialog pd;
    private ProfilePresenter mProfilePresenter;
    private String accessToken = "";
    private int userId;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mActivity = getActivity();
        mContext = getContext();
        sessionManager = new SessionManager();
        smileyRemover = new SmileyRemover();
        childList = new ArrayList<>();
        mProfilePresenter = new ProfilePresenter(mContext, this);
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        pd.setMessage(AppConstants.PROFILE_API_CALL_DIALOG_MSG);
        pd.show();
        accessToken = sessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY);
        userId = sessionManager.getIntValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY);
        etName = rootView.findViewById(R.id.etName);
        etEmailId = rootView.findViewById(R.id.etEmailId);
        etMobileNumber = rootView.findViewById(R.id.etMobileNumber);
        etParent = rootView.findViewById(R.id.etParent);
        etReferralCount = rootView.findViewById(R.id.etReferralCount);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        nameTIL = rootView.findViewById(R.id.nameTIL);
        emailTIL = rootView.findViewById(R.id.emailTIL);
        mobileNumberTIL = rootView.findViewById(R.id.mobileNumberTIL);

        nodataTV = rootView.findViewById(R.id.commonNoDataTV);
        mRecyclerView = rootView.findViewById(R.id.commonRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        etName.setFilters(new InputFilter[]{smileyRemover});
        etEmailId.setFilters(new InputFilter[]{smileyRemover});

        mChildAdapter = new ChildAdapter(mContext, childList);
        mRecyclerView.setAdapter(mChildAdapter);

        mProfilePresenter.callGetProfileAPI(String.valueOf(userId), accessToken, AppConstants.OS_NAME_VALUE, "1.0");


        btnSubmit.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (etName.getText().toString().trim().length() == 0 || etEmailId.getText().toString().trim().length() == 0 ||
                    etMobileNumber.getText().toString().trim().length() == 0) {

                if (etName.getText().toString().trim().length() == 0 && etEmailId.getText().toString().trim().length() == 0 &&
                        etMobileNumber.getText().toString().trim().length() == 0) {
                    nameTIL.setError("Enter your name");
                    emailTIL.setError("Enter your email id");
                    mobileNumberTIL.setError("Enter your mobile number");

                } else if (etName.getText().toString().trim().length() == 0) {
                    nameTIL.setError("Enter your name");
                } else if (etEmailId.getText().toString().trim().length() == 0) {
                    emailTIL.setError("Enter your email id");
                } else if (etMobileNumber.getText().toString().trim().length() == 0) {
                    mobileNumberTIL.setError("Enter your mobile number");
                }
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmailId.getText().toString().trim()).matches()) {
                    emailTIL.setError("Enter your valid email id");
                } else if (!AppConstants.MOBILE_NUMBER_REX.matches(etMobileNumber.getText().toString().trim())) {
                    mobileNumberTIL.setError("Please give valid mobile number with country code (+91)");
                } else {
                    //Call api
                    pd.setMessage(AppConstants.UPDATE_PROFILE_API_CALL_DIALOG_MSG);
                    //   pd.show();
                    // mProfilePresenter.updateProfile(userId, accessToken, etName.getText().toString().trim(), etEmailId.getText().toString().trim(), etMobileNumber.getText().toString().trim());
                }
            }
        }
    }

    @Override
    public void updateProfile(JSONObject profileJSON) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
        try {
            JSONObject jsonObject = profileJSON;
            etName.setText(jsonObject.getString(AppConstants.NAME_KEY));
            etEmailId.setText(jsonObject.getString(AppConstants.EMAIL_KEY));
            etMobileNumber.setText(jsonObject.getString(AppConstants.PHONE_KEY));
            etReferralCount.setText(jsonObject.getString(AppConstants.API_REDEEM_POINT_KEY));
            etParent.setText(jsonObject.getString(AppConstants.API_PARENT_KEY));
            JSONArray childArray = jsonObject.getJSONArray("child_data");
            for (int i = 0; i < childArray.length(); i++) {
                ChildModel model = new ChildModel();
                model.setChildName(childArray.getJSONObject(i).getString(AppConstants.NAME_KEY));
                model.setChildEmailId(childArray.getJSONObject(i).getString(AppConstants.EMAIL_KEY));
                model.setChildReferralCode(childArray.getJSONObject(i).getString(AppConstants.API_CHILD_REFERRAL_CODE_KEY));
                model.setChildId(childArray.getJSONObject(i).getInt(AppConstants.API_CHILD_USER_ID_KEY));
                childList.add(model);
            }

            mChildAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showError(int code, String errorMsg) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(int code, String message) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(BaseFragment mFragment, String parameter);
    }
}
