package com.prm392.assignment.productsale.model.products;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductVariantModel {
    @SerializedName(value = "productVariantId")
    private String productVariantId;

    @SerializedName(value = "productName")
    private String productName;

    @SerializedName(value = "unitPrice")
    private double unitPrice;

    @SerializedName(value = "attributes")
    private Map<String, String> attributes;

    @SerializedName(value = "pictureUrl")
    private String pictureUrl;

    @SerializedName(value = "storeId")
    private String storeId;

    @SerializedName(value = "storeName")
    private String storeName;

    @SerializedName(value = "storeUrl")
    private String storeUrl;
}
