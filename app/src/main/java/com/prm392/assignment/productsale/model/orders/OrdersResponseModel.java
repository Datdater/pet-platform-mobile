package com.prm392.assignment.productsale.model.orders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponseModel implements Serializable {
    @SerializedName("items")
    private List<OrderModel> orders;
    
    @SerializedName("totalCount")
    private int totalCount;
    
    @SerializedName("pageIndex")
    private int pageNumber;
    
    @SerializedName("pageSize")
    private int pageSize;
    

} 