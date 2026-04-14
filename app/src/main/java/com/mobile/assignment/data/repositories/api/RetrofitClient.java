package com.mobile.assignment.data.repositories.api;

import retrofit2.Retrofit;

public class RetrofitClient {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://myfakeapi.com/")
            .build();

    IApiService service = retrofit.create(IApiService.class);
}
