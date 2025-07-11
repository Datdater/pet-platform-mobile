package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.Response;
import io.reactivex.rxjava3.core.Observable;

public interface CustomerService {
    @Headers({"client: mobile"})
    @GET("customers/address")
    Observable<Response<GetAllAddressResponseModel>> getCustomer(@Header("Authorization") String token);
}
