package com.carcatalog.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Room DAO — CRUD operations for the favourites table.
 */
@Dao
public interface FavouriteCarDao {

    /** Insert or replace a favourite car. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavouriteCar car);

    /** Remove a favourite car. */
    @Delete
    void delete(FavouriteCar car);

    /** Get all favourite cars, observed as LiveData. */
    @Query("SELECT * FROM favourites ORDER BY brand, model")
    LiveData<List<FavouriteCar>> getAll();

    /** Check whether a car with the given id is already saved. */
    @Query("SELECT COUNT(*) FROM favourites WHERE id = :carId")
    int isFavourite(int carId);
}
