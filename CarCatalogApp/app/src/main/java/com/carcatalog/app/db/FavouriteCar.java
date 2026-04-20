package com.carcatalog.app.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Room entity — stores a car the user has marked as a favourite.
 * Maps directly to the "favourites" table in the local SQLite database.
 */
@Entity(tableName = "favourites")
public class FavouriteCar {

    @PrimaryKey
    public int id;

    public String brand;
    public String model;
    public String color;
    public int year;
    public String vin;
    public String price;

    public FavouriteCar() { }

    @Ignore
    public FavouriteCar(int id, String brand, String model,
                        String color, int year, String vin, String price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.vin = vin;
        this.price = price;
    }

    /** Display title: "Toyota Camry (2007)" */
    public String getTitle() {
        return brand + " " + model + " (" + year + ")";
    }
}
