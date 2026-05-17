package com.carcatalog.app.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database — single instance (singleton).
 * Version 1 — contains the favourites table.
 */
@Database(entities = {FavouriteCar.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final String DB_NAME = "car_catalog_db";

    public abstract FavouriteCarDao favouriteCarDao();

    /** Returns the singleton instance, creating it if necessary. */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
