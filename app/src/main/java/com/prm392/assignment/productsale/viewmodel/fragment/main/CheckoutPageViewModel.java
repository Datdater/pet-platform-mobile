package com.prm392.assignment.productsale.viewmodel.fragment.main;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.navigation.NavController;

import com.prm392.assignment.productsale.Api.CreateOrder;
import com.prm392.assignment.productsale.R;
import com.prm392.assignment.productsale.data.repository.CartRepository;
import com.prm392.assignment.productsale.data.repository.ProductsSaleRepository;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.UserModel;
import com.prm392.assignment.productsale.model.cart.CartModel;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.view.activity.PaymentNotification;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;
//import vn.zalopay.sdk.ZaloPayError;
//import vn.zalopay.sdk.ZaloPaySDK;
//import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckoutPageViewModel extends ViewModel {
    private final ProductsSaleRepository productsSaleRepository;
    private final CartRepository cartRepository;
    private LiveData<Response<CartModel>> cartLiveData;

    private MutableLiveData<String> paymentResult = new MutableLiveData<>();

    private long productId;
    private final String token;

    @Getter
    @Setter
    private CartModel cartModel;

    @Getter
    private final UserModel userModel;

    @Getter
    @Setter
    private String paymentMethod;

    private NavController controller;

    private final Application app;

    public LiveData<String> getPaymentResult() {
        return paymentResult;
    }

    public CheckoutPageViewModel(@NotNull Application application) {
        super();
        app = application;
        cartRepository = new CartRepository();
        productsSaleRepository = new ProductsSaleRepository();

        token = UserAccountManager.getToken(application, UserAccountManager.TOKEN_TYPE_BEARER);
        userModel = UserAccountManager.getUser(application);

    }

    public static final ViewModelInitializer<CheckoutPageViewModel> initializer = new ViewModelInitializer<>(
            CheckoutPageViewModel.class,
            creationExtras -> {
                Application app = creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new CheckoutPageViewModel(app);
            }
    );

    public LiveData<Response<BaseResponseModel>> completePaymentAndConvertCartToOrder(
            int userId, String paymentMethod, String billingAddress) {
        return cartRepository.completePaymentAndConvertCartToOrder(token, userId, paymentMethod, billingAddress);
    }

    public LiveData<Response<CartModel>> getCart(int userId) {
        cartLiveData = cartRepository.getCart(token, userId);
        return cartLiveData;
    }

    public void buyNow(Context context){
        if(paymentMethod == "Cash"){
        if (cartModel == null) return;
        int userId = userModel.getId();
        String billingAddress = "Billing Address Placeholder";

        completePaymentAndConvertCartToOrder(userId, "Cash", billingAddress).observeForever(response -> {
            if (response != null && response.isSuccessful()) {
                Toast.makeText(context, "Thanh toán thành công, giỏ hàng đã được chuyển thành đơn hàng.", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(context, PaymentNotification.class);
//                intent1.putExtra("result", "Thanh toán thành công");
//                context.startActivity(intent1);

            } else {
                Toast.makeText(context, "Thanh toán thất bại, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(context, PaymentNotification.class);
                intent1.putExtra("result", "Thanh toán thất bại");
                context.startActivity(intent1);
            }
        });
    }}
//    public void buyNow(Context context) {
//        if (cartModel == null) return;
//        CreateOrder orderApi = new CreateOrder();
//
//        String totalString = String.format(Locale.US, "%.0f", cartModel.getTotalPrice());
//
//        try {
//            JSONObject data = orderApi.createOrder(totalString);
//            String code = data.getString("return_code");
//            if (code.equals("1")) {
//                String token = data.getString("zp_trans_token");
//                ZaloPaySDK.getInstance().payOrder((Activity) context, token, "demozpdk://app", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        Intent intent1 = new Intent(context, PaymentNotification.class);
//                    intent1.putExtra("result", "Thanh toán thành công");
//                    context.startActivity(intent1);
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//                        Intent intent1 = new Intent(context, PaymentNotification.class);
//                        intent1.putExtra("result", "Hủy thanh toán");
//                        context.startActivity(intent1);
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                        Intent intent1 = new Intent(context, PaymentNotification.class);
//                        intent1.putExtra("result", "Thanh toán thất bại");
//                        context.startActivity(intent1);
//                    }
//                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
