package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;
import com.prm392.assignment.productsale.model.orders.CreateOrderModel;
import com.prm392.assignment.productsale.model.orders.CreateOrderResponseModel;
import com.prm392.assignment.productsale.model.orders.OrdersResponseModel;
import com.prm392.assignment.productsale.model.orders.UpdatePaymentModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderService {
    @Headers({"client: mobile"})
    @POST("orders")
    Observable<Response<CreateOrderResponseModel>> createOrder(@Header("Authorization") String token, @Body CreateOrderModel model);
    @Headers({"client: mobile"})
    @GET("orders")
    Observable<Response<OrdersResponseModel>> getOrders(@Header("Authorization") String token, @Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize);

    @Headers({"client: mobile"})
    @PUT("payment")
    Observable<Response<BaseResponseModel>> createPayment(@Header("Authorization") String token, @Body UpdatePaymentModel model);

}
