package com.mobile.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.assignment.data.repositories.api.CarItem;
import com.mobile.assignment.data.repositories.api.CarList;
import com.mobile.assignment.data.repositories.api.IApiService;
import com.mobile.assignment.data.repositories.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final List<CarItem> carItemList = new ArrayList<>();
    private CarAdapter adapter;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView carRecyclerView = findViewById(R.id.carRecyclerView);
        logoutButton = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();

        adapter = new CarAdapter(carItemList);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        carRecyclerView.setAdapter(adapter);

        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IApiService service = RetrofitClient.getApiService();

        service.getAll().enqueue(new Callback<CarList>() {
            @Override
            public void onResponse(Call<CarList> call, Response<CarList> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().list == null) {
                    Log.e("MainActivity", "Unable to load cars. Code: " + response.code());
                    Toast.makeText(MainActivity.this, "Unable to load cars right now.", Toast.LENGTH_SHORT).show();
                    return;
                }

                carItemList.clear();
                carItemList.addAll(response.body().list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CarList> call, Throwable t) {
                Log.e("MainActivity", "Failed to load cars", t);
                Toast.makeText(MainActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}