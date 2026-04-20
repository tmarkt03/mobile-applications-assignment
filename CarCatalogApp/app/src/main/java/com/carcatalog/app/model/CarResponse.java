package com.carcatalog.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Top-level API response wrapper.
 * https://myfakeapi.com/api/cars/ returns: { "cars": [ ... ] }
 */
public class CarResponse {

    @SerializedName("cars")
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }
}
