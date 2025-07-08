package com.prm392.assignment.productsale.viewmodel.fragment.main;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.prm392.assignment.productsale.data.repository.ProductsSaleRepository;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.UserModel;
import com.prm392.assignment.productsale.model.cart.AddProductCartModel;
import com.prm392.assignment.productsale.model.products.ProductSaleModel;
import com.prm392.assignment.productsale.model.products.ProductSalePageResponseModel;
import com.prm392.assignment.productsale.model.products.ProductVariantModel;
import com.prm392.assignment.productsale.model.products.StoreLocation;
import com.prm392.assignment.productsale.util.UserAccountManager;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

public class ProductPageViewModel extends ViewModel {
    private final ProductsSaleRepository productsSaleRepository;

    private String productId;
    private final String token;

    @Getter
    @Setter
    private ProductSalePageResponseModel productSaleModel;

    @Getter
    @Setter
    private int productQuantity = 1;
    @Getter
    @Setter
    private UserModel userModel;

    public ProductPageViewModel(@NotNull Application application) {
        super();
        productsSaleRepository = new ProductsSaleRepository();

        token = UserAccountManager.getToken(application, UserAccountManager.TOKEN_TYPE_BEARER);
        userModel = UserAccountManager.getUser(application);
    }


    public static final ViewModelInitializer<ProductPageViewModel> initializer = new ViewModelInitializer<>(
            ProductPageViewModel.class,
            creationExtras -> {
                Application app = creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new ProductPageViewModel(app);
            }
    );

    public LiveData<Response<ProductSalePageResponseModel>> getProductSale() {
        return productsSaleRepository.getProductSale(token, productId);
    }

    public LiveData<Response<BaseResponseModel>> addProductToCart(String productVariantId) {
        MediatorLiveData<Response<BaseResponseModel>> result = new MediatorLiveData<>();

        LiveData<Response<ProductVariantModel>> productLiveData = productsSaleRepository.getProductVariant(productVariantId);

        result.addSource(productLiveData, productResponse -> {
            if (productResponse != null && productResponse.isSuccessful() && productResponse.body() != null) {
                ProductVariantModel productVariantModel = productResponse.body();

                AddProductCartModel addProductCartModel = new AddProductCartModel();
                addProductCartModel.setPictureUrl(productVariantModel.getPictureUrl());
                addProductCartModel.setProductName(productVariantModel.getProductName());
                addProductCartModel.setUnitPrice(productVariantModel.getUnitPrice());
                addProductCartModel.setQuantity(productQuantity);
                addProductCartModel.setStoreId(productVariantModel.getStoreId());
                addProductCartModel.setStoreName(productVariantModel.getStoreName());
                addProductCartModel.setStoreUrl(productVariantModel.getStoreUrl());
                addProductCartModel.setAttributes(getFormattedAttributes(productVariantModel.getAttributes()));
                addProductCartModel.setProductVariantId(productVariantModel.getProductVariantId());

                LiveData<Response<BaseResponseModel>> cartResponse = productsSaleRepository.addProductToCart(token, addProductCartModel);
                result.addSource(cartResponse, result::setValue);
            } else {
//                result.setValue(Response.error(...)); // Xử lý lỗi nếu cần
            }
        });

        return result;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    private String getFormattedAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) return "";
        return attributes.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
