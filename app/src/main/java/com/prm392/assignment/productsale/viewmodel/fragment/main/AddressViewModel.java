package com.prm392.assignment.productsale.viewmodel.fragment.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prm392.assignment.productsale.data.repository.CustomerRepository;
import com.prm392.assignment.productsale.data.repository.LocationRepository;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.CreateAddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;
import com.prm392.assignment.productsale.model.location.LocationModel;
import com.prm392.assignment.productsale.model.location.LocationResponseModel;
import com.prm392.assignment.productsale.util.UserAccountManager;

import java.util.ArrayList;

import retrofit2.Response;

public class AddressViewModel extends ViewModel {
    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<AddressModel> selectedAddress;
    private final MutableLiveData<ArrayList<LocationModel>> provinces;
    private final MutableLiveData<ArrayList<LocationModel>> districts;
    private final MutableLiveData<ArrayList<LocationModel>> wards;

    public AddressViewModel() {
        customerRepository = new CustomerRepository();
        locationRepository = new LocationRepository();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
        selectedAddress = new MutableLiveData<>();
        provinces = new MutableLiveData<>(new ArrayList<>());
        districts = new MutableLiveData<>(new ArrayList<>());
        wards = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<AddressModel> getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressModel address) {
        selectedAddress.setValue(address);
    }

    public LiveData<Response<GetAllAddressResponseModel>> getAddresses() {
        isLoading.setValue(true);
        String token = UserAccountManager.getToken(null, UserAccountManager.TOKEN_TYPE_BEARER);
        return customerRepository.getCustomerAddress(token);
    }

    public LiveData<Response<GetAllAddressResponseModel>> createAddress(CreateAddressModel model) {
        isLoading.setValue(true);
        String token = UserAccountManager.getToken(null, UserAccountManager.TOKEN_TYPE_BEARER);
        return customerRepository.createAddress(token, model);
    }

    public LiveData<Response<GetAllAddressResponseModel>> updateAddress(String id, CreateAddressModel model) {
        isLoading.setValue(true);
        String token = UserAccountManager.getToken(null, UserAccountManager.TOKEN_TYPE_BEARER);
        return customerRepository.updateAddress(token, id, model);
    }

    public LiveData<Response<GetAllAddressResponseModel>> deleteAddress(String id) {
        isLoading.setValue(true);
        String token = UserAccountManager.getToken(null, UserAccountManager.TOKEN_TYPE_BEARER);
        return customerRepository.deleteAddress(token, id);
    }

    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }

    public void setError(String error) {
        errorMessage.setValue(error);
    }

    public void clearError() {
        errorMessage.setValue(null);
    }

    public boolean isSuccessfulResponse(Response<?> response) {
        return response != null && response.code() == BaseResponseModel.SUCCESSFUL_OPERATION;
    }

    public boolean isAuthError(Response<?> response) {
        return response != null && response.code() == BaseResponseModel.FAILED_AUTH;
    }

    public boolean isRequestFailure(Response<?> response) {
        return response != null && response.code() == BaseResponseModel.FAILED_REQUEST_FAILURE;
    }

    // Location methods
    public LiveData<ArrayList<LocationModel>> getProvinces() {
        return provinces;
    }

    public LiveData<ArrayList<LocationModel>> getDistricts() {
        return districts;
    }

    public LiveData<ArrayList<LocationModel>> getWards() {
        return wards;
    }

    public void loadProvinces() {
        locationRepository.getProvinces(1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                provinces.setValue(response.body().getData());
            }
        });
    }

    public void loadDistricts(String provinceId) {
        if (provinceId == null || provinceId.isEmpty()) {
            districts.setValue(new ArrayList<>());
            wards.setValue(new ArrayList<>());
            return;
        }
        
        locationRepository.getDistricts(provinceId, 1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                districts.setValue(response.body().getData());
                wards.setValue(new ArrayList<>()); // Clear wards when district changes
            }
        });
    }

    public void loadWards(String districtId) {
        if (districtId == null || districtId.isEmpty()) {
            wards.setValue(new ArrayList<>());
            return;
        }
        
        locationRepository.getWards(districtId, 1000).observeForever(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                wards.setValue(response.body().getData());
            }
        });
    }
} 