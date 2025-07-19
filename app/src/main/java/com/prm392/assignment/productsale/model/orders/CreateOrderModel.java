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
public class CreateOrderModel implements Serializable {
    @SerializedName("addressId")
    private String addressId;
    @SerializedName("paymentMethod")
    private int paymentMethod;
    @SerializedName("deliveryPrice")
    private float deliveryPrice;
    @SerializedName("promotionId")
    private String promotionId;
    @SerializedName("note")
    private String note;
    @SerializedName("orderDetails")
    private List<OrderDetailModel> orderDetails;
}
