package com.prm392.assignment.productsale.model.cart;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartModel {
    @SerializedName("id")
    private String productId;
    @SerializedName("quantity")
    private int quantity;
}
