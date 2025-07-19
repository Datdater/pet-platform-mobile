package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.location.LocationResponseModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationService {
    @GET("provinces")
    Observable<Response<LocationResponseModel>> getProvinces(@Query("size") int size);
    
    @GET("districts/{provinceId}")
    Observable<Response<LocationResponseModel>> getDistricts(@retrofit2.http.Path("provinceId") String provinceId, @Query("size") int size);
    
    @GET("wards/{districtId}")
    Observable<Response<LocationResponseModel>> getWards(@retrofit2.http.Path("districtId") String districtId, @Query("size") int size);
} 