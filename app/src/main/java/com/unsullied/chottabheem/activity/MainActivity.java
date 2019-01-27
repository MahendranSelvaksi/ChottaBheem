package com.unsullied.chottabheem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.unsullied.chottabheem.R;
import com.unsullied.chottabheem.fragment.AboutUsFragment;
import com.unsullied.chottabheem.fragment.AddAdminRequestFragment;
import com.unsullied.chottabheem.fragment.AdminRequestFragment;
import com.unsullied.chottabheem.fragment.ContactUsFragment;
import com.unsullied.chottabheem.fragment.HomeFragment;
import com.unsullied.chottabheem.fragment.NotificationFragment;
import com.unsullied.chottabheem.fragment.PrivatePolicyFragment;
import com.unsullied.chottabheem.fragment.ProfileFragment;
import com.unsullied.chottabheem.fragment.SettingsFragment;
import com.unsullied.chottabheem.fragment.TermsAndConditionsFragment;
import com.unsullied.chottabheem.utils.AppConstants;
import com.unsullied.chottabheem.utils.BaseFragment;
import com.unsullied.chottabheem.utils.CustomTextView;
import com.unsullied.chottabheem.utils.SessionManager;
import com.unsullied.chottabheem.utils.Utility;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ContactUsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, AdminRequestFragment.OnFragmentInteractionListener,
        TermsAndConditionsFragment.OnFragmentInteractionListener, PrivatePolicyFragment.OnFragmentInteractionListener,
        AboutUsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
        AddAdminRequestFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener {
    DrawerLayout drawer;
    Toolbar toolbar;
    private CustomTextView titleTV, logoutTV, userNameTV, userMobileTV, referralCodeTV, overallTV, progressTV, pointsTV;
    private ProgressBar pbId;
    private Utility mUtility;
    private LinearLayout pointsLayout;
    private SessionManager mSessionManager;
    private Context mContext;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        titleTV = toolbar.findViewById(R.id.toolbar_title);
        pointsLayout = toolbar.findViewById(R.id.pointsLayout);
        pointsTV = toolbar.findViewById(R.id.pointsTV);
        mUtility = new Utility();
        mSessionManager = new SessionManager();
        mContext = getApplicationContext();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        pointsTV.setText("100 ".concat(getString(R.string.pointStr)));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        logoutTV = (CustomTextView) hView.findViewById(R.id.logoutTV);
        userNameTV = (CustomTextView) hView.findViewById(R.id.userNameTV);
        referralCodeTV = (CustomTextView) hView.findViewById(R.id.referralCodeTV);
        userMobileTV = (CustomTextView) hView.findViewById(R.id.userMobileTV);
        progressTV = (CustomTextView) hView.findViewById(R.id.progressTV);
        overallTV = (CustomTextView) hView.findViewById(R.id.overallTV);
        pbId = hView.findViewById(R.id.pbId);
        updateHeaderViewValue();

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        pointsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getCurrentFragment() instanceof AdminRequestFragment)) {
                    updateToolbarTitle(getString(R.string.admin_requestTitle));
                    replaceFragment(new AdminRequestFragment(), "");
                }
            }
        });

        updateToolbarTitle(getString(R.string.homeTitle));
        replaceFragment(new HomeFragment(), "");

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mUtility.hideKeyboard(MainActivity.this, drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mUtility.hideKeyboard(MainActivity.this, drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //AccountKit.logOut();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            mUtility.hideKeyboard(MainActivity.this, drawer);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (doubleBackToExitPressedOnce) {
                    finish();
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                String tag = fragmentManager
                        .getBackStackEntryAt(
                                fragmentManager
                                        .getBackStackEntryCount() - 2)
                        .getName();
                mUtility.printLogcat("Tag name POP:::" + tag);
                changeTitleFromBackStackFragment(tag);
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navHome:
                if (!(getCurrentFragment() instanceof HomeFragment)) {
                    updateToolbarTitle(getString(R.string.homeTitle));
                    replaceFragment(new HomeFragment(), "");
                }
                break;
            case R.id.navProfile:
                if (!(getCurrentFragment() instanceof ProfileFragment)) {
                    updateToolbarTitle(getString(R.string.profileTitle));
                    replaceFragment(new ProfileFragment(), "");
                }
                break;
            case R.id.navAdminReq:
                if (!(getCurrentFragment() instanceof AdminRequestFragment)) {
                    updateToolbarTitle(getString(R.string.admin_requestTitle));
                    replaceFragment(new AdminRequestFragment(), "");
                }
                break;
            case R.id.navTerms:
                if (!(getCurrentFragment() instanceof TermsAndConditionsFragment)) {
                    updateToolbarTitle(getString(R.string.terms_and_conditionsTitle));
                    replaceFragment(new TermsAndConditionsFragment(), "");
                }
                break;
            case R.id.navPrivatePolicy:
                if (!(getCurrentFragment() instanceof PrivatePolicyFragment)) {
                    updateToolbarTitle(getString(R.string.privacy_policyTitle));
                    replaceFragment(new PrivatePolicyFragment(), "");
                }
                break;
            case R.id.navContact:
                if (!(getCurrentFragment() instanceof ContactUsFragment)) {
                    updateToolbarTitle(getString(R.string.contact_usTitle));
                    replaceFragment(new ContactUsFragment(), "");
                }
                break;
            case R.id.navAbout:
                if (!(getCurrentFragment() instanceof AboutUsFragment)) {
                    updateToolbarTitle(getString(R.string.about_usTitle));
                    replaceFragment(new AboutUsFragment(), "");
                }
                break;
            case R.id.navNotification:
                if (!(getCurrentFragment() instanceof NotificationFragment)) {
                    updateToolbarTitle(getString(R.string.notificationTitle));
                    replaceFragment(new NotificationFragment(), "");
                }
                break;
            case R.id.navShareReferral:
                /* */
                shareIt();
                break;
            case R.id.navSetting:
                if (!(getCurrentFragment() instanceof SettingsFragment)) {
                    updateToolbarTitle(getString(R.string.setting_Title));
                    replaceFragment(new SettingsFragment(), "");
                }
                break;
            case R.id.navExit:
                exitDialog();
                break;

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        return currentFragment;
    }

    private void updateToolbarTitle(String title) {
        titleTV.setText(title.trim());
    }

    private void replaceFragment(BaseFragment fragment, String parameter) {
        //replacing the fragment
        if (fragment != null) {
            if (!parameter.equals("")) {
                Bundle args = new Bundle();
                args.putString("parameter", parameter);
                fragment.setArguments(args);
            }
            String backStateName = fragment.getClass().getName();
            mUtility.printLogcat("Tag name PUSH:::" + backStateName);
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = false;
            if (!fragmentPopped) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        }
    }

    @Override
    public void contactUsFragmentChangeListerner(BaseFragment fragment, String parameter) {
        updateToolbarTitle(getString(R.string.contact_usTitle));
        replaceFragment(fragment, parameter);
    }

    @Override
    public void onHomeFragmentListener(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.homeTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onProfileFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.profileTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onAdminRequestFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.admin_requestTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onTermsAndConditionsFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.admin_requestTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onPrivatePolicyFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.privacy_policyTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onAboutUsFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.about_usTitle));
        replaceFragment(mFragment, parameter);
    }

    @Override
    public void onShareReferralFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.setting_Title));
        replaceFragment(mFragment, parameter);
    }

   /* public static void updateNavigationHeader() {
        if (AppConstants.debug) Log.w(AppConstants.tag, "Called updateNavigationHeader()");
        headerCustomerNameTV.setText(sessionManager.getCustomerDisplayName(mContext).trim());
        headerCustomerEmailTV.setText(sessionManager.getValueFromSession(mContext, AppConstants.PROFILE_SESSION_NAME, AppConstants.CUSTOMER_EMAIL_KEY).trim());
    }*/

    private void changeTitleFromBackStackFragment(String tag) {
        String title = "";
        switch (tag) {
            case "com.unsullied.chottabheem.fragment.HomeFragment":
                title = getString(R.string.homeTitle);
                break;
            case "com.unsullied.chottabheem.fragment.AboutUsFragment":
                title = getString(R.string.about_usTitle);
                break;
            case "com.unsullied.chottabheem.fragment.AdminRequestFragment":
                title = getString(R.string.admin_requestTitle);
                break;
            case "com.unsullied.chottabheem.fragment.ContactUsFragment":
                title = getString(R.string.contact_usTitle);
                break;
            case "com.unsullied.chottabheem.fragment.PrivatePolicyFragment":
                title = getString(R.string.privacy_policyTitle);
                break;
            case "com.unsullied.chottabheem.fragment.ProfileFragment":
                title = getString(R.string.profileTitle);
                break;
            case "com.unsullied.chottabheem.fragment.SettingsFragment":
                title = getString(R.string.setting_Title);
                break;
            case "com.unsullied.chottabheem.fragment.TermsAndConditionsFragment":
                title = getString(R.string.terms_and_conditionsTitle);
                break;
            case "com.unsullied.chottabheem.fragment.AddAdminRequestFragment":
                title = getString(R.string.add_admin_request_Title);
                break;
            case "com.unsullied.chottabheem.fragment.NotificationFragment":
                title = getString(R.string.notificationTitle);
                break;
        }
        updateToolbarTitle(title);
    }

    private void shareIt() {
//sharing implementation here
        String shareBody = "Hi...\n" +
                "I used a new app called Chotta Bheem (Recharge at the Curb), to do mobile rechange and get redeem valies, loved it. Give it a try, you'll love it!\n" +
                "\n" +
                "Refferal Code:RA2010\n" +
                "\n" +
                "For Android:\n" +
                "https://play.google.com/store/apps/details?id=com.unsullied.chottabheem";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void exitDialog() {
        android.app.AlertDialog.Builder errorMsgBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        errorMsgBuilder.setTitle("Alert!")
                .setMessage("Do You want to Exit or Not?")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        android.app.AlertDialog errorAlert = errorMsgBuilder.create();
        if (!errorAlert.isShowing())
            errorAlert.show();
    }

    private void logoutDialog() {
        android.app.AlertDialog.Builder errorMsgBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        errorMsgBuilder.setTitle("Alert!")
                .setMessage("Do You want to Logout?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSessionManager.logoutSession(getApplicationContext(), AppConstants.USER_SESSION_NAME);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                });
        android.app.AlertDialog errorAlert = errorMsgBuilder.create();
        if (!errorAlert.isShowing())
            errorAlert.show();
    }

    @Override
    public void onAddAdminRequestFragmentInteraction(BaseFragment mFragment, String parameter) {
        updateToolbarTitle(getString(R.string.add_admin_request_Title));
        replaceFragment(mFragment, parameter);
    }

    public void updateHeaderViewValue() {
        pbId.setMax(Integer.parseInt(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_OVERALL_REFERRAL_KEY)));
        pbId.setProgress(Integer.parseInt(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY)));
        userNameTV.setText(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_NAME_KEY));
        userMobileTV.setText(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.USER_MOBILE_KEY));
        referralCodeTV.setText(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REFERRAL_CODE_KEY));
        progressTV.setText(String.valueOf(pbId.getProgress()).concat("/").concat(String.valueOf(pbId.getMax())));
        // overallTV.setText(mSessionManager.getValueFromSessionByKey(mContext, AppConstants.USER_SESSION_NAME, AppConstants.API_REDEEM_PROGRESS_KEY));
    }

    @Override
    public void onNotificationFragmentInteraction(BaseFragment mFragment, String parameter) {
        replaceFragment(mFragment,parameter);
    }
}
