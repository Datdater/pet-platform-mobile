package com.prm392.assignment.productsale.model.services;

import java.util.List;

public class BookingRequestModel {
    private String bookingTime;
    private String description;
    private String promotionId;
    private List<BookingDetail> bookingDetails;

    public static class BookingDetail {
        private Pet pet;
        private List<Service> services;

        public static class Pet {
            private String id;
            public Pet(String id) { this.id = id; }
            public String getId() { return id; }
            public void setId(String id) { this.id = id; }
        }
        public static class Service {
            private String id;
            public Service(String id) { this.id = id; }
            public String getId() { return id; }
            public void setId(String id) { this.id = id; }
        }
        public Pet getPet() { return pet; }
        public void setPet(Pet pet) { this.pet = pet; }
        public List<Service> getServices() { return services; }
        public void setServices(List<Service> services) { this.services = services; }
    }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPromotionId() { return promotionId; }
    public void setPromotionId(String promotionId) { this.promotionId = promotionId; }
    public List<BookingDetail> getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(List<BookingDetail> bookingDetails) { this.bookingDetails = bookingDetails; }
} 