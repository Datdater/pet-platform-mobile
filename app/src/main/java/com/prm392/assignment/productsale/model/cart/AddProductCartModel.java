package com.prm392.assignment.productsale.model.cart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductCartModel {
    @SerializedName(value = "productVariantId")
    private String productVariantId;

    @SerializedName(value = "productName")
    private String productName;

    @SerializedName(value = "attributes")
    private String attributes;

    @SerializedName(value = "unitPrice")
    private double unitPrice;

    @SerializedName(value = "quantity")
    private int quantity;

    @SerializedName(value = "pictureUrl")
    private String pictureUrl;

    @SerializedName(value = "storeId")
    private String storeId;

    @SerializedName(value = "storeName")
    private String storeName;

    @SerializedName(value = "storeUrl")
    private String storeUrl;
}


