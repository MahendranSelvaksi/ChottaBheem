package com.unsullied.chottabheem.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.activity.MainActivity;
import com.unsullied.chottabheem.utils.AppController;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.LocaleManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SwitchCompat languageSW;
    private View rootView;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_share_refferal, container, false);

        languageSW = rootView.findViewById(R.id.languageSW);
//languageSW.setOnCheckedChangeListener(this);
        /*languageSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.w("Success","isChecked:::"+isChecked+(isChecked ? LocaleManager.LANGUAGE_ENGLISH : LocaleManager.LANGUAGE_TAMIL));
            //    setNewLocale(isChecked ? LocaleManager.LANGUAGE_ENGLISH : LocaleManager.LANGUAGE_TAMIL, true);
            }
        });*/

       /*languageSW.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               setNewLocale(AppController.localeManager.getLanguage().equalsIgnoreCase(LocaleManager.LANGUAGE_ENGLISH) ?
                       LocaleManager.LANGUAGE_TAMIL : LocaleManager.LANGUAGE_ENGLISH, false);
               return false;
           }
       });*/

       languageSW.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setNewLocale(AppController.localeManager.getLanguage().equalsIgnoreCase(LocaleManager.LANGUAGE_ENGLISH) ?
                       LocaleManager.LANGUAGE_TAMIL : LocaleManager.LANGUAGE_ENGLISH, true);
           }
       });
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
    public void onResume() {
        super.onResume();
         Log.w("Success","AppController.localeManager.getLanguage():::"+AppController.localeManager.getLanguage());
        languageSW.setChecked(AppController.localeManager.getLanguage().equalsIgnoreCase(LocaleManager.LANGUAGE_ENGLISH));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

   /* @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.w("Success","isChecked:::"+isChecked+(isChecked ? LocaleManager.LANGUAGE_ENGLISH : LocaleManager.LANGUAGE_TAMIL));
    }*/

    private boolean setNewLocale(String language, boolean restartProcess) {
        AppController.localeManager.setNewLocale(getContext(), language);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        if (restartProcess) {
            System.exit(0);
        } else {
            //Toast.makeText(getActivity(), "Activity restarted", Toast.LENGTH_SHORT).show();
        }
        return true;
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
        void onShareReferralFragmentInteraction(BaseFragment mFragment, String parameter);
    }
}
