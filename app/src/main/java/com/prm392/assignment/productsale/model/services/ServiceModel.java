package com.prm392.assignment.productsale.model.services;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceModel {
    @SerializedName(value = "id")
    private String id;
    @SerializedName(value = "name")
    private String name;
    @SerializedName(value = "description")
    private String description;
    @SerializedName(value = "image")
    private String image;
    @SerializedName(value = "storeId")
    private String storeId;
    @SerializedName(value = "estimatedTime")
    private String estimatedTime;
    @SerializedName(value = "serviceCategoryId")
    private String serviceCategoryId;
    @SerializedName(value = "price")
    private double price;
    @SerializedName(value = "basePrice")
    private double basePrice;
    @SerializedName(value = "storeCity")
    private String storeCity;
    @SerializedName(value = "storeDistrict")
    private String storeDistrict;
    @SerializedName(value = "totalUsed")
    private int totalUsed;
    @SerializedName(value = "ratingAverage")
    private double ratingAverage;
    @SerializedName(value = "status")
    private boolean status;
    @SerializedName(value = "storeName")
    private String storeName;
    @SerializedName(value = "categoryName")
    private String categoryName;
}
