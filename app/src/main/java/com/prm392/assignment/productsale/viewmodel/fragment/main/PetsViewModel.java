package com.prm392.assignment.productsale.viewmodel.fragment.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.PetService;
import com.prm392.assignment.productsale.model.pets.PetModel;
import com.prm392.assignment.productsale.model.pets.PetResponseModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class PetsViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Response<PetResponseModel>> petsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PetModel>> pets = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Response<Void>> updateResult = new MutableLiveData<>();
    private final MutableLiveData<Response<Void>> addResult = new MutableLiveData<>();
    private final MutableLiveData<Response<Void>> deleteResult = new MutableLiveData<>();

    public LiveData<Response<PetResponseModel>> getPetsLiveData() { return petsLiveData; }
    public LiveData<List<PetModel>> getPets() { return pets; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Response<Void>> getUpdateResult() { return updateResult; }
    public LiveData<Response<Void>> getAddResult() { return addResult; }
    public LiveData<Response<Void>> getDeleteResult() { return deleteResult; }

    public void loadPets(String token) {
        if (isLoading.getValue() != null && isLoading.getValue()) return;
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        PetService service = RetrofitClient.getMainInstance().create(PetService.class);
        disposables.add(
            service.getPets(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        petsLiveData.setValue(response);
                        if (response.isSuccessful() && response.body() != null) {
                            pets.setValue(response.body().getItems());
                        } else {
                            pets.setValue(null);
                            errorMessage.setValue("Failed to load pets");
                        }
                        isLoading.setValue(false);
                    },
                    throwable -> {
                        petsLiveData.setValue(null);
                        pets.setValue(null);
                        errorMessage.setValue("Network error: " + throwable.getMessage());
                        isLoading.setValue(false);
                    }
                )
        );
    }

    public void updatePet(String token, String id, PetModel pet) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        PetService service = RetrofitClient.getMainInstance().create(PetService.class);
        disposables.add(
            service.updatePet(token, id, pet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        updateResult.setValue(response);
                        isLoading.setValue(false);
                        if (response != null && response.isSuccessful()) {
                            loadPets(token);
                        }
                    },
                    throwable -> {
                        errorMessage.setValue("Update failed: " + throwable.getMessage());
                        isLoading.setValue(false);
                    }
                )
        );
    }

    public void addPet(String token, PetModel pet) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        PetService service = RetrofitClient.getMainInstance().create(PetService.class);
        disposables.add(
            service.addPet(token, pet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        addResult.setValue(response);
                        isLoading.setValue(false);
                        if (response != null && response.isSuccessful()) {
                            loadPets(token);
                        }
                    },
                    throwable -> {
                        errorMessage.setValue("Add failed: " + throwable.getMessage());
                        isLoading.setValue(false);
                    }
                )
        );
    }

    public void deletePet(String token, String id) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        PetService service = RetrofitClient.getMainInstance().create(PetService.class);
        disposables.add(
            service.deletePet(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        deleteResult.setValue(response);
                        isLoading.setValue(false);
                        if (response != null && response.isSuccessful()) {
                            loadPets(token);
                        }
                    },
                    throwable -> {
                        errorMessage.setValue("Delete failed: " + throwable.getMessage());
                        isLoading.setValue(false);
                    }
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 