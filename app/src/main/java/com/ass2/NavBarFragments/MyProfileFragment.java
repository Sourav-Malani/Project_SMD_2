package com.ass2.NavBarFragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.HttpService.FileUtils;
import com.ass2.HttpService.HttpService;
import com.ass2.HttpService.RetrofitBuilder;
import com.ass2.HttpService.UserProfileModel;
import com.ass2.config.Config;
import com.ass2.project_smd.R;
import com.ass2.project_smd.nav_bar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;

public class MyProfileFragment extends Fragment {

    private static final int PROFILE_PHOTO_REQUEST_CODE = 300;
    private TextView editName, editEmail, phoneNo;
    private ImageButton backBtn,editProfileImage;
    private ImageView profileImage;
    private OkHttpClient client = new OkHttpClient();
    private String  profilePhotoUrl = "",email= "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        editName = root.findViewById(R.id.usernameText);
        editEmail = root.findViewById(R.id.emailText);
        phoneNo = root.findViewById(R.id.phText);
        backBtn = root.findViewById(R.id.backButton);
        profileImage = root.findViewById(R.id.profileImage);
        editProfileImage = root.findViewById(R.id.selectImageButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SignInActivity", "Profile Picture URL: " + profilePhotoUrl);
                Intent intent = new Intent(requireActivity(), nav_bar.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", requireActivity().MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
        String loginMethod = sharedPrefs.getString("loginMethod", "");
        if(loginMethod.equals("email")) {
            editProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requirePermission();
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PROFILE_PHOTO_REQUEST_CODE); // Define PROFILE_PHOTO_REQUEST_CODE
                }
            });
        }
        else if(loginMethod.equals("google")){
            editProfileImage.setVisibility(View.GONE);
        }

        if (isLogged && loginMethod.equals("email")) {
            email = sharedPrefs.getString("email", "");
            //profilePhotoUrl = sharedPrefs.getString("image_url", "");
            fetchAndDisplayUserData(email);
        } else if (isLogged && loginMethod.equals("google")) {
            GoogleSignInAccount recievedAccount = GoogleSignIn.getLastSignedInAccount(requireActivity());
            fetchUserDataFromGoogleAndUpdateUI(recievedAccount);
        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                 if (requestCode == PROFILE_PHOTO_REQUEST_CODE) {
                     profileImage.setImageURI(selectedImageUri);
                     profilePhotoUrl = FileUtils.getPath(getContext(), selectedImageUri);
                    //saveImageUrlsToSharedPreferences();
                    // Upload the profile photo
                    uploadFileToServer(profilePhotoUrl);
                }
            }
        }
    }

    private void saveImageUrlsToSharedPreferences(String imgURL) {
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("image_url", imgURL);
        editor.apply();
    }

    public void uploadFileToServer(String url) {
        File file = new File(Uri.parse(url).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage", file.getName(), requestBody);

        // Add user email and image type to the request
        //RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), imageType);
        // Add user email to the request
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        HttpService service = RetrofitBuilder.getClient().create(HttpService.class);

        Call<UserProfileModel> call = service.callUploadApi(filePart, emailBody);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, retrofit2.Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel fileModel = response.body();
                    //Toast.makeText(getContext(), fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), fileModel.getImageURL(), Toast.LENGTH_SHORT).show();
                    String imgURL = fileModel.getImageURL();
                    // Update the profilePhotoUrl in SharedPreferences after uploading
                    //profilePhotoUrl = response.body().imageUrl; // Assuming your UserProfileModel contains the new image URL

                    // Save the updated URL in SharedPreferences
                    saveImageUrlsToSharedPreferences(imgURL);

//                    UserProfileModel fileModel = response.body();
//                    Toast.makeText(getContext(), fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the response body is null
                    Toast.makeText(getContext(), "Response is not successful or is empty", Toast.LENGTH_SHORT).show();
                    if (response.errorBody() != null) {
                        try {
                            // Log or display the error body for debugging
                            String errorString = response.errorBody().string();
                            Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                if (t instanceof IOException) {
                    // Handle network or server error
                    Toast.makeText(getContext(), "Network or server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (t instanceof HttpException) {
                    // Handle HTTP errors
                    retrofit2.Response<?> response = ((HttpException) t).response();
                    try {
                        if (response != null && response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(getContext(), "Server response: " + errorBody, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle other types of errors
                    Toast.makeText(getContext(), "Unknown error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchAndDisplayUserData(String email) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Use OkHttpClient for making HTTP requests
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, "email=" + email);
                    Request request = new Request.Builder()
                            .url(Config.API_BASE_URL + "get_user_data.php")
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleLoginResponse(responseData);
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayError("Failed");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayError("IO Error");
                        }
                    });
                }
            }
        });
    }

    private void handleLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                changeUI(jsonResponse);
            } else {
                String message = jsonResponse.getString("message");
                displayError(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Print the response for debugging purposes
            displayError("Error parsing response");
        }
    }

    private void changeUI(JSONObject userData) {
        try {
            editName.setText(userData.getString("full_name"));
            editEmail.setText(userData.getString("email"));
            phoneNo.setText(userData.getString("phone_no"));
            if (userData.has("image_url")) {
                profilePhotoUrl = userData.getString("image_url");

                SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("image_url", profilePhotoUrl);
                Log.d("SignInActivity", "Profile Picture URL: " + profilePhotoUrl);
                editor.apply();
                Picasso.get()
                        .load(profilePhotoUrl)
                        .rotate(90) // Adjust the rotation angle as needed
                        .into(profileImage);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            displayError("Error updating UI");
        }
    }

    public void requirePermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{READ_EXTERNAL_STORAGE},1);
    }
    private void displayError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void fetchUserDataFromGoogleAndUpdateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            editName.setText(account.getDisplayName());
            editEmail.setText(account.getEmail());
            Picasso.get().load(account.getPhotoUrl().toString()).into(profileImage);
            phoneNo.setText("N/A");
        }
    }
}
