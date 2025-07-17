package com.prm392.assignment.productsale.model.services;

import com.google.gson.annotations.SerializedName;
import com.prm392.assignment.productsale.model.BaseResponseModel;
import com.prm392.assignment.productsale.model.products.ProductSaleModel;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceResponseModel extends BaseResponseModel {

    @SerializedName("totalCount")
    private int totalItemsCount;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("pageIndex")
    private int pageIndex;

    @SerializedName("hasNextPage")
    private boolean next;

    @SerializedName("hasPreviousPage")
    private boolean previous;

    @SerializedName("items")
    private ArrayList<ServiceModel> services;
}
