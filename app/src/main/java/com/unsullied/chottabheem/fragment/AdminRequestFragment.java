package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.adapter.AdminRequestAdapter;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.RedeemModel;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.mvp.ProfileMVP;
import com.unsullied.chottabheem.utils.mvp.RedeemMVP;
import com.unsullied.chottabheem.utils.mvp.RedeemPresenter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRequestFragment extends BaseFragment implements ProfileMVP.View, RedeemMVP.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView adminRequestRecyclerView;
    private FloatingActionButton addRequestFAB;
    private AdminRequestAdapter mAdminRequestAdapter;
    private List<RedeemModel> mRedeemData;
    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private Context mContext;
    private Utility myUtility;
    private SessionManager sessionManager;
    private ProgressDialog pd;
    private RedeemPresenter mRedeemPresenter;

    public AdminRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminRequestFragment newInstance(String param1, String param2) {
        AdminRequestFragment fragment = new AdminRequestFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_admin_request, container, false);

        mActivity = getActivity();
        mContext = getContext();
        myUtility=new Utility();
        sessionManager = new SessionManager();
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        pd.setMessage(AppConstants.GET_REDEEM_API_CALL_DIALOG_MSG);
        pd.show();
        mRedeemData = new ArrayList<>();
        mRedeemPresenter = new RedeemPresenter(mContext, this, this);

        addRequestFAB = rootView.findViewById(R.id.addRequestFAB);
        adminRequestRecyclerView = rootView.findViewById(R.id.adminRequestRecyclerView);
        adminRequestRecyclerView.setHasFixedSize(true);
        adminRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdminRequestAdapter = new AdminRequestAdapter(getActivity(), mRedeemData);
        adminRequestRecyclerView.setAdapter(mAdminRequestAdapter);
        addRequestFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAdminRequestFragmentInteraction(new AddAdminRequestFragment(), "");
            }
        });

        mRedeemPresenter.getRedeemList();
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
    }

    @Override
    public void updateAdapter(List<RedeemModel> mData) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
        myUtility.printLogcat("Come updateAdapter"+mData.size());
        mRedeemData.addAll(mData);
        mAdminRequestAdapter.notifyDataSetChanged();
        adminRequestRecyclerView.setVisibility(mRedeemData.size() > 0 ? View.VISIBLE : View.GONE);
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
        void onAdminRequestFragmentInteraction(BaseFragment mFragment, String parameter);
    }
}
