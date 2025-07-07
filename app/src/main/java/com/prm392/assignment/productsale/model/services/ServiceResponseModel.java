package com.prm392.assignment.productsale.model.services;

import com.google.gson.annotations.SerializedName;
import com.prm392.assignment.productsale.model.products.ProductSaleModel;

import java.util.ArrayList;

public class ServiceResponseModel {

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
