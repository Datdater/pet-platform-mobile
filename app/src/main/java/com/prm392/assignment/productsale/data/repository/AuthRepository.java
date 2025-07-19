package com.prm392.assignment.productsale.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.AuthService;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.SignInModel;
import com.prm392.assignment.productsale.model.SignUpModel;
import com.prm392.assignment.productsale.model.UserResponseModel;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthRepository {

    private final Retrofit mainClient;

    public AuthRepository() {
        mainClient = RetrofitClient.getMainInstance();
    }

    public LiveData<Response<UserResponseModel>> signIn(SignInModel signInModel) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(AuthService.class)
                        .signIn(signInModel)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            Log.e(TAG, "SignIn Error: " + exception.getMessage(), exception);
                            ResponseBody responseBody = ResponseBody.create(
                                    MediaType.get("application/json"), "");

                            if (exception instanceof HttpException) {
                                return Response.error(((HttpException) exception).code(), responseBody);
                            }

                            return Response.error(
                                    BaseResponseModel.FAILED_REQUEST_FAILURE, responseBody);
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<UserResponseModel>> signUp(SignUpModel signUpModel) {
        Log.d(TAG, "SignUp attempt with data: " + signUpModel.toString());

        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(AuthService.class)
                        .signUp(signUpModel)
                        .subscribeOn(Schedulers.io())
                        .map(response -> {
                            Log.d(TAG, "Raw response code: " + response.code());

                            if (response.isSuccessful()) {
                                // Handle successful response even if body is null
                                if (response.body() == null) {
                                    // Create a success response with empty UserResponseModel
                                    UserResponseModel emptyUserResponse = new UserResponseModel();
                                    return Response.success(emptyUserResponse);
                                } else {
                                    return response;
                                }
                            } else {
                                return response;
                            }
                        })
                        .onErrorReturn(exception -> {
                            Log.e(TAG, "SignUp Error: " + exception.getMessage(), exception);

                            // Check if it's a JSON parsing error (empty body)
                            if (exception.getMessage() != null &&
                                    exception.getMessage().contains("End of input")) {
                                Log.d(TAG, "Empty response body detected, treating as success");
                                UserResponseModel emptyUserResponse = new UserResponseModel();
                                return Response.success(emptyUserResponse);
                            }

                            ResponseBody responseBody = ResponseBody.create(
                                    MediaType.get("application/json"), "");

                            if (exception instanceof HttpException) {
                                Log.e(TAG, "HTTP Error Code: " + ((HttpException) exception).code());
                                return Response.error(((HttpException) exception).code(), responseBody);
                            }

                            Log.e(TAG, "Network/Connection Error");
                            return Response.error(
                                    BaseResponseModel.FAILED_REQUEST_FAILURE, responseBody);
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }
    public LiveData<Response<Void>> sendEmailConfirmation(String email) {
        Log.d(TAG, "Sending email confirmation to: " + email);

        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(AuthService.class)
                        .sendEmailConfirmation(email)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            Log.e(TAG, "SendEmailConfirmation Error: " + exception.getMessage(), exception);
                            ResponseBody responseBody = ResponseBody.create(
                                    MediaType.get("application/json"), "");

                            if (exception instanceof HttpException) {
                                return Response.error(((HttpException) exception).code(), responseBody);
                            }

                            return Response.error(
                                    BaseResponseModel.FAILED_REQUEST_FAILURE, responseBody);
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<ProfileResponseModel>> getProfile(String token) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(AuthService.class)
                        .getProfile(token)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            MediaType mediaType = MediaType.parse("application/json");
                            ResponseBody responseBody = ResponseBody.create(mediaType, "");

                            if (exception instanceof HttpException) {
                                return Response.error(((HttpException) exception).code(), responseBody);
                            }

                            return Response.error(
                                    BaseResponseModel.FAILED_REQUEST_FAILURE, responseBody);
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<BaseResponseModel>> changePassword(String token, ChangePasswordModel changePasswordModel) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(AuthService.class)
                        .changePassword(token, changePasswordModel)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            MediaType mediaType = MediaType.parse("application/json");
                            ResponseBody responseBody = ResponseBody.create(mediaType, "");

                            if (exception instanceof HttpException) {
                                return Response.error(((HttpException) exception).code(), responseBody);
                            }

                            return Response.error(
                                    BaseResponseModel.FAILED_REQUEST_FAILURE, responseBody);
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }
}
