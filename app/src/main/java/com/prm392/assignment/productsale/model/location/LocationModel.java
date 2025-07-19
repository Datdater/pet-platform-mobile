package com.prm392.assignment.productsale.model.location;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationModel {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("code")
    private String code;
    
    @SerializedName("division_type")
    private String divisionType;
    
    @SerializedName("codename")
    private String codename;
    
    @SerializedName("phone_code")
    private String phoneCode;
    
    @Override
    public String toString() {
        return name;
    }
} 