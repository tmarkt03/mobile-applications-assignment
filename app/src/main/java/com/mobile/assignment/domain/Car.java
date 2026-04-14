package com.mobile.assignment.domain;

public class Car {
    private int id;
    private String make;
    private String model;
    private String colour;
    private int year;
    private String vin;
    private String price;
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public String getPrice() {
        return price;
    }

    public String getVin() {
        return vin;
    }

    public int getYear() {
        return year;
    }

    public String getColour() {
        return colour;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public int getId() {
        return id;
    }
}
