package com.mobile.assignment;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_recycler_view);

        RecyclerView carRecyclerView =
                findViewById(R.id.carRecyclerView);

        IApiService service = new CarRepository();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}