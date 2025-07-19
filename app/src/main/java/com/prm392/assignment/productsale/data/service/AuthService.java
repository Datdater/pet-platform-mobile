package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.ChangePasswordModel;
import com.prm392.assignment.productsale.model.ProfileResponseModel;
import com.prm392.assignment.productsale.model.SignInModel;
import com.prm392.assignment.productsale.model.SignUpModel;
import com.prm392.assignment.productsale.model.UserResponseModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {
    @Headers({"client: mobile"})
    @POST("Auth/login")
    Observable<Response<UserResponseModel>> signIn(@Body SignInModel signInModel);

    @Headers({"client: mobile"})
    @POST("Auth/register")
    Observable<Response<UserResponseModel>> signUp(@Body SignUpModel signUpModel);
    @Headers({"client: mobile"})
    @GET("Profile")
    Observable<Response<ProfileResponseModel>> getProfile(@Header("Authorization") String token);
    @Headers({"client: mobile"})
    @PUT("Profile/update-password")
    Observable<Response<BaseResponseModel>> changePassword(@Header("Authorization") String token, @Body ChangePasswordModel changePasswordModel);
}
