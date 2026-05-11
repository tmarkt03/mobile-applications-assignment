package com.mobile.assignment.data.repositories.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarList {
    @SerializedName("cars")
    public List<CarItem> list;

    public CarList(int id, String carMake, String carModel, String color, int year, String vin, String price, boolean availability) {
    }
}
