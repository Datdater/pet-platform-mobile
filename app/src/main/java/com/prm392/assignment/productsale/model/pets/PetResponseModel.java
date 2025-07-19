package com.prm392.assignment.productsale.model.pets;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PetResponseModel {
    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("items")
    private List<PetModel> items;

    @SerializedName("pageIndex")
    private int pageIndex;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("hasPreviousPage")
    private boolean hasPreviousPage;

    @SerializedName("hasNextPage")
    private boolean hasNextPage;

    // Constructors
    public PetResponseModel() {}

    public PetResponseModel(int totalCount, List<PetModel> items, int pageIndex, int pageSize, boolean hasPreviousPage, boolean hasNextPage) {
        this.totalCount = totalCount;
        this.items = items;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }

    // Getters and Setters
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PetModel> getItems() {
        return items;
    }

    public void setItems(List<PetModel> items) {
        this.items = items;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
} 