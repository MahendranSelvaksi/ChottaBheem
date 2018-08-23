package com.unsullied.chottabheem.utils;

public class MainActivitya /*extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DeliveryLocation.DeliveryLocationOnFragmentChangedListener,
        AddEditAddressFragment.AddressFragmentOnFragmentChangedListener,
        ListAddressFragment.ListAddressFragmentOnFragmentChangedListener,
        MyOrdersFragment.myOrdersFragmentChangeListener,
        CartCountMVP.View, ViewCartFragment.OnFragmentInteractionListener, PaymentFragment.OnFragmentInteractionListener,
        SuggestionFragment.OnFragmentInteractionListener, ViewOrderFragment.OnFragmentInteractionListener*/{


    /*NavigationView navigationView;
    Toolbar toolbar;
    static TextView headerCustomerNameTV;
    static TextView headerCustomerEmailTV;

    static SessionManager sessionManager;
    private Utility myUtility;
    static Context mContext;
    private int selectedFragment = 0, customerId;
    private boolean initial = true;
    public static int count = 0;
    //private CartCountPresenter cartCountPresenter;
    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager();
        myUtility = new Utility();
        mContext = getApplicationContext();
        customerId = sessionManager.isLoggedIn(mContext);
       // cartCountPresenter = new CartCountPresenter(mContext, this);
        //callCartCountPresenter();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //displaySelectedScreen(R.id.nav_home, 0);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        updateToolbarTitle(getString(R.string.home));

        View hView = navigationView.getHeaderView(0);
        headerCustomerNameTV = (TextView) hView.findViewById(R.id.headerCustomerNameTV);
        headerCustomerEmailTV = (TextView) hView.findViewById(R.id.headerCustomerEmailTV);
        updateNavigationHeader();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                myUtility.hideKeyboard(MainActivity.this, drawerView);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
                if (AppConstants.debug)
                    Log.w(AppConstants.tag, "Tag name POP:::" + tag);
                changeTitleFromBackStackFragment(tag);
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem itemCart = menu.findItem(R.id.cartAction);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        BadgeUpdate.setBadgeCount(this, icon, count);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cartAction) {
            // Toast.makeText(mContext, "Clicked Cart...", Toast.LENGTH_SHORT).show();
            if (count > 0) {
                if (!(getCurrentFragment() instanceof ViewCartFragment)) {
                    if (AppConstants.debug)
                        Log.w(AppConstants.tag, "Currently not show ViewCartFragment");
                    selectedFragment = -1;
                    onFragmentChange(new ViewCartFragment(), "", getString(R.string.cart));
                }
            }
        } else if (id == R.id.homeAction) {
            if (!(getCurrentFragment() instanceof HomeFragment)) {
                if (AppConstants.debug)
                    Log.w(AppConstants.tag, "Currently not show ViewCartFragment");
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            }
        }else if (id==R.id.suggestionAction){
            if (!(getCurrentFragment() instanceof SuggestionFragment)) {
                if (AppConstants.debug)
                    Log.w(AppConstants.tag, "Currently not show ViewCartFragment");
                selectedFragment = -1;
                onFragmentChange(new SuggestionFragment(), "", getString(R.string.suggestion));
            }
        }else if (id==R.id.locationAction){
            if (!(getCurrentFragment() instanceof DeliveryLocation)) {
                onFragmentChange(new DeliveryLocation(), "", getString(R.string.location));
            }
        }
        
       *//* LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        BadgeUpdate.setBadgeCount(this, icon, 9);*//*


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        item.setChecked(true);
        if (id == R.id.nav_home) {
            // Handle the camera action
            if (AppConstants.debug)
                Log.w(AppConstants.tag, "111" + toolbar.getTitle());
            if (!(getCurrentFragment() instanceof HomeFragment)) {
                fragment = new HomeFragment();
                onFragmentChange(new HomeFragment(), "", getString(R.string.home));
            }
            initial = false;
        } else if (id == R.id.nav_account) {
            updateToolbarTitle(getString(R.string.my_account));
            if (!(getCurrentFragment() instanceof ProfileFragment)) {
                fragment = new ProfileFragment();
                onFragmentChange(new ProfileFragment(), "", getString(R.string.my_account));
            }
            initial = false;
        } else if (id == R.id.nav_history) {
            updateToolbarTitle(getString(R.string.history));
            if (!(getCurrentFragment() instanceof MyOrdersFragment)) {
                fragment = new DeliveryLocation();
                onFragmentChange(new MyOrdersFragment(), "", getString(R.string.history));
            }
            initial = false;
        } *//*else if (id == R.id.nav_location) {
            if (!(getCurrentFragment() instanceof DeliveryLocation)) {
                fragment = new DeliveryLocation();
                onFragmentChange(new DeliveryLocation(), "", getString(R.string.location));
            }
            initial = false;
        }*//* else if (id == R.id.add_address) {
            //if (!(getCurrentFragment() instanceof ListAddressFragment)) {
            onFragmentChange(new ListAddressFragment(), AppConstants.ADDRESS_LIST_EDIT_DELETE_KEY, getString(R.string.addressTitle));
            //}
            initial = false;
        } else if (id == R.id.nav_logout) {
            selectedFragment = 5;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (selectedFragment == 5) {
            sessionManager.logoutFromAccount(mContext);
            finish();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
        }
        return true;
    }

    private void replaceFragment(Fragment fragment, String parameter) {
        //replacing the fragment
        if (fragment != null) {
            if (!parameter.equals("")) {
                Bundle args = new Bundle();
                args.putString("parameter", parameter);
                fragment.setArguments(args);
            }
            String backStateName = fragment.getClass().getName();
            Log.w(AppConstants.tag, "Tag name PUSH:::" + backStateName);
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = false;
            if (backStateName.equals("com.valuestream.ecommerce.fragments.ListAddressFragment")) {
                fragmentPopped = false;
            } else {
                fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
            }
            if (!fragmentPopped) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        }
    }

    public static void updateNavigationHeader() {
        if (AppConstants.debug) Log.w(AppConstants.tag, "Called updateNavigationHeader()");
        headerCustomerNameTV.setText(sessionManager.getCustomerDisplayName(mContext).trim());
        headerCustomerEmailTV.setText(sessionManager.getValueFromSession(mContext, AppConstants.PROFILE_SESSION_NAME, AppConstants.CUSTOMER_EMAIL_KEY).trim());
    }


    @Override
    public void showError(String error) {

    }

    @Override
    public void showCartCount(int count) {
        MainActivity.count = count;
        invalidateOptionsMenu();
    }

   *//* private void callCartCountPresenter() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(AppConstants.CUSTOMER_ID_KEY, customerId);
            cartCountPresenter.getCartCont(AppConstants.CART_COUNT_API, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            myUtility.logErrors(mContext, customerId, e.getMessage(), 10006);
        }
    }*//*



    Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        return currentFragment;
    }

    @Override
    public void onFragmentChange(Fragment fragment, String parameter, String title) {
        updateToolbarTitle(title);
        replaceFragment(fragment, parameter);
    }

    private void updateToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void changeTitleFromBackStackFragment(String tag) {
        String title = "";
        switch (tag) {
            case "com.valuestream.ecommerce.fragments.AddEditAddressFragment":
                title = getString(R.string.editAddressString);
                break;
            case "com.valuestream.ecommerce.fragments.DeliveryLocation":
                title = getString(R.string.location);
                break;
            case "com.valuestream.ecommerce.fragments.HomeFragment":
                title = getString(R.string.home);
                break;
            case "com.valuestream.ecommerce.fragments.ListAddressFragment":
                title = getString(R.string.addressTitle);
                break;
            case "com.valuestream.ecommerce.fragments.MyOrdersFragment":
                title = getString(R.string.myOrder);
                break;
            case "com.valuestream.ecommerce.fragments.PaymentFragment":
                title = getString(R.string.paymentTitle);
                break;
            case "com.valuestream.ecommerce.fragments.ProfileFragment":
                title = getString(R.string.my_account);
                break;
            case "com.valuestream.ecommerce.fragments.SuggestionFragment":
                title = getString(R.string.suggestion);
                break;
            case "com.valuestream.ecommerce.fragments.ViewCartFragment":
                title = getString(R.string.cart);
                break;
            case "com.valuestream.ecommerce.fragments.ViewOrderFragment":
                title = getString(R.string.viewOrderString);
                break;


        }
        updateToolbarTitle(title);
    }*/
}
