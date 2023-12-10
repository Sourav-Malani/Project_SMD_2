package com.ass2.project_smd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class welcome extends AppCompatActivity {
    Button btn_facebook, btn_google;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView signIn;
    ImageButton btn_startWithEmailorPhone;

    private static final int RC_SIGN_IN = 123; // Use a unique request code
    private static final String TAG = "SignInActivity";
    private SignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_google = findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_startWithEmailorPhone = findViewById(R.id.btn_startWithEmailorPhone);

        btn_startWithEmailorPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome.this, signup.class);
                startActivity(intent);
            }
        });

        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the login page
                Intent intent = new Intent(welcome.this, Login.class);
                startActivity(intent);
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle(); // Call your sign-in method when the Google button is clicked
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign-In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // If the user is already signed in, navigate to the next activity
            navigateToNavBarActivity("google");
        }
    }

    // Your existing code for navigation
    void navigateToNavBarActivity(String signInMethod) {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("loginMethod", signInMethod);
        editor.apply();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DashboardFragment dashboardFragment = new DashboardFragment();


    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
        if(isLogged){
            Intent intent = new Intent(this, nav_bar.class);
            startActivity(intent);
            finish();
        }


    }


    // Modified Google Sign-In code
    private void signInWithGoogle() {
        signInClient = Identity.getSignInClient(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = gsc.getSignInIntent();
        signInIntent.putExtra("loginMethod", "google");
        startActivityForResult(signInIntent, RC_SIGN_IN);


        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("loginMethod", "google");
        editor.putBoolean("isLogged", true);
        editor.apply();
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }






    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                // Successfully signed in with Google, navigate to the next activity
                navigateToNavBarActivity("google");
            }
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            // Handle sign-in failure, e.g., show an error message
            //Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
        }
    }
}