package edu.practicum.models;

import java.util.List;

public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public static class Builder {
        private String firstName = null;
        private String lastName = null;
        private String address = null;
        private String metroStation = null;
        private String phone = null;
        private int rentTime = 0;
        private String deliveryDate = null;
        private String comment = null;
        private List<String> color = null;

        public Order.Builder firstName(String val) {
            this.firstName = val;
            return this;
        }

        public Order.Builder lastName(String val) {
            this.lastName = val;
            return this;
        }

        public Order.Builder address(String val) {
            this.address = val;
            return this;
        }

        public Order.Builder metroStation(String val) {
            this.metroStation = val;
            return this;
        }

        public Order.Builder phone(String val) {
            this.phone = val;
            return this;
        }

        public Order.Builder rentTime(int val) {
            this.rentTime = val;
            return this;
        }

        public Order.Builder deliveryDate(String val) {
            this.deliveryDate = val;
            return this;
        }

        public Order.Builder comment(String val) {
            this.comment = val;
            return this;
        }

        public Order.Builder color(List<String> val) {
            this.color = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }

    }

    public Order(Order.Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        address = builder.address;
        metroStation = builder.metroStation;
        phone = builder.phone;
        rentTime = builder.rentTime;
        deliveryDate = builder.deliveryDate;
        comment = builder.comment;
        color = builder.color;
    }

    public List<String> getColor() {
        return color;
    }

    public String getComment() {
        return comment;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getPhone() {
        return phone;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public String getAddress() {
        return address;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
