package com.prm392.assignment.productsale.view.fragment.accountSign;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.databinding.FragmentSignUpBinding;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.util.DialogsProvider;
import com.prm392.assignment.productsale.util.SharedPrefManager;
import com.prm392.assignment.productsale.util.TextFieldValidator;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.view.activity.AccountSign;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.viewmodel.fragment.accountSign.SignUpViewModel;

import org.json.JSONObject;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding vb;
    private SignUpViewModel viewModel;
    private static final String TAG = "SignUpFragment";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vb = FragmentSignUpBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vb = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((AccountSign) getActivity()).isBackButtonVisible()) {
            ((AccountSign) getActivity()).setTitle(getString(R.string.Sign_Up));
            ((AccountSign) getActivity()).setBackButton(true);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        // Name validation (changed from username)
        vb.signUpUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    // Simple name validation - at least 2 characters
                    if (editable.toString().trim().length() >= 2)
                        vb.signUpUsername.setError(null);
                    else
                        vb.signUpUsername.setError(getString(R.string.Name_too_short));
                } else vb.signUpUsername.setError(null);
            }
        });

        // Email validation
        vb.signUpEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (TextFieldValidator.isValidEmail(editable.toString()))
                        vb.signUpEmail.setError(null);
                    else
                        vb.signUpEmail.setError(getString(R.string.Email_not_complete_or_not_valid));
                } else vb.signUpEmail.setError(null);
            }
        });

        // Phone validation
        vb.signUpPhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (TextFieldValidator.isValidPhone(editable.toString()))
                        vb.signUpPhone.setError(null);
                    else
                        vb.signUpPhone.setError(getString(R.string.Phone_not_valid));
                } else vb.signUpPhone.setError(null);
            }
        });

        // Address validation (optional field)
        vb.signUpAddress.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (TextFieldValidator.isValidAddress(editable.toString()))
                        vb.signUpAddress.setError(null);
                    else
                        vb.signUpAddress.setError(getString(R.string.Address_not_valid));
                } else vb.signUpAddress.setError(null);
            }
        });

        // Password validation
        vb.signUpPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (TextFieldValidator.isValidPassword(editable.toString()))
                        vb.signUpPassword.setError(null);
                    else vb.signUpPassword.setError(getString(R.string.Password_not_valid));
                } else vb.signUpPassword.setError(null);

                // Also check confirm password when password changes
                if (vb.signUpPasswordConfirm.getEditText().getText().length() > 0) {
                    if (!vb.signUpPasswordConfirm.getEditText().getText().toString().equals(editable.toString()))
                        vb.signUpPasswordConfirm.setError(getString(R.string.Not_matching_the_password));
                    else vb.signUpPasswordConfirm.setError(null);
                } else vb.signUpPasswordConfirm.setError(null);
            }
        });

        // Password confirmation validation
        vb.signUpPasswordConfirm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (!editable.toString().equals(vb.signUpPassword.getEditText().getText().toString()))
                        vb.signUpPasswordConfirm.setError(getString(R.string.Not_matching_the_password));
                    else vb.signUpPasswordConfirm.setError(null);
                } else vb.signUpPasswordConfirm.setError(null);
            }
        });

        // Sign up button click listener
        vb.signUpButton.setOnClickListener(button -> {
            if (isDataValid()) signUp();
        });
    }


    boolean isDataValid() {
        boolean validData = true;

        // Check password confirmation
        if (vb.signUpPasswordConfirm.getError() != null || vb.signUpPasswordConfirm.getEditText().getText().length() == 0) {
            vb.signUpPasswordConfirm.requestFocus();
            vb.signUpPasswordConfirm.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fieldmissing));
            validData = false;
        }

        // Check password
        if (vb.signUpPassword.getError() != null || vb.signUpPassword.getEditText().getText().length() == 0) {
            vb.signUpPassword.requestFocus();
            vb.signUpPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fieldmissing));
            validData = false;
        }

        // Check email
        if (vb.signUpEmail.getError() != null || vb.signUpEmail.getEditText().getText().length() == 0) {
            vb.signUpEmail.requestFocus();
            vb.signUpEmail.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fieldmissing));
            validData = false;
        }

        // Check name (changed from username)
        if (vb.signUpUsername.getError() != null || vb.signUpUsername.getEditText().getText().length() == 0) {
            vb.signUpUsername.requestFocus();
            vb.signUpUsername.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fieldmissing));
            validData = false;
        }

        // Check phone
        if (vb.signUpPhone.getError() != null || vb.signUpPhone.getEditText().getText().length() == 0) {
            vb.signUpPhone.requestFocus();
            vb.signUpPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fieldmissing));
            validData = false;
        }

        return validData;
    }

    void signUp() {
        // Show loading dialog
        DialogsProvider.get(getActivity()).setLoading(true);

        // Log the data being sent
        String name = vb.signUpUsername.getEditText().getText().toString();
        String email = vb.signUpEmail.getEditText().getText().toString();
        String password = vb.signUpPassword.getEditText().getText().toString();
        String phone = vb.signUpPhone.getEditText().getText().toString();

        Log.d(TAG, "Starting sign up with:");
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Phone: " + phone);

        // Call ViewModel to perform sign up (updated parameters)
        viewModel.signUp(name, email, password, phone)
                .observe(getViewLifecycleOwner(), response -> {
                    Log.d(TAG, "Received response with code: " + response.code());

                    // Hide loading dialog
                    DialogsProvider.get(getActivity()).setLoading(false);

                    // Handle different response codes
                    switch (response.code()) {
                        case BaseResponseModel.SUCCESSFUL_OPERATION: // 200
                        case BaseResponseModel.SUCCESSFUL_CREATION: // 201
                            // Registration successful - email verification required
                            Log.d(TAG, "Registration successful with code: " + response.code());

                            String userEmail = vb.signUpEmail.getEditText().getText().toString();

                            // Gọi API gửi email confirm
                            viewModel.sendEmailConfirmation(userEmail)
                                    .observe(getViewLifecycleOwner(), emailResponse -> {
                                        if (emailResponse.isSuccessful()) {
                                            Log.d(TAG, "Email confirmation sent successfully");
                                        } else {
                                            Log.e(TAG, "Failed to send email confirmation: " + emailResponse.code());
                                        }
                                    });

                            // Show success dialog
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                            builder.setTitle("Registration Successful")
                                    .setMessage("We've sent a verification link to:\n" + userEmail + "\n\nPlease check your email and click the link to activate your account.")
                                    // Trong dialog success callback
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        // Set flag để hiển thị popup khi về trang Sign In
                                        SharedPrefManager.get(getContext()).setJustRegistered(true);

                                        // Navigate back to sign in
                                        if (getActivity() instanceof AccountSign) {
                                            getActivity().onBackPressed();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                            break;

                        case BaseResponseModel.FAILED_INVALID_DATA: // 400
                            // Invalid data - try to get detailed error message
                            String message = "Invalid registration data";
                            try {
                                if (response.errorBody() != null) {
                                    String errorJson = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(errorJson);
                                    if (jsonObject.has("message")) {
                                        message = jsonObject.getString("message");
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response", e);
                            }
                            DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Register_failed), message);
                            break;

                        case BaseResponseModel.FAILED_DATA_CONFLICT: // 409
                            // User already exists
                            DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Sign_Up_Failed), "Email already exists. Please use a different email or sign in.");
                            break;

                        case BaseResponseModel.FAILED_REQUEST_FAILURE: // 504
                            // Network error
                            DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Request_Error), getString(R.string.Please_Check_your_connection));
                            break;

                        default:
                            // Other server errors
                            Log.e(TAG, "Unexpected response code: " + response.code());
                            DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Server_Error), getString(R.string.Code) + response.code());
                    }
                });
    }
}