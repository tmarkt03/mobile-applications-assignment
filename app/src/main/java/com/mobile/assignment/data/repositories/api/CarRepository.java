package com.mobile.assignment.data.repositories.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarRepository implements IApiService {
    public Call<CarList> getAll() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://myfakeapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiService service = retrofit.create(IApiService.class);

        Call<CarList> call =
                service.getAll();

        List<CarList> cars = new ArrayList<>();
        call.enqueue(new Callback<CarList>() {
            @Override
            public void onResponse(Call<CarList> call, Response<CarList> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().list.size(); i++) {
                        int id = response.body()
                                .list.get(i)
                                .id;
                        String carMake = response.body()
                                .list.get(i)
                                .carMake;
                        String carModel = response.body()
                                .list.get(i)
                                .carModel;
                        String color = response.body()
                                .list.get(i)
                                .color;
                        int year = response.body()
                                .list.get(i)
                                .year;
                        String vin = response.body()
                                .list.get(i)
                                .vin;
                        String price = response.body()
                                .list.get(i)
                                .price;
                        boolean availability = response.body()
                                .list.get(i)
                                .availability;
                        cars.add(new CarList(id, carMake, carModel, color, year, vin, price, availability));
                    }
                }

            }

            @Override
            public void onFailure(Call<CarList> call, Throwable t) {

                Log.e("MainActivity", t.getMessage());
            }
        });
        return (Call<CarList>) cars;
    }
}
