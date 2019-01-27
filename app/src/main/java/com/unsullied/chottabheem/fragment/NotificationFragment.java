package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.adapter.NotificationAdapter;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.RedeemModel;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;
import com.unsullied.chottabheem.utils.mvp.NotificationPresenter;
import com.unsullied.chottabheem.utils.mvp.RedeemMVP;
import com.unsullied.chottabheem.utils.paymentgateway.AppPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends BaseFragment implements RedeemMVP.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RelativeLayout includeLayout;
    private RecyclerView mRecyclerView;
    private CustomTextView mNoDataTV;

    private Context mContext;
    private List<RedeemModel> mData;
    private NotificationAdapter mNotificationAdapter;
    private NotificationPresenter mNotificationPresenter;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private Utility myUtility;
    private AppPreference mAppPreference;
    private SessionManager sessionManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        // return inflater.inflate(R.layout.fragment_notification, container, false);
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        mContext = getActivity();
        mActivity = getActivity();
        myUtility = new Utility();
        mAppPreference = new AppPreference();
        mData = new ArrayList<>();
        sessionManager = new SessionManager();
        mNotificationPresenter = new NotificationPresenter(mContext, this, mActivity);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
          mProgressDialog.show();

        includeLayout = rootView.findViewById(R.id.fttPlansLayout);
        mRecyclerView = includeLayout.findViewById(R.id.commonRecyclerView);
        mNoDataTV = includeLayout.findViewById(R.id.commonNoDataTV);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mNotificationAdapter = new NotificationAdapter(mActivity, mData);
        mRecyclerView.setAdapter(mNotificationAdapter);
        mNotificationPresenter.getNotification();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragment.OnFragmentInteractionListener) {
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
    public void updateAdapter(List<RedeemModel> mData) {
        mProgressDialog.dismiss();
        if (mData.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataTV.setVisibility(View.GONE);
            this.mData.addAll(mData);
            mNotificationAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataTV.setVisibility(View.VISIBLE);
        }
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
        void onNotificationFragmentInteraction(BaseFragment mFragment, String parameter);
    }
}
