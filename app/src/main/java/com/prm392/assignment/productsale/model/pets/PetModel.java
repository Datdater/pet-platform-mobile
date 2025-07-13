package com.prm392.assignment.productsale.model.pets;

import com.google.gson.annotations.SerializedName;

public class PetModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("dob")
    private String dob;

    @SerializedName("weight")
    private int weight;

    @SerializedName("petType")
    private boolean petType; // true for dog, false for cat

    @SerializedName("appUserId")
    private String appUserId;

    @SerializedName("color")
    private String color;

    @SerializedName("specialRequirement")
    private String specialRequirement;

    // Constructors
    public PetModel() {}

    public PetModel(String name, String image, String dob, int weight, boolean petType, String color, String specialRequirement) {
        this.name = name;
        this.image = image;
        this.dob = dob;
        this.weight = weight;
        this.petType = petType;
        this.color = color;
        this.specialRequirement = specialRequirement;
    }

    public PetModel(String id, String name, String image, String dob, int weight, boolean petType, String appUserId, String color, String specialRequirement) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.dob = dob;
        this.weight = weight;
        this.petType = petType;
        this.appUserId = appUserId;
        this.color = color;
        this.specialRequirement = specialRequirement;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isPetType() {
        return petType;
    }

    public void setPetType(boolean petType) {
        this.petType = petType;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    // Helper method to get pet type as string
    public String getPetTypeString() {
        return petType ? "Chó" : "Mèo";
    }

    // Helper method to get display name for spinner
    public String getDisplayName() {
        return name + " (" + getPetTypeString() + ")";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
} 