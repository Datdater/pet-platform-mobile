package com.prm392.assignment.productsale.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseModel extends BaseResponseModel {

    @SerializedName("accessToken")
    private String token;

    @SerializedName("user")
    private UserModel user;

}
