package com.prm392.assignment.productsale.model.orders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel implements Serializable {
    @SerializedName("id")
    private String id;
    
    @SerializedName("storeId")
    private String storeId;
    
    @SerializedName("storeName")
    private String storeName;
    
    @SerializedName("customerName")
    private String customerName;
    
    @SerializedName("customerPhone")
    private String customerPhone;
    
    @SerializedName("price")
    private Double price;
    
    @SerializedName("createdTime")
    private Date createdTime;
    
    @SerializedName("deliveryPrice")
    private Double deliveryPrice;
    
    @SerializedName("orderDetailDTOs")
    private List<OrderDetailModel> orderDetailDTOs;
    
    @SerializedName("orderStatus")
    private String orderStatus;
}


