package com.carcatalog.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Car model mapped from MyFakeAPI response.
 * Implements Serializable so instances can be passed between fragments via Bundle.
 *
 * API fields: id, car (brand), car_model, car_color, car_model_year, car_vin, price, availability
 * Note: the API has no image or website URL, so we derive them.
 */
public class Car implements Serializable {

    @SerializedName("id")
    private final int id;

    @SerializedName("car")
    private final String brand;

    @SerializedName("car_model")
    private final String model;

    @SerializedName("car_color")
    private final String color;

    @SerializedName("car_model_year")
    private final int year;

    @SerializedName("car_vin")
    private final String vin;

    @SerializedName("price")
    private final String price;

    @SerializedName("availability")
    private final boolean availability;

    public Car(int id,
               String brand,
               String model,
               String color,
               int year,
               String vin,
               String price,
               boolean availability) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.vin = vin;
        this.price = price;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public String getVin() {
        return vin;
    }

    public String getPrice() {
        return price;
    }

    public boolean isAvailability() {
        return availability;
    }

    /** Display title: "Toyota Camry (2007)" */
    public String getTitle() {
        return brand + " " + model + " (" + year + ")";
    }

    /**
     * Deterministic car image via loremflickr.
     * The ?lock=$id parameter makes it consistent per card (same car → same image).
     */
    public String getImageUrl() {
        return "https://loremflickr.com/400/250/car,automobile?lock=" + id;
    }

    /**
     * Buy button URL → Google search for this car.
     * Opens in the browser (no selling happens in this app — catalog only).
     */
    public String getBuyUrl() {
        return "https://www.google.com/search?q=" +
                (brand + "+" + model + "+" + year).replace(" ", "+") +
                "+car";
    }
}
