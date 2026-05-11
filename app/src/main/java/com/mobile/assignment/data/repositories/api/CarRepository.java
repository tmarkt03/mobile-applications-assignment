package com.mobile.assignment.data.repositories.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarRepository {
    public List<CarList> getCars() {
        List<CarList> cars = new ArrayList<>();

        return cars;
    }
}
