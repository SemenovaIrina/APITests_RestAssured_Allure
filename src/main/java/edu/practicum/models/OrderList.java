package edu.practicum.models;

import java.util.List;

public class OrderList {
    private List<OrderInfoGetInList> orders;
    private PageInfo pageInfo;
    private List<Station> availableStations;

    public List<OrderInfoGetInList> getOrders() {
        return orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Station> getAvailableStations() {
        return availableStations;
    }

    public void setOrders(List<OrderInfoGetInList> orders) {
        this.orders = orders;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setAvailableStations(List<Station> availableStations) {
        this.availableStations = availableStations;
    }
}

