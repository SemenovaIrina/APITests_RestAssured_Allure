package edu.practicum.models;

import java.util.List;

public class InfoAboutOneOrder {
    private OrderInfo order;

    public OrderInfo getOrder() {
        return order;
    }

    // так как этот класс больше нигде не используется не стала выносить его отдельно, сделала вложенным
    public class OrderInfo {
        private int id;
        private String firstName;
        private String lastName;
        private String address;
        private String metroStation;
        private String phone;
        private int rentTime;
        private String deliveryDate;
        private int track;
        private int status;
        private List<String> color;
        private String comment;
        private boolean cancelled;
        private boolean finished;
        private boolean inDelivery;
        private String courierFirstName;
        private String createdAt;
        private String updatedAt;


        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getAddress() {
            return address;
        }

        public String getMetroStation() {
            return metroStation;
        }

        public String getPhone() {
            return phone;
        }

        public int getRentTime() {
            return rentTime;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public int getTrack() {
            return track;
        }

        public int getStatus() {
            return status;
        }

        public List<String> getColor() {
            return color;
        }

        public String getComment() {
            return comment;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isInDelivery() {
            return inDelivery;
        }

        public String getCourierFirstName() {
            return courierFirstName;
        }
    }
}

