package com.prm392.assignment.productsale.model.products;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductSalePageResponseModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("storeId")
    private String storeId;

    @SerializedName("storeName")
    private String storeName;

    @SerializedName("storeUrl")
    private String storeUrl;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("basePrice")
    private double basePrice;

    @SerializedName("weight")
    private double weight;

    @SerializedName("length")
    private int length;

    @SerializedName("height")
    private int height;

    @SerializedName("width")
    private int width;

    @SerializedName("sold")
    private int sold;

    @SerializedName("starAverage")
    private double starAverage;

    @SerializedName("reviewCount")
    private int reviewCount;

    @SerializedName("variants")
    private List<Variant> variants;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("reviews")
    private Object reviews; // Hoặc đổi sang kiểu cụ thể nếu có định nghĩa

    // Nested classes for variants and images
    @Getter
    @Setter
    public static class Variant {
        @SerializedName("attributes")
        private Map<String, String> attributes;

        @SerializedName("price")
        private double price;

        @SerializedName("stock")
        private int stock;
    }

    @Getter
    @Setter
    public static class Image {
        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("isMain")
        private boolean isMain;
    }

}
