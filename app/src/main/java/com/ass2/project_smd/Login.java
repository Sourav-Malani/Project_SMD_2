package com.ass2.project_smd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ass2.HttpService.ApiCallback;
import com.ass2.HttpService.ApiHelper;
import com.ass2.HttpService.HttpService;
import com.ass2.HttpService.RetrofitBuilder;
import com.ass2.HttpService.UserProfileModel;
import com.ass2.config.Config;
import com.ass2.project_smd.R;
import com.ass2.project_smd.nav_bar;

import org.json.JSONObject;

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

public class Login extends AppCompatActivity {

//    private ApiHelper apiHelper;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupRedirectText = findViewById(R.id.txt_signup);
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignup();
            }
        });

        // Initialize ApiHelper with the base URL of your API
//        HttpService httpService = RetrofitBuilder.getClient().create(HttpService.class);
//        apiHelper = new ApiHelper(httpService, this);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

                // Call the login method using the ApiHelper
                loginUser(email, password);

                //start the nav_bar activity and pass all data obtained from server to it

            }
        });
    }

    private void loginUser(String email, String password) {


        if (validateEmail(email) && validatePassword(password)) {
            attemptLogin(email, password);
        } else {
            displayError("Please enter valid credentials.");
        }
    }

    private boolean validateEmail(String val) {

        if (val.isEmpty()) {
            ((EditText) findViewById(R.id.emailText)).setError("Email cannot be empty");
            return false;
        } else {
            ((EditText) findViewById(R.id.emailText)).setError(null);
            return true;
        }
    }

    private boolean validatePassword(String val) {
        if (val.isEmpty()) {
            ((EditText) findViewById(R.id.passwordEditText)).setError("Password cannot be empty");
            return false;
        } else {
            ((EditText) findViewById(R.id.passwordEditText)).setError(null);
            return true;
        }
    }

    private void attemptLogin(String email, String password) {
        // Prepare data for sending
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("email", email);
        postDataParams.put("password", password);

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("http://192.168.18.114/Ass02API/login.php"); // Use your IP and path
                    URL url = new URL(Config.API_BASE_URL + "login.php");

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
                                handleLoginResponse(sb.toString());
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayError("Login failed");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                // Store user data in SharedPreferences
                setSharedPreference(jsonResponse);
                navigateToHome();
            } else {
                String message = jsonResponse.getString("message");
                displayError(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            displayError("Error parsing response");
        }
    }

    private void setSharedPreference(JSONObject userData) {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("loginMethod", "email");
        editor.putString("name", userData.optString("name"));
        editor.putString("email", userData.optString("email"));



        try {
            editor.putBoolean("isLogged", true);
            editor.putString("name", userData.getString("name"));
            editor.putString("email", userData.getString("email"));
            editor.putString("delivery_address", userData.getString("delivery_address"));


            // Add other user data as needed
            // Store profile and cover photo URLs if available
            if (userData.has("image_url")) {
                editor.putString("image_url", userData.getString("image_url"));
            }
            if (userData.has("delivery_address")) {
                editor.putString("delivery_address", userData.getString("delivery_address"));
            }
            if (userData.has("phone")) {
                editor.putString("phone", userData.getString("phone"));
            }


            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void displayError(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {

        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("loginMethod", "email");
        editor.putString("name", sharedPrefs.getString("name", ""));
        editor.putString("email", sharedPrefs.getString("email", ""));
        editor.apply();

            // Navigate to the Home activity
        Intent intent = new Intent(Login.this, nav_bar.class);
        intent.putExtra("loginMethod", "email");
        startActivity(intent);
        finish();
    }

    private void navigateToSignup() {
        Intent intent = new Intent(Login.this, signup.class);
        startActivity(intent);
    }

    private void navigateToNavBarActivity(UserProfileModel result){
        // Create an Intent to start the NavBarActivity
        Intent intent = new Intent(Login.this, nav_bar.class);

        // Pass the user data obtained from the server to the NavBarActivity
        intent.putExtra("userFullName", result.getFullName());
        intent.putExtra("userEmail", result.getEmail());
        intent.putExtra("userPassword", result.getPassword());
        intent.putExtra("userDeliveryAddress", result.getDeliveryAddress());
        intent.putExtra("userPhoneNo", result.getPhoneNo());
        intent.putExtra("userImageURL", result.getImageURL());


        // Start the NavBarActivity
        startActivity(intent);

    }
}
