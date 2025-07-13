package com.prm392.assignment.productsale.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.CustomerService;
import com.prm392.assignment.productsale.data.service.ProductSaleService;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.CreateAddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;
import com.prm392.assignment.productsale.model.cart.CartModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerRepository {

    private final Retrofit mainClient;


    // Headers
    private static final String AUTHORIZATION = "Authorization";
    public CustomerRepository() {
        mainClient = RetrofitClient.getMainInstance();
    }
    public LiveData<Response<GetAllAddressResponseModel>> getCustomerAddress(String token) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(CustomerService.class)
                        .getCustomer(token)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();

                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));

                            return Response.error(BaseResponseModel.FAILED_REQUEST_FAILURE, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<GetAllAddressResponseModel>> createAddress(String token, CreateAddressModel model) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(CustomerService.class)
                        .createAddress(token, model)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();

                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));

                            return Response.error(BaseResponseModel.FAILED_REQUEST_FAILURE, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<GetAllAddressResponseModel>> updateAddress(String token, String id, CreateAddressModel model) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(CustomerService.class)
                        .updateAddress(token, id, model)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();

                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));

                            return Response.error(BaseResponseModel.FAILED_REQUEST_FAILURE, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

    public LiveData<Response<GetAllAddressResponseModel>> deleteAddress(String token, String id) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(CustomerService.class)
                        .deleteAddress(token, id)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(exception -> {
                            exception.printStackTrace();

                            if (exception.getClass() == HttpException.class)
                                return Response.error(((HttpException) exception).code(), ResponseBody.create(null, ""));

                            return Response.error(BaseResponseModel.FAILED_REQUEST_FAILURE, ResponseBody.create(null, ""));
                        })
                        .toFlowable(BackpressureStrategy.LATEST)
        );
    }

}
