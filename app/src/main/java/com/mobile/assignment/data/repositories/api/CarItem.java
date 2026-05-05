package com.mobile.assignment.data.repositories.api;

import com.google.gson.annotations.SerializedName;

public class CarItem {

    public int id;
    @SerializedName("car")
    public String carMake;
    @SerializedName("car_model")
    public String carModel;
    @SerializedName("car_color")
    public String color;
    @SerializedName("car_model_year")
    public int year;
    @SerializedName("car_vin")
    public String vin;
    public String price;
    public boolean availability;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    //public CarItem(int id, String carMake, String carModel, String color, int year, String vin, String price, boolean availability) {}



}
