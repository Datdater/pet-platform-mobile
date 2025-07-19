package com.prm392.assignment.productsale.view.fragment.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.adapters.CheckoutListAdapter;
import com.prm392.assignment.productsale.databinding.FragmentCheckoutPageBinding;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.UserModel;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.cart.CartItemModel;
import com.prm392.assignment.productsale.model.cart.CartModel;
import com.prm392.assignment.productsale.model.services.ServiceModel;
import com.prm392.assignment.productsale.util.DialogsProvider;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.view.activity.PaymentNotification;
import com.prm392.assignment.productsale.viewmodel.fragment.main.CheckoutPageViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.view.LineChartView;
//import vn.zalopay.sdk.Environment;
//import vn.zalopay.sdk.ZaloPaySDK;


public class CheckoutPageFragment extends Fragment {
    private FragmentCheckoutPageBinding vb;
    private CheckoutPageViewModel viewModel;

    private CheckoutListAdapter adapter;
    private NavController navController;

    public CheckoutPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.ThreadPolicy policy = new
//                StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        // ZaloPay SDK Init
//        ZaloPaySDK.init(2553, Environment.SANDBOX);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentCheckoutPageBinding.inflate(inflater, container, false);
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
        ((MainActivity) getActivity()).setTitle("Checkout");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(CheckoutPageViewModel.initializer)).get(CheckoutPageViewModel.class);

        new Handler().post(() -> {
            navController = ((MainActivity) getActivity()).getAppNavController();
        });

        adapter = new CheckoutListAdapter(getContext(), vb.checkoutRecyclerView);
        vb.checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.checkoutRecyclerView.setAdapter(adapter);

        ArrayList<CartItemModel> selectedItems = null;
        if (getArguments() != null) {
            selectedItems = (ArrayList<CartItemModel>) getArguments().getSerializable("selected_items");
        }

        if (selectedItems != null && !selectedItems.isEmpty()) {
            // Set selected items to ViewModel
            viewModel.setSelectedItems(selectedItems);
            
            adapter.addCartItems(selectedItems);

            double totalPrice = 0;
            for (CartItemModel item : selectedItems) {
                totalPrice += item.getQuantity() * item.getPrice() + 30000;
            }

            vb.txtTotalAmount.setText(String.format(Locale.US, "%.0f₫", totalPrice));
            setDefaultAddressModel();
//            vb.lblUserAddress.setText(viewModel.getAddressModel().toString());
//            vb.lblUsername.setText(viewModel.getAddressModel().getName());
//            vb.lblUserPhone.setText(viewModel.getAddressModel().getPhoneNumber());
//            UserModel userModel = viewModel.getUserModel();
//            vb.lblUsername.setText(userModel.getUserName());
//            vb.lblUserPhone.setText(userModel.getPhone());
//            vb.lblUserAddress.setText(userModel.getAddress());
//            List<AddressModel> addressModel = new ArrayList<>();


            vb.checkoutPageLoadingPage.setVisibility(View.GONE);
            vb.getRoot().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.lay_on));
        }

        vb.paymentGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.cash) {
                viewModel.setPaymentMethod("Cash");
            } else if (checkedId == R.id.creditCard) {
                viewModel.setPaymentMethod("CreditCard");
            }
        });

        vb.cash.setChecked(true);

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                vb.checkoutPageLoadingPage.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                vb.buyNowBtn.setEnabled(!isLoading);
            }
        });

        vb.buyNowBtn.setOnClickListener((v) -> {
            // Call buyNow method in ViewModel
            viewModel.buyNow(getActivity());
        });

        vb.navBack.setOnClickListener((v) -> {
            getActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        // loadCheckoutData(); // Đã bỏ
    }

    public void setDefaultAddressModel() {
        vb.checkoutPageLoadingPage.setVisibility(View.VISIBLE);

        viewModel.getAddressCustomer().observe(getViewLifecycleOwner(), response -> {
            vb.checkoutPageLoadingPage.setVisibility(View.GONE); // Ẩn loading khi có response

            if (response == null) {
                Toast.makeText(getContext(), "Response null", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (response.code()) {
                case BaseResponseModel.SUCCESSFUL_OPERATION:
                    ArrayList<AddressModel> addressList = response.body().getAddresses();
                    if (addressList != null) {
                        for (AddressModel address : addressList) {
                            if (address.isDefault()) {
                                viewModel.setAddressModel(address);

                                vb.lblUserAddress.setText(address.toString());
                                vb.lblUsername.setText(address.getName());
                                vb.lblUserPhone.setText(address.getPhoneNumber());
                                break;
                            }
                        }
                    }
                    break;

                case BaseResponseModel.FAILED_REQUEST_FAILURE:
                    Toast.makeText(getContext(), "Loading address Failed", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    DialogsProvider.get(getActivity()).messageDialog(
                            getString(R.string.Server_Error),
                            getString(R.string.Code) + response.code()
                    );
            }
        });
    }



//    void loadCheckoutData() {
//        vb.checkoutPageLoadingPage.setVisibility(View.VISIBLE);
//        String userId = viewModel.getUserModel().getId();
//        // Lấy giỏ hàng từ ViewModel
//        viewModel.getCart(userId).observe(getViewLifecycleOwner(), response -> {
//            switch (response.code()) {
//                case BaseResponseModel.SUCCESSFUL_OPERATION:
//                    if (response.body() == null) {
//                        return;
//                    }
//                    viewModel.setCartModel(response.body());
//
//                    ArrayList<CartItemModel> cartItems = response.body().getCartItems();
//                    adapter.addCartItems(cartItems);
//
//                    renderCheckoutPage();
//                    vb.checkoutPageLoadingPage.setVisibility(View.GONE);
//                    vb.getRoot().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.lay_on));
//                    break;
//
//                case BaseResponseModel.FAILED_REQUEST_FAILURE:
//                    DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Loading_Failed), getString(R.string.Please_Check_your_connection));
//                    break;
//
//                default:
//                    DialogsProvider.get(getActivity()).messageDialog(getString(R.string.Server_Error), getString(R.string.Code) + response.code());
//            }
//        });
//    }


    void renderCheckoutPage() {
        CartModel cartModel = viewModel.getCartModel();
        UserModel userModel = viewModel.getUserModel();
        vb.txtTotalAmount.setText(cartModel.getTotalPrice() + "$");
        vb.lblUsername.setText(userModel.getUserName());
        vb.lblUserPhone.setText(userModel.getPhone());
        vb.lblUserAddress.setText(userModel.getAddress());

    }

}