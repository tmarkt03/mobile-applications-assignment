package com.mobile.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.assignment.data.repositories.api.CarItem;
import com.mobile.assignment.data.repositories.api.CarList;
import com.mobile.assignment.data.repositories.api.CarRepository;
import com.mobile.assignment.data.repositories.api.IApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    List<CarItem> carItemList;
    CarAdapter adapter;
    Button Logout;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        RecyclerView carRecyclerView =
                findViewById(R.id.carRecyclerView);

        carItemList = new ArrayList<>();
        adapter = new CarAdapter(carItemList);
        carRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());

        carRecyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://myfakeapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiService service = retrofit.create(IApiService.class);

        Call<CarList> call = service.getAll();

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
                                     carItemList.add(new CarItem(id, carMake, carModel, color, year, vin, price, availability));


                                 }

                                 adapter.notifyDataSetChanged();
                             }

                         }

                         @Override
                         public void onFailure(Call<CarList> call, Throwable t) {

                             Log.e("MainActivity", t.getMessage());
                         }
        });
        setContentView(R.layout.activity_main);

        Logout = findViewById(R.id.logout);
        fAuth = FirebaseAuth.getInstance();

        Logout.setOnClickListener(v -> {
            fAuth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
        });

        }

}