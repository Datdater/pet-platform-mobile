package com.prm392.assignment.productsale.model.cart;

import com.google.gson.annotations.SerializedName;
import com.prm392.assignment.productsale.model.products.ProductSaleModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemModel {
    @SerializedName(value = "cartItemId")
    private String cartItemId;
    private boolean selected;
    @SerializedName(value = "id")
    private String cartId;

    @SerializedName(value = "productVariantId")
    private String productId;

    @SerializedName(value = "quantity")
    private int quantity;

    @SerializedName(value = "unitPrice")
    private float price;

    @SerializedName(value = "pictureUrl")
    private String pictureUrl;
    @SerializedName(value = "storeUrl")
    private String storeUrl;
    @SerializedName(value = "storeName")
    private String storeName;

    @SerializedName(value = "productName")
    private String productName;


}


