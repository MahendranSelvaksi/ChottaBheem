package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.CustomEditText;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.material_spinnar.MaterialSpinner;
import com.unsullied.chottabheem.utils.material_spinnar.MaterialSpinnerAdapter;
import com.unsullied.chottabheem.utils.mvp.ProfileMVP;
import com.unsullied.chottabheem.utils.mvp.RedeemPresenter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddAdminRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAdminRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAdminRequestFragment extends BaseFragment implements ProfileMVP.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private MaterialSpinner operatorSP;
    private CustomEditText etPoints, etMobile;
    private SeekBar redeemSeekBar;
    private Button submitBtn;
    private CustomTextView startValueTV, endValueTV;

    private OnFragmentInteractionListener mListener;

    private List<Object> mOperaterList;
    private Activity mActivity;
    private Utility myUtility;
    private SessionManager sessionManager;
    private ProgressDialog pd;
    private RedeemPresenter mRedeemPresenter;
    private int selectedOperatorPosition = -1;
    private boolean isSeekbarValueChanged = false;


    public AddAdminRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAdminRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAdminRequestFragment newInstance(String param1, String param2) {
        AddAdminRequestFragment fragment = new AddAdminRequestFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_admin_request, container, false);

        mActivity = getActivity();
        mContext = getContext();
        myUtility = new Utility();
        sessionManager = new SessionManager();
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        pd.setMessage(AppConstants.ADD_REDEEM_API_CALL_DIALOG_MSG);
        mRedeemPresenter = new RedeemPresenter(mContext, this);

        operatorSP = rootView.findViewById(R.id.operatorSP);
        redeemSeekBar = rootView.findViewById(R.id.redeemSeekBar);
        startValueTV = rootView.findViewById(R.id.startingValueTV);
        endValueTV = rootView.findViewById(R.id.finalValueTV);
        submitBtn = rootView.findViewById(R.id.submitBtn);
        etPoints = rootView.findViewById(R.id.etPoints);
        etMobile = rootView.findViewById(R.id.etMobile);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mOperaterList = new ArrayList<>();
        String[] operatorArry = getResources().getStringArray(R.array.operatorArry);
        mOperaterList.addAll(Arrays.asList(operatorArry));
        startValueTV.setText("0");
        endValueTV.setText("1500");
        operatorSP.setAdapter(new MaterialSpinnerAdapter<Object>(mContext, mOperaterList));
        operatorSP.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedOperatorPosition = position == 0 ? -1 : position;
            }
        });

        redeemSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isSeekbarValueChanged = true;
                endValueTV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etPoints.getText().toString().length() == 0 || etMobile.getText().toString().length() == 0 ||
                        selectedOperatorPosition == -1 || !isSeekbarValueChanged) {
                    if (etPoints.getText().toString().length() == 0 && etMobile.getText().toString().length() == 0) {
                        Toast.makeText(mContext, "Please give value to all fields...", Toast.LENGTH_SHORT).show();
                    } else if (etPoints.getText().toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Please give points value...", Toast.LENGTH_SHORT).show();
                    } else if (etMobile.getText().toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Please give mobile number...", Toast.LENGTH_SHORT).show();
                    } else if (selectedOperatorPosition == -1) {
                        Toast.makeText(mContext, "Please select operator...", Toast.LENGTH_SHORT).show();
                    } else if (!isSeekbarValueChanged) {
                        Toast.makeText(mContext, "Please choose redeem value...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (endValueTV.getText().toString().equals("0")) {
                        Toast.makeText(mContext, "Please give valid redeem value...", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.show();
                        mRedeemPresenter.addRedeem(etPoints.getText().toString(), etMobile.getText().toString(), operatorArry[selectedOperatorPosition],
                                endValueTV.getText().toString());
                    }
                }
            }
        });

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
    public void updateProfile(JSONObject profileJSON) {

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
        mListener.onAddAdminRequestFragmentInteraction(new AdminRequestFragment(), "");
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
        void onAddAdminRequestFragmentInteraction(BaseFragment mFragment, String parameter);
    }
}
