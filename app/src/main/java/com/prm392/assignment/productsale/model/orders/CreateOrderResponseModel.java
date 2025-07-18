package com.prm392.assignment.productsale.model.orders;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrderResponseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("paymentUrl")
    private String paymentUrl;
}
