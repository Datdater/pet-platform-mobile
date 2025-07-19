package com.prm392.assignment.productsale.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import android.util.Log;
import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.LocationService;
import com.prm392.assignment.productsale.model.location.LocationResponseModel;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationRepository {
    private final Retrofit locationClient;
    private static final String LOCATION_BASE_URL = "https://open.oapi.vn/location/";

    public LocationRepository() {
        // Create a separate Retrofit instance for location API
        locationClient = RetrofitClient.createInstance(LOCATION_BASE_URL);
        Log.d("LocationRepository", "Created with base URL: " + LOCATION_BASE_URL);
    }

    public LiveData<Response<LocationResponseModel>> getProvinces(int size) {
        Log.d("LocationRepository", "Loading provinces with size: " + size);
        return LiveDataReactiveStreams.fromPublisher(
                locationClient.create(LocationService.class)
                        .getProvinces(size)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            Log.e("LocationRepository", "Error loading provinces", exception);
                            exception.printStackTrace();
                            if (exception.getClass() == HttpException.class) {
                                HttpException httpException = (HttpException) exception;
                                Log.e("LocationRepository", "HTTP Error: " + httpException.code() + " - " + httpException.message());
                                return Response.error(httpException.code(), ResponseBody.create(null, httpException.message()));
                            }
                            return Response.error(500, ResponseBody.create(null, exception.getMessage()));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<LocationResponseModel>> getDistricts(String provinceId, int size) {
        return LiveDataReactiveStreams.fromPublisher(
                locationClient.create(LocationService.class)
                        .getDistricts(provinceId, size)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();
                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));
                            return Response.error(500, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<LocationResponseModel>> getWards(String districtId, int size) {
        return LiveDataReactiveStreams.fromPublisher(
                locationClient.create(LocationService.class)
                        .getWards(districtId, size)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();
                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));
                            return Response.error(500, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }
} 