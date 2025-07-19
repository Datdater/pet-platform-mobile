package com.prm392.assignment.productsale.model.services;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailResponseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("basePrice")
    private int basePrice;
    @SerializedName("storeCity")
    private String storeCity;
    @SerializedName("storeDistrict")
    private String storeDistrict;
    @SerializedName("totalUsed")
    private int totalUsed;
    @SerializedName("ratingAverage")
    private float ratingAverage;
    @SerializedName("totalReviews")
    private int totalReviews;
    @SerializedName("image")
    private String image;
    @SerializedName("storeId")
    private String storeId;
    @SerializedName("estimatedTime")
    private String estimatedTime;
    @SerializedName("serviceCategoryId")
    private String serviceCategoryId;
    @SerializedName("serviceCategoryName")
    private String serviceCategoryName;
    @SerializedName("status")
    private boolean status;
    @SerializedName("petServiceDetails")
    private List<PetServiceDetail> petServiceDetails;
    @SerializedName("petServiceSteps")
    private List<PetServiceStep> petServiceSteps;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetServiceDetail {
        @SerializedName("id")
        private String id;
        @SerializedName("petWeightMin")
        private int petWeightMin;
        @SerializedName("petWeightMax")
        private int petWeightMax;
        @SerializedName("amount")
        private int amount;
        @SerializedName("petType")
        private boolean petType;
        @SerializedName("description")
        private String description;
        @SerializedName("name")
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetServiceStep {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("description")
        private String description;
        @SerializedName("priority")
        private int priority;
    }
} 