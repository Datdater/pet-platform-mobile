package com.prm392.assignment.productsale.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileResponseModel extends BaseResponseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("email")
    private String email;
}