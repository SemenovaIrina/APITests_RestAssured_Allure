package edu.practicum.models;

import io.qameta.allure.internal.shadowed.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

public class NearestMetroStations {
    @JsonDeserialize(as = ArrayList.class)
    private ArrayList<String> nearestStation;

    public void setNearestStation(ArrayList<String> nearestStation) {
        this.nearestStation = nearestStation;
    }

    public ArrayList<String> getNearestStation() {
        return nearestStation;
    }
}
