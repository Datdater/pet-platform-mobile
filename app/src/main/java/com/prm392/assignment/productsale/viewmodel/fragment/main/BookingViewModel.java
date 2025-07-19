package com.prm392.assignment.productsale.viewmodel.fragment.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.repository.PetRepository;
import com.prm392.assignment.productsale.data.service.PetService;
import com.prm392.assignment.productsale.data.service.BookingService;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.pets.PetModel;
import com.prm392.assignment.productsale.model.pets.PetResponseModel;
import com.prm392.assignment.productsale.model.services.BookingRequestModel;
import android.util.Log;
import retrofit2.Response;
import okhttp3.ResponseBody;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.google.gson.Gson;

public class BookingViewModel extends ViewModel {
    private final PetRepository petRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    private final MutableLiveData<List<PetModel>> petsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Response<BaseResponseModel>> bookingResponse = new MutableLiveData<>();
    private final MutableLiveData<String> bookingResult = new MutableLiveData<>();

    public BookingViewModel(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public LiveData<List<PetModel>> getPets(String token) {
        loadPets(token);
        return petsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> createBooking(String token, BookingRequestModel model) {
        BookingService bookingService = RetrofitClient.getMainInstance().create(BookingService.class);
        disposables.add(
            bookingService.createBooking(token, model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response != null) {
                            Log.d("BookingViewModel", "Status code: " + response.code());
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String id = response.body().string(); // Lấy chuỗi GUID
                                    Log.d("BookingViewModel", "Booking success, id: " + id);
                                    bookingResult.setValue(id);
                                } catch (Exception e) {
                                    Log.e("BookingViewModel", "Error reading response body", e);
                                    bookingResult.setValue(null);
                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    Log.d("BookingViewModel", "Error body: " + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("BookingViewModel", "Error reading errorBody", e);
                                }
                                bookingResult.setValue(null);
                            } else {
                                bookingResult.setValue(null);
                            }
                        } else {
                            bookingResult.setValue(null);
                        }
                    },
                    throwable -> {
                        Log.e("BookingViewModel", "API booking failed: " + throwable.getMessage(), throwable);
                        bookingResult.setValue(null);
                    }
                )
        );
        return bookingResult;
    }

    private void loadPets(String token) {
        Log.d("BookingViewModel", "Loading pets with token: " + token);
        isLoading.setValue(true);
        disposables.add(
            petRepository.getPetsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    pets -> {
                        Log.d("BookingViewModel", "Pets loaded successfully. Count: " + (pets != null ? pets.size() : "null"));
                        petsLiveData.setValue(pets);
                        isLoading.setValue(false);
                    },
                    throwable -> {
                        Log.e("BookingViewModel", "API call failed: " + throwable.getMessage(), throwable);
                        errorMessage.setValue("Không thể tải danh sách thú cưng: " + throwable.getMessage());
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

    // Factory for creating ViewModel with dependencies
    public static class Factory implements ViewModelProvider.Factory {
        private final PetRepository petRepository;

        public Factory(PetRepository petRepository) {
            this.petRepository = petRepository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(BookingViewModel.class)) {
                return (T) new BookingViewModel(petRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    // Static initializer for easy access
    public static final ViewModelProvider.Factory initializer = new Factory(
        new PetRepository(RetrofitClient.getMainInstance().create(PetService.class))
    );
} 