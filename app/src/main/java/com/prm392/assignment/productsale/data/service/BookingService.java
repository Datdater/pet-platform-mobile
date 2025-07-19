package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.services.BookingRequestModel;
import com.prm392.assignment.productsale.model.services.BookingResponseModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.POST;

public interface BookingService {
    @Headers({"client: mobile"})
    @POST("booking")
    Observable<Response<ResponseBody>> createBooking(@Header("Authorization") String token, @Body BookingRequestModel model);

    @Headers({"client: mobile"})
    @GET("booking")
    Observable<Response<BookingResponseModel>> getBookings(@Header("Authorization") String token, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);
} 