package com.prm392.assignment.productsale.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseModel {
    @SerializedName("data")
    private ArrayList<LocationModel> data;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("status")
    private String status;
} 