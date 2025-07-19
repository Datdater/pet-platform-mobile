package com.prm392.assignment.productsale.model.address;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAddressModel {
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("ward")
    private String ward;
    @SerializedName("district")
    private String district;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("isDefault")
    private boolean isDefault;

}
