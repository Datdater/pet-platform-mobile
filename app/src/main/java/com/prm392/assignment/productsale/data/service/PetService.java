package com.prm392.assignment.productsale.data.service;

import com.prm392.assignment.productsale.model.pets.PetResponseModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.DELETE;

public interface PetService {
    @Headers({"client: mobile"})
    @GET("pets")
    Observable<Response<PetResponseModel>> getPets(@Header("Authorization") String token);

    @Headers({"client: mobile"})
    @PUT("pets/{id}")
    Observable<Response<Void>> updatePet(
        @Header("Authorization") String token,
        @Path("id") String id,
        @Body com.prm392.assignment.productsale.model.pets.PetModel pet
    );

    @Headers({"client: mobile"})
    @POST("pets")
    Observable<Response<Void>> addPet(
        @Header("Authorization") String token,
        @Body com.prm392.assignment.productsale.model.pets.PetModel pet
    );

    @Headers({"client: mobile"})
    @DELETE("pets/{id}")
    Observable<Response<Void>> deletePet(
        @Header("Authorization") String token,
        @Path("id") String id
    );
}
