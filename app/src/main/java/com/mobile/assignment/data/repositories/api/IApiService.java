package com.mobile.assignment.data.repositories.api;

import com.mobile.assignment.domain.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {
    @GET("/api/cars")
    Call<List<Car>> listCars();

}
