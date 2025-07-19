package com.prm392.assignment.productsale.model.products;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductSaleModel {

    @SerializedName(value = "id")
    private String productId;

    @SerializedName(value = "name")
    private String productName;

    @SerializedName(value = "storeName")
    private String storeName;

    @SerializedName(value = "price")
    private float price;
    @SerializedName(value = "starAverage")
    private float starAverage;
    @SerializedName(value = "reviewCount")
    private int reviewCount;
    @SerializedName(value = "sold")
    private int sold;

    @SerializedName(value = "productImage")
    private String imageUrl;

    @SerializedName(value = "categoryName")
    private String categoryName;

    public String getCurrencyPrice() {
        return (int)price + "đ";
    }

    public String getProductName() { return productName; }

    public String getProductImage() { return imageUrl; }
}
