package com.ass2.HttpService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HttpService {

    @Multipart
    @POST("upload_profile_pic.php")
    Call<UserProfileModel> callUploadApi(@Part MultipartBody.Part image,
                                         @Part("userEmail") RequestBody email,
                                         @Part("imageType") RequestBody imageType);

    @Multipart
    @POST("update_profile.php") // Replace with the actual API endpoint for updating the profile
    Call<UserProfileModel> callUpdateProfile(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("register.php")
    Call<UserProfileModel> callRegisterApi(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("delivery_address") RequestBody delivery_address,
            @Part("PhNo") RequestBody PhNo,
            @Part("imgURL") RequestBody imgURL


    );

    @Multipart
    @POST("login.php")
    Call<UserProfileModel> callLoginApi(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );


}