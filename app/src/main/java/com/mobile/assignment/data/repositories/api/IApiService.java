package com.mobile.assignment.data.repositories.api;


import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService{
    @GET("api/cars")
    Call<CarList> getAll();

}
