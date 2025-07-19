package com.prm392.assignment.productsale.model.address;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressModel  {
    @SerializedName("id")
    private String id;
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("ward")
    private String ward;
    @SerializedName("district")
    private String district;
    @SerializedName("country")
    private String country;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("addressType")
    private String addressType;
    @SerializedName("isDefault")
    private boolean isDefault;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        appendIfNotEmpty(sb, street);
        appendIfNotEmpty(sb, ward);
        appendIfNotEmpty(sb, district);
        appendIfNotEmpty(sb, city);
        appendIfNotEmpty(sb, country);

        // Xoá dấu phẩy thừa cuối cùng (nếu có)
        if (sb.length() > 0 && sb.charAt(sb.length() - 2) == ',') {
            sb.setLength(sb.length() - 2); // Xóa ", "
        }

        return sb.toString();
    }
    private void appendIfNotEmpty(StringBuilder sb, String value) {
        if (value != null && !value.trim().isEmpty()) {
            sb.append(value.trim()).append(", ");
        }
    }
}
