package com.carcatalog.app.network;

import com.carcatalog.app.model.CarResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    /**
     * Fetches all cars from MyFakeAPI.
     * Full URL: https://myfakeapi.com/api/cars/
     */
    @GET("cars/")
    Call<CarResponse> getCars();
}
