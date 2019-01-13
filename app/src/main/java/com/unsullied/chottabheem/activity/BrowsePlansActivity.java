package com.unsullied.chottabheem.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.fragment.DataPlansFragment;
import com.unsullied.chottabheem.fragment.FTTFragment;
import com.unsullied.chottabheem.fragment.RoamingFragment;
import com.unsullied.chottabheem.fragment.SPLOffersFragment;
import com.unsullied.chottabheem.fragment.TopupFragment;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.CustomTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class BrowsePlansActivity extends AppCompatActivity {

    public static int operatorId = 0, selectedCircleId = 0;
    public static String emailIdStr, selectedMobileNumber;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String> titleData;
    //public List<String> hintData;
    private Toolbar toolbar;
    private CustomTextView tittleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_browse_plans);

        titleData = new ArrayList<>();
        operatorId = getIntent().getIntExtra(AppConstants.JSON_OPERATOR_ID_KEY, 0);
        selectedMobileNumber = getIntent().getStringExtra(AppConstants.USER_MOBILE_KEY);
        selectedCircleId = getIntent().getIntExtra(AppConstants.JSON_CIRCLE_ID_KEY, 0);
        emailIdStr = getIntent().getStringExtra(AppConstants.USER_EMAIL_ID_KEY);

        String[] titleArray = getResources().getStringArray(R.array.browse_plans_titles);
        String[] hintArray = getResources().getStringArray(R.array.browse_plans_key);
        titleData.addAll(Arrays.asList(titleArray));
        //      hintData.addAll(Arrays.asList(hintArray));

        toolbar = (Toolbar) findViewById(R.id.plansToolbar);
        // setSupportActionBar(toolbar);
          tittleTV = findViewById(R.id.toolbar_title);
         tittleTV.setText("Best Plans");
       // tittleTV.setGravity(Gravity.LEFT);
       // toolbar.setTitle("Best Plans");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        setupTab();


    }


    /**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
     * The fragment argument representing the section number for this
     * fragment.
     *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        */

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_browse_plans, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }*/
    private void setupTab() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(titleData.get(0));
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(titleData.get(1));
        tabTwo.setGravity(Gravity.CENTER);
        tabTwo.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(titleData.get(2));
        tabThree.setGravity(Gravity.CENTER);
        tabThree.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.getTabAt(2).setCustomView(tabThree);


        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(titleData.get(3));
        tabFour.setGravity(Gravity.CENTER);
        tabFour.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.getTabAt(3).setCustomView(tabFour);


        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText(titleData.get(4));
        tabFive.setGravity(Gravity.CENTER);
        tabFive.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.getTabAt(4).setCustomView(tabFive);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            if (position == 0) {
                fragment = new SPLOffersFragment();
            } else if (position == 1) {
                fragment = new DataPlansFragment();
            } else if (position == 2) {
                fragment = new FTTFragment();
            } else if (position == 3) {
                fragment = new TopupFragment();
            } else if (position == 4) {
                fragment = new RoamingFragment();
            }
            return fragment;
        }

      /*  @Override
        public CharSequence getPageTitle(int position) {
            return titleData.get(position);
        }*/

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }
}
