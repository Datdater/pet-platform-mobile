package com.prm392.assignment.productsale.data.remote;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String mainClientUrl = "https://prn-222.food/api/v1/";
    private static Retrofit mainClient;
    private static final String TAG = "RetrofitClient";

    private RetrofitClient() {
    }

    public static Retrofit getMainInstance() {
        if (mainClient == null) {
            Log.d(TAG, "Creating Retrofit instance with base URL: " + mainClientUrl);

            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("OkHttp", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttpClient with logging and timeouts
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            mainClient = new Retrofit.Builder()
                    .baseUrl(mainClientUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

            Log.d(TAG, "Retrofit instance created successfully");
        }

        return mainClient;
    }
}