package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.CreateAddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.Response;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerService {
    @Headers({"client: mobile"})
    @GET("customers/address")
    Observable<Response<GetAllAddressResponseModel>> getCustomer(@Header("Authorization") String token);
    @Headers({"client: mobile"})
    @POST("customers/address")
    Observable<Response<GetAllAddressResponseModel>> createAddress(@Header("Authorization") String token, @Body CreateAddressModel model);
    @Headers({"client: mobile"})
    @PUT("customers/address/{id}")
    Observable<Response<GetAllAddressResponseModel>> updateAddress(@Header("Authorization") String token, @Path("id") String id, @Body CreateAddressModel model);

    @Headers({"client: mobile"})
    @DELETE("customers/address/{id}")
    Observable<Response<GetAllAddressResponseModel>> deleteAddress(@Header("Authorization") String token, @Path("id") String id);

}
