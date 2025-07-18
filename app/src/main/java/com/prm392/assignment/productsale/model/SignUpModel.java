package com.prm392.assignment.productsale.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpModel {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @Override
    public String toString() {
        return "SignUpModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='[HIDDEN]'" +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


}