package com.ass2.project_smd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.config.Config;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyProfileFragment extends Fragment {

    TextView editName, editEmail, phoneNo;
    ImageButton backBtn;
    ImageView profileImage, editProfileImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_profile, container, false);
        editName = root.findViewById(R.id.usernameText);
        editEmail = root.findViewById(R.id.emailText);
        phoneNo = root.findViewById(R.id.phText);
        backBtn = root.findViewById(R.id.backButton);
        profileImage = root.findViewById(R.id.profileImage);
        editProfileImage = root.findViewById(R.id.selectImageButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), nav_bar.class);
                startActivity(intent);
            }
        });

        // Call the method to fetch and display user data
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userPrefs", getActivity().MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
        String loginMethod = sharedPrefs.getString("loginMethod", "");
        //String email = sharedPrefs.getString("email", "");

        if(isLogged && loginMethod.equals("email")){
            String email = sharedPrefs.getString("email", "");
            fetchAndDisplayUserData(email);
        }
        else if(isLogged && loginMethod.equals("google")){
            fetchUserDataFromGoogle();
        }

        return root;
    }

    private void fetchAndDisplayUserData(String em) {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("email", em);

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Config.API_BASE_URL + "get_user_data.php");

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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleLoginResponse(sb.toString());
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
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
                changeUI(jsonResponse);
                //navigateToHome();
            } else {
                String message = jsonResponse.getString("message");
                displayError(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // Print the response for debugging purposes
            Log.e("JSON Parsing Error", "Error parsing response: " + response);
            displayError("Error parsing response");
        }
    }

    private void changeUI(JSONObject userData) throws JSONException {
            //editor.putBoolean("isLogged", true);
            //editor.putString("name", userData.getString("name"));
            //editor.putString("email", userData.getString("email"));
            //editor.putString("phone", userData.getString("phone"));

            editName.setText(userData.getString("full_name"));
            editEmail.setText(userData.getString("email"));
            phoneNo.setText(userData.getString("phone_no"));

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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void fetchUserDataFromGoogle(){

    }
}
