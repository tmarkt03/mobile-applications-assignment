package com.mobile.assignment.data.repositories;

import com.mobile.assignment.data.repositories.api.IApiService;
import com.mobile.assignment.data.repositories.api.RetrofitClient;
import com.mobile.assignment.domain.Car;

import java.util.List;

import retrofit2.Call;

public class ApiRepository {
    private IApiService apiService;
    public ApiRepository() {
        this.apiService = RetrofitClient.getApiService();
    }
    public void fetchPosts(RepositoryCallback<List<Car>> callback) {
        Call<List<Car>> listCars = apiService.listCars();
        listCars.enqueue(new retrofit2.Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call,
                                   retrofit2.Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onError("Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}
