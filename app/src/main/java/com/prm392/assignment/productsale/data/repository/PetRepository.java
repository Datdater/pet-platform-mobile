package com.prm392.assignment.productsale.data.repository;

import com.prm392.assignment.productsale.data.service.PetService;
import com.prm392.assignment.productsale.model.pets.PetModel;
import com.prm392.assignment.productsale.model.pets.PetResponseModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public class PetRepository {
    private final PetService petService;

    public PetRepository(PetService petService) {
        this.petService = petService;
    }

    public Observable<Response<PetResponseModel>> getPets(String token) {
        return petService.getPets(token);
    }

    public Observable<List<PetModel>> getPetsList(String token) {
        return petService.getPets(token)
                .map(response -> {
                    if (response.isSuccessful() && response.body() != null) {
                        return response.body().getItems();
                    } else {
                        throw new RuntimeException("Failed to get pets: " + response.code());
                    }
                });
    }

    public Observable<Response<Void>> updatePet(String token, String id, PetModel pet) {
        return petService.updatePet(token, id, pet);
    }

    public Observable<Response<Void>> addPet(String token, PetModel pet) {
        return petService.addPet(token, pet);
    }

    public Observable<Response<Void>> deletePet(String token, String id) {
        return petService.deletePet(token, id);
    }
} 