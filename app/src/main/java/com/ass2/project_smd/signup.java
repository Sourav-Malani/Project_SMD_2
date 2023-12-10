package com.ass2.project_smd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ass2.HttpService.ApiCallback;
import com.ass2.HttpService.ApiHelper;
import com.ass2.HttpService.HttpService;
import com.ass2.HttpService.RetrofitBuilder;
import com.ass2.HttpService.UserProfileModel;
import com.ass2.Models.UserModel;
import com.ass2.config.Config;
import com.ass2.project_smd.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signup extends AppCompatActivity {

    TextView loginRedirectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signUpButton = findViewById(R.id.signUpButton);
        loginRedirectText = findViewById(R.id.txt_loginRedirect);
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String fullName = ((EditText) findViewById(R.id.usernameText)).getText().toString();
                String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

                // Create a new UserModel with signup information

                // Get user input from EditText fields
                String deliveryAddress = "addr"; // Get delivery address input
                String phoneNo = "phn"; // Get phone number input
                String imageURL = "imgURL"; // Get image URL input

                // Create a new UserModel with signup information
                //UserProfileModel user = new UserProfileModel(fullName, email, password, "addr", "phn", "imgURL");
                registerUser(fullName, email, password);
                // Register the user using the ApiHelper
            }
        });
    }

    private void registerUser(String fullname, String email, String password) {
        // Prepare data for sending
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("full_name", fullname);
        postDataParams.put("email", email);
        postDataParams.put("pass", password);
//        postDataParams.put("address", address);
//        postDataParams.put("phone_no", phno);
//        postDataParams.put("image_url", imageURL);

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("http://192.168.18.114/Ass02API/register.php"); // Use your IP and path
                    URL url = new URL(Config.API_BASE_URL + "register.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("SignupActivity", "Response Code: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line;

                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }

                        in.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(signup.this, sb.toString(), Toast.LENGTH_LONG).show();
                                if (sb.toString().contains("successfully")) {
                                    navigateToLogin();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(signup.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("SignupActivity", "Exception: " + e.getMessage());
                }
            }
        }).start();
    }

    private String getPostDataString(HashMap<String, String> params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(signup.this, Login.class);
        startActivity(intent);
    }


}
