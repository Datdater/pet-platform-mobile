package com.prm392.assignment.productsale.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.OrderService;
import com.prm392.assignment.productsale.data.service.ProductSaleService;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.cart.AddProductCartModel;
import com.prm392.assignment.productsale.model.orders.CreateOrderModel;
import com.prm392.assignment.productsale.model.orders.CreateOrderResponseModel;
import com.prm392.assignment.productsale.model.orders.OrdersResponseModel;
import com.prm392.assignment.productsale.model.orders.UpdatePaymentModel;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderRepository {

    private static final String AUTHORIZATION = "Authorization";
    private final Retrofit mainClient;

    public OrderRepository() {
        mainClient = RetrofitClient.getMainInstance();
    }

    public LiveData<Response<CreateOrderResponseModel>> createOrder(String token, CreateOrderModel model) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(OrderService.class)
                        .createOrder(token, model)
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

    public LiveData<Response<OrdersResponseModel>> getOrders(String token, Integer pageNumber, Integer pageSize) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(OrderService.class)
                        .getOrders(token, pageNumber, pageSize)
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

    public LiveData<Response<BaseResponseModel>> createPayment(String token, UpdatePaymentModel model) {
        return LiveDataReactiveStreams.fromPublisher(
                mainClient.create(OrderService.class)
                        .createPayment(token, model)
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
