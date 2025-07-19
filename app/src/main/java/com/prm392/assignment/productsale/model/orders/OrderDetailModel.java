package com.prm392.assignment.productsale.model.orders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailModel implements Serializable {
    @SerializedName("quantity")
    private Integer quantity;
    
    @SerializedName("price")
    private Double price;
    
    @SerializedName("productVariationId")
    private String productVariationId;
    
    @SerializedName("productName")
    private String productName;
    
    @SerializedName("pictureUrl")
    private String pictureUrl;
    
    @SerializedName("productId")
    private String productId;
    
    @SerializedName("attribute")
    private Map<String, String> attribute;
}
