package com.ass2.project_smd;


import static androidx.core.text.SpannableStringBuilderKt.color;

import static com.ass2.project_smd.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.Drawer.DrawerAdapter;
import com.ass2.Drawer.DrawerItem;
import com.ass2.Drawer.SimpleItem;
import com.ass2.Drawer.SpaceItem;
import com.ass2.Helper.CartDBHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class nav_bar extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, DashboardFragment.DashboardListener {

    private static final int POS_MY_ORDERS = 0;
    private static final int POS_MY_PROFILE = 1;
    private static final int POS_DELIVERY_ADDRESS = 2;
    private static final int POS_PAYMENT_METHODS = 3;
    private static final int POS_CONTACT_US= 4;
    private static final int POS_SETTINGS = 5;
    private static final int POS_HELP = 6;
    private static final int POS_LOGOUT = 7;


    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav  slidingRootNav;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    ImageView drawerProfilePic;
    TextView drawerUserEmail, drawerUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout.activity_nav_bar);
        //profilePic = findViewById(R.id.profile_image);
        //user_name = findViewById(R.id.user_name);
        //user_email = findViewById(R.id.user_email);
        //ImageView vectorImageView = view.findViewById(R.id.vectorImageView);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(layout.drawer_menu)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_MY_ORDERS).setChecked(true),
                createItemFor(POS_MY_PROFILE),
                createItemFor(POS_DELIVERY_ADDRESS),
                createItemFor(POS_PAYMENT_METHODS),
                createItemFor(POS_CONTACT_US),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_HELP),
                new SpaceItem(48),
                createItemForLogout(POS_LOGOUT)));
        adapter.setListener(this);


        //View otherLayout = getLayoutInflater().inflate(layout.drawer_menu, null);//"Fixed here"

        // Find the RecyclerView from the inflated layout
        @SuppressLint("MissingInflatedId") RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_MY_ORDERS);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Retrieve the GoogleSignInAccount object from the intent
        //GoogleSignInAccount recievedAccount = getIntent().getParcelableExtra("googleSignInAccount");
        GoogleSignInAccount recievedAccount = GoogleSignIn.getLastSignedInAccount(this);

        drawerUserName = slidingRootNav.getLayout().findViewById(id.user_name);
        drawerUserEmail = slidingRootNav.getLayout().findViewById(id.user_email);
        drawerProfilePic = slidingRootNav.getLayout().findViewById(id.profile_image);

        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String LOGIN_METHOD = sharedPrefs.getString("loginMethod", "");
        Boolean isLogged = sharedPrefs.getBoolean("isLogged", false);

        // Now you can use the 'account' object as needed
        if (recievedAccount != null && LOGIN_METHOD.equals("google") && isLogged) {
            updateUI(recievedAccount);
        }
        else if(LOGIN_METHOD.equals("email") && isLogged){
            drawerUserName.setText(sharedPrefs.getString("name", ""));
            drawerUserEmail.setText(sharedPrefs.getString("email", ""));
        }



    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(color.gray))
                .withTextTint(color(color.black))
                .withSelectedIconTint(color(color.gray))
                .withSelectedTextTint(color(color.black));
    }
    private DrawerItem createItemForLogout(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position],1)
                .withIconTint(color(color.orange))
                .withTextTint(color(color.black))
                .withSelectedIconTint(color(color.gray))
                .withSelectedTextTint(color(color.black));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++){
            int id = ta.getResourceId(i, 0);
            if(id != 0){
                icons[i] = ContextCompat.getDrawable(this,id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DashboardFragment dashboardFragment = new DashboardFragment();
        //transaction.replace(R.id.container, dashboardFragment);


        // Set the listener for DashboardFragment
        if(position == POS_MY_ORDERS){
            dashboardFragment.attachDashboardListener(this);
            transaction.replace(R.id.container, dashboardFragment);
        }
        else if(position == POS_MY_PROFILE){
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            transaction.replace(R.id.container, myProfileFragment);
        }
        else if(position == POS_DELIVERY_ADDRESS){
            NearbyResFragment nearbyResFragment = new NearbyResFragment();
            transaction.replace(R.id.container, nearbyResFragment);
        }
        else if(position == POS_PAYMENT_METHODS){
            SettingsFragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.container, settingsFragment);
        }
        else if(position == POS_CONTACT_US){
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            transaction.replace(R.id.container, aboutUsFragment);
        }
        else if(position == POS_SETTINGS){
            SettingsFragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.container, settingsFragment);
        }
        else if(position == POS_HELP){
            SettingsFragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.container, settingsFragment);
        }
        else if(position == 8){//POS_LOGOUT
            signOut();
        }
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void openMenu() {
        // Implement the logic to open the sliding root navigation menu here
        // For example:
        if (slidingRootNav != null) {
            slidingRootNav.openMenu(true);
        }
    }
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {

            if (drawerUserName != null) {
                drawerUserName.setText(account.getDisplayName());
            }
            if (drawerUserEmail != null) {
                drawerUserEmail.setText(account.getEmail());
            }
            if (drawerProfilePic != null) {
                if (account.getPhotoUrl() != null) {
                    String photoUrl = account.getPhotoUrl().toString();

                    // Load the image using an image loading library like Picasso or Glide
                    // For example, using Picasso:
                    Picasso.get().load(photoUrl).into(drawerProfilePic);

                    // If you want to download and display the image manually, you can use:
                    /* new DownloadImageTask(yourImageView).execute(photoUrl); */
                }
            }


        } else {
        }
    }

    private void signOut() {
        // Clear cart data
        CartDBHelper dbHelper = new CartDBHelper(this);
        dbHelper.clearCart();
        nagivateToSignInActivity("null");

    }

    void nagivateToSignInActivity(String signInMethod) {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("loginMethod", signInMethod);
        editor.putBoolean("isLogged", false);
        editor.apply();

        Intent intent = new Intent(this, welcome.class);
        startActivity(intent);
        finish();
    }

}