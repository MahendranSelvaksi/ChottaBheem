package com.unsullied.chottabheem.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.adapter.HomeMenuAdapter;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView homeMenuRecyclerView;
    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private Activity mActivity;
    private Utility myUtility;
    private List<String> menuData;
    private HomeMenuAdapter menuAdapter;
    private List<Integer> menuIcons;
    private int[] iconsArray = new int[]{R.drawable.ic_online_payment, R.drawable.ic_bus_ticket,
            R.drawable.ic_ticket, R.drawable.ic_satellite_dish, R.drawable.ic_mobile_broadband_modem, R.drawable.ic_payment_method
            , R.drawable.ic_light_bulb, R.drawable.ic_gas, R.drawable.ic_insurance, R.drawable.ic_telephone, R.drawable.ic_wifi};

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeMenuRecyclerView = rootView.findViewById(R.id.homeMenuRecyclerView);


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();
        mActivity = getActivity();
        myUtility = new Utility();
        menuData = new ArrayList<>();
        menuIcons = new ArrayList<>();
        String[] menuArray = getResources().getStringArray(R.array.menuArray);
        menuData.addAll(Arrays.asList(menuArray));
        for (int i = 0; i < iconsArray.length; i++) {
            menuIcons.add(iconsArray[i]);
        }


        homeMenuRecyclerView.setHasFixedSize(true);
        homeMenuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        menuAdapter = new HomeMenuAdapter(mContext, menuData, iconsArray, this);
        homeMenuRecyclerView.setAdapter(menuAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onHomeFragmentListener(BaseFragment mFragment, String parameter);
    }


    public void callNextActivity(int position) {
        String selectedStr=menuData.get(position).trim();
        Toast.makeText(mContext, ""+selectedStr, Toast.LENGTH_SHORT).show();
       if (selectedStr.equals(menuData.get(0))){

       }else if (selectedStr.equals(menuData.get(1))){

       }else if (selectedStr.equals(menuData.get(2))){

       }else if (selectedStr.equals(menuData.get(3))){

       }else if (selectedStr.equals(menuData.get(4))){

       }else if (selectedStr.equals(menuData.get(5))){

       }else if (selectedStr.equals(menuData.get(6))){

       }else if (selectedStr.equals(menuData.get(7))){

       }else if (selectedStr.equals(menuData.get(8))){

       }

    }
}
