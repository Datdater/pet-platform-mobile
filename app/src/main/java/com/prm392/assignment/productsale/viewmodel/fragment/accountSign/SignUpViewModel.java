package com.prm392.assignment.productsale.viewmodel.fragment.accountSign;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.prm392.assignment.productsale.data.repository.AuthRepository;
import com.prm392.assignment.productsale.model.SignUpModel;
import com.prm392.assignment.productsale.model.UserResponseModel;

import retrofit2.Response;

public class SignUpViewModel extends ViewModel {
    private final AuthRepository authRepository;

    public SignUpViewModel() {
        super();
        authRepository = new AuthRepository();
    }

    public LiveData<Response<UserResponseModel>> signUp(String name, String email, String password, String phoneNumber) {
        SignUpModel signUpModel = new SignUpModel();
        signUpModel.setName(name);
        signUpModel.setEmail(email);
        signUpModel.setPassword(password);
        signUpModel.setPhoneNumber(phoneNumber);

        return authRepository.signUp(signUpModel);
    }
    public LiveData<Response<Void>> sendEmailConfirmation(String email) {
        return authRepository.sendEmailConfirmation(email);
    }
}