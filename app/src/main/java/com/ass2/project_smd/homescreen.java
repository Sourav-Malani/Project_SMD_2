//package com.ass2.project_smd;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ass2.Adapters.MainAdapter;
//import com.ass2.Models.MainModel;
//import com.ass2.project_smd.databinding.ActivityHomescreenBinding;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class homescreen extends AppCompatActivity {
//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;
//
//    TextView address;
//    ImageView profilePic;
//
//    private static final int RC_SIGN_IN = 123; // Use a unique request code
//    private static final String TAG = "SignInActivity";
//
//    ActivityHomescreenBinding binding;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityHomescreenBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        ArrayList<MainModel> list = new ArrayList<>();
//        list.add(new MainModel(
//                R.drawable.pizza_image,
//                "Create Your Own Pizza",
//                "$ 10",
//                "Choose From Our Options Of Designa And Make Your Own Pizza.",
//                "pizza",
//                1));
//
//        list.add(new MainModel(
//                R.drawable.pizza2,
//                "Fresh Farm House",
//                "Rs 1400",
//                "crisp capsicum, succulent mushrooms and fresh tomatoes",
//                "pizza",
//                2));
//        list.add(new MainModel(
//                R.drawable.pizza3,
//                "Peppy Paneer",
//                "Rs. 5900",
//                "Chunky paneer with crisp capsicum and spicy red pepperr",
//                "pizza",
//                3));
//        list.add(new MainModel(
//                R.drawable.pizza4,
//                "Mexican Green Wave",
//                "Rs. 1400",
//                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
//                "pizza",
//                4));
//        list.add(new MainModel(
//                R.drawable.pizza5,
//                "Peppy Paneer",
//                "$ 15",
//                "Chunky paneer with crisp capsicum and spicy red pepper",
//                "pizza",
//                5));
//        list.add(new MainModel(
//                R.drawable.pizza6,
//                "Mexican Green Wave",
//                "Rs 1700",
//                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
//                "pizza",
//                6));
//        list.add(new MainModel(
//                R.drawable.pizza6,
//                "Mexican Green Wave",
//                "Rs 1700",
//                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
//                "pizza",
//                7));
//        list.add(new MainModel(
//                R.drawable.pizza6,
//                "Mexican Green Wave",
//                "Rs 1700",
//                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
//                "pizza",
//                8));
//        list.add(new MainModel(
//                R.drawable.pizza6,
//                "Mexican Green Wave",
//                "Rs 1700",
//                "A pizza loaded with crunchy onions, crisp capsicum, juicy tomatoes",
//                "pizza",
//                9));
//
//        MainAdapter adapter = new MainAdapter(list, this);
//        binding.recyclerViewCards.setAdapter(adapter);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 columns
//        binding.recyclerViewCards.setLayoutManager(layoutManager);
//
//
//        address = findViewById(R.id.address);
//        profilePic = findViewById(R.id.profile_image);
//
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this,gso);
//
//        Intent intent = getIntent();
//        GoogleSignInAccount receivedAccount = intent.getParcelableExtra("googleSignInAccount");
//        if(receivedAccount != null){
//            updateUI(receivedAccount);
//        }
//
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // [START on_start_sign_in]
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//        // [END on_start_sign_in]
//    }
//    // [START onActivityResult]
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//    // [END onActivityResult]
//
//    // [START handleSignInResult]
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
//    // [END handleSignInResult]
//    void signOut(){
//        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                finish();
//                startActivity(new Intent(homescreen.this,welcome.class));
//            }
//        });
//    }
//    private void updateUI(@Nullable GoogleSignInAccount account) {
//        if (account != null) {
//            //String personEmail = acct.getEmail();
//            address.setText(account.getGivenName());
//            if (account.getPhotoUrl() != null) {
//                String photoUrl = account.getPhotoUrl().toString();
//
//                // Load the image using an image loading library like Picasso or Glide
//                // For example, using Picasso:
//                Picasso.get().load(photoUrl).into(profilePic);
//
//                // If you want to download and display the image manually, you can use:
//                /* new DownloadImageTask(yourImageView).execute(photoUrl); */
//            }
//
//
//        } else {
//        }
//    }
//}