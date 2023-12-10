package com.ass2.HttpService;

import android.content.Context;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHelper {

    private HttpService httpService;
    private Context context;

    public ApiHelper(HttpService httpService, Context context) {
        this.httpService = httpService;
        this.context = context;
    }

//    public void registerUser(String name, String email, String password,String delivery_address,String PhNo,String imgURL, final ApiCallback<UserProfileModel> callback) {
//        Call<UserProfileModel> call = httpService.callRegisterApi(
//                RequestBody.create(MediaType.parse("text/plain"), name),
//                RequestBody.create(MediaType.parse("text/plain"), email),
//                RequestBody.create(MediaType.parse("text/plain"), password)
//                ,RequestBody.create(MediaType.parse("text/plain"), delivery_address)
//                ,RequestBody.create(MediaType.parse("text/plain"), PhNo)
//                ,RequestBody.create(MediaType.parse("text/plain"), imgURL)
//        );
//
//        call.enqueue(new Callback<UserProfileModel>() {
//            @Override
//            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
//                if (response.isSuccessful()) {
//                    UserProfileModel userProfile = response.body();
//                    callback.onSuccess(userProfile);
//                } else {
//                    callback.onError("Registration failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfileModel> call, Throwable t) {
//                callback.onError("Network error: " + t.getMessage());
//            }
//        });
//    }
//
//    public void loginUser(String email, String password, final ApiCallback<UserProfileModel> callback) {
//        Call<UserProfileModel> call = httpService.callLoginApi(
//                RequestBody.create(MediaType.parse("text/plain"), email),
//                RequestBody.create(MediaType.parse("text/plain"), password)
//        );
//
//        call.enqueue(new Callback<UserProfileModel>() {
//            @Override
//            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
//                if (response.isSuccessful()) {
//                    UserProfileModel userProfile = response.body();
//                    callback.onSuccess(userProfile);
//                } else {
//                    callback.onError("Login failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfileModel> call, Throwable t) {
//                callback.onError("Network error: " + t.getMessage());
//            }
//        });
//    }

    // Define similar methods for other API requests here
}
