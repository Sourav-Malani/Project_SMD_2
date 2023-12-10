package com.ass2.project_smd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ass2.Helper.User;
import com.ass2.Helper.UserDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen  extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000); // Display splash screen for 5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Check if the user is logged in already.
                    SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
                    String loginMethod = sharedPrefs.getString("loginMethod", "");

                    Intent intent = null;
                    if (isLogged && loginMethod.equals("google")) { // If the user is logged in already.
                        final String[] userEmail = {""}; // Final variable to store email

                        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
                        userDatabaseHelper.getUserData(mAuth.getCurrentUser().getEmail(), new UserDatabaseHelper.UserDataCallback() {
                            @Override
                            public void onUserDataReceived(User userData) {
                                userEmail[0] = userData.getEmail();
                            }

                            @Override
                            public void onUserDataError(String error) {
                                // Handle error if necessary
                            }
                        });

                        // Start the new activity inside this callback
                        intent = new Intent(SplashScreen.this, nav_bar.class);
                        intent.putExtra("userEmail", userEmail[0]);
                    }
                    else if(loginMethod.equals("email")){
                        intent = new Intent(SplashScreen.this, nav_bar.class);

                    }
                    else { // If the user is not logged in.
                        intent = new Intent(SplashScreen.this, welcome.class);
                    }

                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }}
