package com.prm392.assignment.productsale.model.address;

import com.google.gson.annotations.SerializedName;
import com.prm392.assignment.productsale.model.BaseResponseModel;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllAddressResponseModel extends BaseResponseModel {
    @SerializedName("addresses")
    private ArrayList<AddressModel> addresses;
}
