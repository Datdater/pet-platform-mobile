package com.prm392.assignment.productsale.viewmodel.fragment.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.prm392.assignment.productsale.data.remote.RetrofitClient;
import com.prm392.assignment.productsale.data.service.ProductSaleService;
import com.prm392.assignment.productsale.model.services.ServiceDetailResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicePageViewModel extends ViewModel {
    private final MutableLiveData<ServiceDetailResponseModel> serviceDetail = new MutableLiveData<>();

    public LiveData<ServiceDetailResponseModel> getServiceDetail() {
        return serviceDetail;
    }

    public void fetchServiceDetail(String token, String serviceId) {
        RetrofitClient.getMainInstance()
                .create(ProductSaleService.class)
                .getService(token, serviceId)
                .enqueue(new Callback<ServiceDetailResponseModel>() {
                    @Override
                    public void onResponse(Call<ServiceDetailResponseModel> call, Response<ServiceDetailResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            serviceDetail.setValue(response.body());
                        } else {
                            serviceDetail.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDetailResponseModel> call, Throwable t) {
                        serviceDetail.setValue(null);
                    }
                });
    }

    public static final ViewModelInitializer<ServicePageViewModel> initializer = new ViewModelInitializer<>(
            ServicePageViewModel.class,
            creationExtras -> new ServicePageViewModel()
    );
} 