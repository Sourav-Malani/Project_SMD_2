package com.ass2.NavBarFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.StarterScreens.signup;
import com.ass2.config.Config;
import com.ass2.project_smd.R;
import com.ass2.project_smd.nav_bar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DelivaryAddressFragment extends Fragment {

    TextView fullNameBox;
    ImageButton backDelivaryAddress;
    EditText phoneNoBox,addressBox;
    Button saveButton;
    String email;
    private OkHttpClient client = new OkHttpClient();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_delivary_address, container, false);
        fullNameBox = root.findViewById(R.id.fullNameBox);
        phoneNoBox = root.findViewById(R.id.phoneNoBox);
        addressBox = root.findViewById(R.id.addressBox);
        backDelivaryAddress = root.findViewById(R.id.backDelivaryAddress);
        saveButton = root.findViewById(R.id.saveButton);

        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", requireActivity().MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
        String loginMethod = sharedPrefs.getString("loginMethod", "");


        if (isLogged && loginMethod.equals("email")) {
            email = sharedPrefs.getString("email", "");
            fetchAndDisplayUserData(email);
        } else if (isLogged && loginMethod.equals("google")) {
            //GoogleSignInAccount recievedAccount = GoogleSignIn.getLastSignedInAccount(requireActivity());
            //fetchUserDataFromGoogleAndUpdateUI(recievedAccount);
        }

        backDelivaryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), nav_bar.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullNameBox.getText().toString();
                String addr = addressBox.getText().toString();
                String phno = phoneNoBox.getText().toString();
                if(!addr.isEmpty() && !phno.isEmpty() && !name.isEmpty())
                    updateUserAddressAndPhone(email,name,addr,phno);
                else
                    Toast.makeText(requireActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
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
                                handleResponse(responseData);
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

    private void handleResponse(String response) {
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
            if(userData.getString("full_name").isEmpty()){
                fullNameBox.setText("No Name");
            }
            else{
                fullNameBox.setText(userData.getString("full_name"));
            }
            if(userData.getString("phone_no").isEmpty()){
                phoneNoBox.setText("No Phone Number");
            }
            else{
                phoneNoBox.setText(userData.getString("phone_no"));
            }
            if(userData.getString("delivery_address").isEmpty()){
                addressBox.setText("No Address");
            }
            else{
                addressBox.setText(userData.getString("delivery_address"));
            }
        } catch (JSONException e) {
            if(!userData.has("delivery_address")){
                addressBox.setText("No Address");
            }
            else if(!userData.has("phone_no")){
                phoneNoBox.setText("No Phone Number");
            }
            else if(!userData.has("full_name")){
                fullNameBox.setText("No Name");
            }
            else{
                e.printStackTrace();
                displayError("Error updating UI"+e.getMessage());

            }
        }
    }

    private void displayError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void updateUserAddressAndPhone(String email,String name,String addr, String phno) {
        // Prepare data for sending
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("email", email);
        postDataParams.put("full_name",name);
        postDataParams.put("delivery_address", addr);
        postDataParams.put("phone_no", phno);
//        postDataParams.put("image_url", imageURL);

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("http://192.168.18.114/Ass02API/register.php"); // Use your IP and path
                    URL url = new URL(Config.API_BASE_URL + "update_user_data.php");

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
                    Log.d("DeliveryAddressActivity", "Response Code: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line;

                        while ((line = in.readLine()) != null) {
                            if (!line.contains("<br />")) {
                                sb.append(line);
                            }
                            if(line.contains("success")){
                                sb.append(line);
                                break;
                            }
                        }



                        in.close();
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(requireActivity(), sb.toString(), Toast.LENGTH_LONG).show();
                                if (sb.toString().contains("success")) {
                                    Toast.makeText(requireActivity(), "Successfully updated", Toast.LENGTH_LONG).show();
                                    SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", requireActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    editor.putString("full_name", name);
                                    editor.putString("delivery_address", addr);
                                    editor.putString("phone_no", phno);
                                    editor.apply();
                                    //go back (pop) to the previous fragment
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                }
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireActivity(), "Failed to update", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("DeliveryAddressActivity", "Exception: " + e.getMessage());
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


}