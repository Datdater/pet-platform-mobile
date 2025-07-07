package com.prm392.assignment.productsale.model.products;

import com.google.gson.annotations.SerializedName;
import com.prm392.assignment.productsale.model.BaseResponseModel;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductsSaleResponseModel extends BaseResponseModel {

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
    private ArrayList<ProductSaleModel> products;

}