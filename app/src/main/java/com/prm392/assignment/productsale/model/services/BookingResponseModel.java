package com.prm392.assignment.productsale.model.services;

import java.util.List;

public class BookingResponseModel {
    private int totalCount;
    private List<BookingItem> items;
    private int pageIndex;
    private int pageSize;
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    public static class BookingItem {
        private String bookingId;
        private String shopName;
        private String userName;
        private String userPhone;
        private String storeAddressProvince;
        private String storeAddressDistrict;
        private String storeAddressWard;
        private String storeAddressStreet;
        private int status;
        private int totalPrice;
        private String bookingTime;
        private List<PetWithServices> petWithServices;

        public static class PetWithServices {
            private Pet pet;
            private List<Service> services;

            public static class Pet {
                private String id;
                private String name;
                private String dob;
                private boolean petType;
                private String color;
                // getters/setters
                public String getId() { return id; }
                public String getName() { return name; }
                public String getDob() { return dob; }
                public boolean isPetType() { return petType; }
                public String getColor() { return color; }
            }
            public static class Service {
                private String id;
                private String serviceDetailName;
                private String serviceName;
                private int price;
                private String imageUrl;
                // getters/setters
                public String getId() { return id; }
                public String getServiceDetailName() { return serviceDetailName; }
                public String getServiceName() { return serviceName; }
                public int getPrice() { return price; }
                public String getImageUrl() { return imageUrl; }
            }
            public Pet getPet() { return pet; }
            public List<Service> getServices() { return services; }
        }
        // getters/setters
        public String getBookingId() { return bookingId; }
        public String getShopName() { return shopName; }
        public String getUserName() { return userName; }
        public String getUserPhone() { return userPhone; }
        public String getStoreAddressProvince() { return storeAddressProvince; }
        public String getStoreAddressDistrict() { return storeAddressDistrict; }
        public String getStoreAddressWard() { return storeAddressWard; }
        public String getStoreAddressStreet() { return storeAddressStreet; }
        public int getStatus() { return status; }
        public int getTotalPrice() { return totalPrice; }
        public String getBookingTime() { return bookingTime; }
        public List<PetWithServices> getPetWithServices() { return petWithServices; }
    }
    // getters/setters
    public int getTotalCount() { return totalCount; }
    public List<BookingItem> getItems() { return items; }
    public int getPageIndex() { return pageIndex; }
    public int getPageSize() { return pageSize; }
    public boolean isHasPreviousPage() { return hasPreviousPage; }
    public boolean isHasNextPage() { return hasNextPage; }
} 