package com.prm392.assignment.productsale.viewmodel.fragment.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.prm392.assignment.productsale.data.repository.AuthRepository;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.ChangePasswordModel;
import com.prm392.assignment.productsale.model.UserModel;
import com.prm392.assignment.productsale.model.UserResponseModel;
import com.prm392.assignment.productsale.model.ProfileResponseModel;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public ProfileViewModel() {
        super();
        authRepository = new AuthRepository();
    }

    public LiveData<Response<ProfileResponseModel>> getProfile(String token) {
        return authRepository.getProfile(token);
    }

    public LiveData<Response<BaseResponseModel>> changePassword(String token, ChangePasswordModel changePasswordModel) {
        return authRepository.changePassword(token, changePasswordModel);
    }
}