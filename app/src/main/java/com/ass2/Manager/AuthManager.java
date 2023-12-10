package com.ass2.Manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ass2.HttpService.ApiCallback;
import com.ass2.HttpService.ApiHelper;
import com.ass2.HttpService.HttpService;
import com.ass2.HttpService.RetrofitBuilder;
import com.ass2.HttpService.UserProfileModel;
import com.ass2.config.Config;
import android.content.SharedPreferences;


public class AuthManager {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "AuthManager";
    private GoogleSignInClient googleSignInClient;
    private Activity activity;
    private SignInClient signInClient;
    private ApiHelper apiHelper;

    public AuthManager(Activity activity) {
        this.activity = activity;
        initGoogleSignInClient();
        initApiHelper();
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    private void initApiHelper() {
        HttpService httpService = RetrofitBuilder.getClient().create(HttpService.class);
        apiHelper = new ApiHelper(httpService, activity);
    }

    public void signInWithGoogle() {
        signInClient = Identity.getSignInClient(activity);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInIntent.putExtra("loginMethod", "google");
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void handleGoogleSignInResult(int requestCode, int resultCode, Intent data, OnCompleteListener<GoogleSignInAccount> onCompleteListener) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            task.addOnCompleteListener(activity, onCompleteListener);
        }
    }

    public void signOutGoogle(OnCompleteListener<Void> onCompleteListener) {
        googleSignInClient.signOut()
                .addOnCompleteListener(activity, onCompleteListener);
    }


    // You can add similar methods for other sign-in providers like Facebook or your server.

    //SERVER:

}
