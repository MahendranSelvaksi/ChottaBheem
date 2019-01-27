package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unsullied.chottabheem.BuildConfig;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.SmileyRemover;
import com.unsullied.chottabheem.utils.mvp.ProfileMVP;
import com.unsullied.chottabheem.utils.mvp.ProfilePresenter;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener, ProfileMVP.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int userId;
    private CustomEditText etName, etEmailId, etMobileNumber, etComments;
    private TextInputLayout nameTIL, emailTIL, mobileNumberTIL, commentsTIL;
    private CustomTextView btnSend;
    private View rootView;
    private SmileyRemover smileyRemover;
    private Activity mActivity;
    private Context mContext;
    private ProgressDialog pd;
    private OnFragmentInteractionListener mListener;
    private SessionManager sessionManager;
    private ProfilePresenter mPresenter;
    private String accessToken;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);

        mActivity = getActivity();
        mContext = getContext();
        sessionManager = new SessionManager();
        smileyRemover = new SmileyRemover();
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        pd.setMessage(AppConstants.CONTACT_US_API_CALL_DIALOG_MSG);
        mPresenter = new ProfilePresenter(mContext, this);
        accessToken = sessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.ACCESS_TOKEN_KEY);
        userId = sessionManager.getIntValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_ID_KEY);

        etName = rootView.findViewById(R.id.etName);
        etEmailId = rootView.findViewById(R.id.etEmailId);
        etMobileNumber = rootView.findViewById(R.id.etMobileNumber);
        etComments = rootView.findViewById(R.id.etComments);
        btnSend = rootView.findViewById(R.id.btnSend);
        nameTIL = rootView.findViewById(R.id.nameTIL);
        emailTIL = rootView.findViewById(R.id.emailTIL);
        mobileNumberTIL = rootView.findViewById(R.id.mobileNumberTIL);
        commentsTIL = rootView.findViewById(R.id.commentsTIL);

        etName.setFilters(new InputFilter[]{smileyRemover});
        etEmailId.setFilters(new InputFilter[]{smileyRemover});

        btnSend.setOnClickListener(this);


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
        if (v == btnSend) {
            if (etName.getText().toString().trim().length() == 0 || etEmailId.getText().toString().trim().length() == 0 ||
                    etMobileNumber.getText().toString().trim().length() == 0 || etComments.getText().toString().trim().length() == 0) {

                if (etName.getText().toString().trim().length() == 0 && etEmailId.getText().toString().trim().length() == 0 &&
                        etMobileNumber.getText().toString().trim().length() == 0 && etComments.getText().toString().trim().length() == 0) {
                    nameTIL.setError(getString(R.string.enterNameStr));
                    emailTIL.setError(getString(R.string.enterEmailIdStr));
                    mobileNumberTIL.setError(getString(R.string.enterMobileNumberStr));
                    commentsTIL.setError(getString(R.string.enterCommentsStr));

                } else if (etName.getText().toString().trim().length() == 0) {
                    nameTIL.setError(getString(R.string.enterNameStr));
                } else if (etEmailId.getText().toString().trim().length() == 0) {
                    emailTIL.setError(getString(R.string.enterEmailIdStr));
                } else if (etMobileNumber.getText().toString().trim().length() == 0) {
                    mobileNumberTIL.setError(getString(R.string.enterMobileNumberStr));
                } else if (etComments.getText().toString().trim().length() == 0) {
                    commentsTIL.setError(getString(R.string.enterCommentsStr));
                }
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmailId.getText().toString().trim()).matches()) {
                    emailTIL.setError(getString(R.string.enterValidEmailIdStr));
                } /*else if (!AppConstants.MOBILE_NUMBER_REX.matches(etMobileNumber.getText().toString().trim())) {
                    mobileNumberTIL.setError("Please give valid mobile number with country code (+91)");
                }*/ else {
                    //Call api
                    pd.show();
                    mPresenter.callContactUsAPI(AppConstants.CONTACT_US_API, userId, accessToken, etName.getText().toString().trim(),
                            etEmailId.getText().toString().trim(), AppConstants.OS_NAME_VALUE, BuildConfig.VERSION_NAME,
                            etMobileNumber.getText().toString().trim(), etComments.getText().toString().trim());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nameTIL.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    emailTIL.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mobileNumberTIL.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    commentsTIL.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void updateProfile(JSONObject profileJSON) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
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
        etName.setText("");
        etEmailId.setText("");
        etMobileNumber.setText("");
        etComments.setText("");
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
        void contactUsFragmentChangeListerner(BaseFragment fragment, String parameter);
    }
}
