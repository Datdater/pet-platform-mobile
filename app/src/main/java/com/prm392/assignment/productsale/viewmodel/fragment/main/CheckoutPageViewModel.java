package com.prm392.assignment.productsale.viewmodel.fragment.main;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
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
import com.prm392.assignment.productsale.data.repository.CustomerRepository;
import com.prm392.assignment.productsale.data.repository.OrderRepository;
import com.prm392.assignment.productsale.data.repository.ProductsSaleRepository;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.UserModel;
import com.prm392.assignment.productsale.model.address.AddressModel;
import com.prm392.assignment.productsale.model.address.GetAllAddressResponseModel;
import com.prm392.assignment.productsale.model.cart.CartItemModel;
import com.prm392.assignment.productsale.model.cart.CartModel;
import com.prm392.assignment.productsale.model.orders.CreateOrderModel;
import com.prm392.assignment.productsale.model.orders.OrderDetailModel;
import com.prm392.assignment.productsale.util.UserAccountManager;
import com.prm392.assignment.productsale.view.activity.MainActivity;
import com.prm392.assignment.productsale.view.activity.PaymentNotification;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private LiveData<Response<CartModel>> cartLiveData;

    private MutableLiveData<String> paymentResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private long productId;
    private final String token;

    @Getter
    @Setter
    private CartModel cartModel;

    @Getter
    private final UserModel userModel;

    @Getter
    @Setter
    private AddressModel addressModel;

    @Getter
    @Setter
    private String paymentMethod;

    @Getter
    @Setter
    private List<CartItemModel> selectedItems;

    private NavController controller;

    private final Application app;

    public LiveData<String> getPaymentResult() {
        return paymentResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public CheckoutPageViewModel(@NotNull Application application) {
        super();
        app = application;
        cartRepository = new CartRepository();
        productsSaleRepository = new ProductsSaleRepository();
        customerRepository = new CustomerRepository();
        orderRepository = new OrderRepository();

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

    public LiveData<Response<BaseResponseModel>> createOrder(CreateOrderModel orderModel) {
        return orderRepository.createOrder(token, orderModel);
    }

    public LiveData<Response<BaseResponseModel>> removeItemFromCart(String cartId) {
        return cartRepository.removeItemFromCart(token, cartId);
    }

    public LiveData<Response<BaseResponseModel>> clearCart() {
        return cartRepository.clearCart(token);
    }

    public LiveData<Response<BaseResponseModel>> completePaymentAndConvertCartToOrder(
            String userId, String paymentMethod, String billingAddress) {
        return cartRepository.completePaymentAndConvertCartToOrder(token, userId, paymentMethod, billingAddress);
    }
    public LiveData<Response<GetAllAddressResponseModel>> getAddressCustomer() {
        return customerRepository.getCustomerAddress(token);
    }

    public LiveData<Response<CartModel>> getCart(String userId) {
        cartLiveData = cartRepository.getCart(token);
        return cartLiveData;
    }

    public void buyNow(Context context){
        if (selectedItems == null || selectedItems.isEmpty()) {
            Toast.makeText(context, "No items selected for checkout", Toast.LENGTH_SHORT).show();
            return;
        }

        if (addressModel == null) {
            Toast.makeText(context, "Please select a delivery address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create order details from selected items
        List<OrderDetailModel> orderDetails = new ArrayList<>();
        for (CartItemModel item : selectedItems) {
            OrderDetailModel detail = new OrderDetailModel();
            detail.setQuantity(item.getQuantity());
            detail.setProductVariationId(item.getProductId());
            orderDetails.add(detail);
        }

        // Create order model
        CreateOrderModel orderModel = new CreateOrderModel();
        orderModel.setAddressId(addressModel.getId());
        orderModel.setPaymentMethod(paymentMethod.equals("Cash") ? 0 : 1); // 1 for Cash, 2 for Credit Card
        orderModel.setDeliveryPrice(30000); // Fixed shipping fee
        orderModel.setPromotionId("11c246d0-0cb5-4be5-8d4a-4d78e887df19"); // No promotion for now
        orderModel.setNote("Order placed from mobile app");
        orderModel.setOrderDetails(orderDetails);

        // Set loading state
        isLoading.postValue(true);
        
        // Call create order API
        createOrder(orderModel).observeForever(response -> {
            if (response != null && response.isSuccessful()) {
                    isLoading.postValue(false);
                    removeOrderedItemsFromCart(context);

            } else {
                isLoading.postValue(false);
                Toast.makeText(context, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PaymentNotification.class);
                intent.putExtra("result", "Network error");
                context.startActivity(intent);
            }
        });
    }

    private void removeOrderedItemsFromCart(Context context) {
        if (selectedItems == null || selectedItems.isEmpty()) {
            // No items to remove, just show success message
            isLoading.postValue(false);
            Toast.makeText(context, "Order created successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, PaymentNotification.class);
            intent.putExtra("result", "Order created successfully");
            context.startActivity(intent);
            return;
        }

        // Option 1: Remove specific ordered items one by one
        removeNextItemFromCart(context, 0);
        
    }

    private void removeNextItemFromCart(Context context, int index) {
        for (CartItemModel item : selectedItems) {
            removeCartItem(item.getCartId()).observeForever(response -> {switch (response.code()) {
                case BaseResponseModel.SUCCESSFUL_OPERATION:
                    Toast.makeText(context, "Product removed from cart", Toast.LENGTH_SHORT).show();
                    isLoading.postValue(false);
                    Toast.makeText(context, "Order created successfully! Items removed from cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PaymentNotification.class);
                    intent.putExtra("result", "Order created successfully");
                    context.startActivity(intent);
                    break;

                case BaseResponseModel.FAILED_REQUEST_FAILURE:
                    Toast.makeText(context, "Error: Failed to remove item", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    break;
            }});
        }


    }

    private void clearEntireCart(Context context) {
        clearCart().observeForever(response -> {
            isLoading.postValue(false);
            if (response != null && response.isSuccessful()) {
                Toast.makeText(context, "Order created successfully! Cart cleared.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PaymentNotification.class);
                intent.putExtra("result", "Order created successfully");
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Order created successfully! (Cart may not have been cleared)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PaymentNotification.class);
                intent.putExtra("result", "Order created successfully");
                context.startActivity(intent);
            }
        });
    }

    public LiveData<Response<BaseResponseModel>> removeCartItem( String cartId) {
        return cartRepository.removeItemFromCart(token, cartId);
    }

    public void setDefaultAddressModel() {

    }

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
