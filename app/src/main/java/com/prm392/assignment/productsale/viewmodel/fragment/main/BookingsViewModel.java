package com.prm392.assignment.productsale.viewmodel.fragment.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.BookingService;
import com.prm392.assignment.productsale.model.services.BookingResponseModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class BookingsViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Response<BookingResponseModel>> bookingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> hasMoreData = new MutableLiveData<>(true);
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;

    public LiveData<Response<BookingResponseModel>> getBookingsLiveData() { return bookingsLiveData; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getHasMoreData() { return hasMoreData; }
    public int getCurrentPage() { return currentPage; }

    public void loadBookings(String token, boolean reset) {
        if (isLoading.getValue() != null && isLoading.getValue()) return;
        isLoading.setValue(true);
        if (reset) currentPage = 1;
        BookingService service = RetrofitClient.getMainInstance().create(BookingService.class);
        disposables.add(
            service.getBookings(token, currentPage, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        bookingsLiveData.setValue(response);
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            hasMoreData.setValue(response.body().getItems().size() >= PAGE_SIZE);
                        } else {
                            hasMoreData.setValue(false);
                        }
                    },
                    throwable -> {
                        bookingsLiveData.setValue(null);
                        isLoading.setValue(false);
                        hasMoreData.setValue(false);
                    }
                )
        );
    }

    public void loadMoreBookings(String token) {
        if (hasMoreData.getValue() != null && !hasMoreData.getValue()) return;
        currentPage++;
        loadBookings(token, false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 