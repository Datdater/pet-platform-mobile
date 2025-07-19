package com.prm392.assignment.productsale.view.fragment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.model.ChangePasswordModel;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.viewmodel.fragment.main.ProfileViewModel;

public class PasswordChangeDialog extends DialogFragment {

    private View rootView;
    private ProfileViewModel viewModel;
    private String token;
    private PasswordChangeListener listener;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnCancel;
    private Button btnConfirm;

    public interface PasswordChangeListener {
        void onPasswordChangeSuccess();
        void onPasswordChangeFailed(String message);
    }

    public static PasswordChangeDialog newInstance() {
        return new PasswordChangeDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PasswordChangeListener) {
            listener = (PasswordChangeListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ProfileViewModel();
        token = UserAccountManager.getToken(getContext(), UserAccountManager.TOKEN_TYPE_BEARER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_password_change, container, false);

        // Initialize views
        etOldPassword = rootView.findViewById(R.id.et_old_password);
        etNewPassword = rootView.findViewById(R.id.et_new_password);
        etConfirmPassword = rootView.findViewById(R.id.et_confirm_password);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnConfirm = rootView.findViewById(R.id.btn_confirm);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (oldPassword.isEmpty()) {
            etOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            return;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        // Show loading
        btnConfirm.setEnabled(false);
        btnConfirm.setText("Đang xử lý...");

        ChangePasswordModel changePasswordModel = new ChangePasswordModel(oldPassword, newPassword);

        viewModel.changePassword(token, changePasswordModel).observe(getViewLifecycleOwner(), response -> {
            btnConfirm.setEnabled(true);
            btnConfirm.setText("Xác nhận");

            if (response != null && response.isSuccessful() && response.body() != null) {
                BaseResponseModel result = response.body();

                // Kiểm tra response code thay vì status
                if (response.code() == BaseResponseModel.SUCCESSFUL_OPERATION) {
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onPasswordChangeSuccess();
                    }
                    dismiss();
                } else {
                    String errorMessage = "Đổi mật khẩu thất bại";
                    if (listener != null) {
                        listener.onPasswordChangeFailed(errorMessage);
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            } else {
                String errorMessage = "Có lỗi xảy ra, vui lòng thử lại";
                if (response != null && response.code() == BaseResponseModel.FAILED_AUTH) {
                    errorMessage = "Mật khẩu cũ không đúng";
                }

                if (listener != null) {
                    listener.onPasswordChangeFailed(errorMessage);
                }
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}