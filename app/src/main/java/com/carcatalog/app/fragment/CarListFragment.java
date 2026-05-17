package com.carcatalog.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carcatalog.app.MainActivity;
import com.carcatalog.app.R;
import com.carcatalog.app.UserPrefs;
import com.carcatalog.app.adapter.CarAdapter;
import com.carcatalog.app.databinding.FragmentCarListBinding;
import com.carcatalog.app.model.Car;
import com.carcatalog.app.model.CarResponse;
import com.carcatalog.app.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CAR LIST FRAGMENT
 * Displays all cars fetched from the MyFakeAPI REST endpoint using Retrofit.
 * Replaces CatalogActivity.
 * Tapping a car opens CarDetailFragment.
 */
public class CarListFragment extends Fragment {

    private FragmentCarListBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String user = UserPrefs.getLoggedInUser(requireContext());
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Car Catalog");
            activity.getSupportActionBar()
                    .setSubtitle("Hi, " + (user != null ? user : "User"));
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        binding.rvCars.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCars.setHasFixedSize(true);
        binding.rvCars.setItemAnimator(null);

        fetchCars();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalog, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        UserPrefs.logout(requireContext());
                        ((MainActivity) requireActivity()).showAuthUI();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchCars() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvError.setVisibility(View.GONE);
        binding.tvCarCount.setText("Loading cars...");

        RetrofitClient.getApi().getCars().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(@NonNull Call<CarResponse> call,
                                   @NonNull Response<CarResponse> response) {
                if (binding == null) return;
                if (response.isSuccessful() && response.body() != null
                        && response.body().getCars() != null) {
                    List<Car> allCars = response.body().getCars();
                    int limit = Math.min(allCars.size(), 150);
                    List<Car> cars = new ArrayList<>(allCars.subList(0, limit));

                    CarAdapter adapter = new CarAdapter(cars,
                            car -> ((MainActivity) requireActivity()).openCarDetail(car));
                    binding.rvCars.setAdapter(adapter);
                    binding.tvCarCount.setText("Showing " + cars.size() + " cars");
                    binding.tvError.setVisibility(View.GONE);
                } else {
                    showError("Could not load cars.\n\nCheck your connection and try again.");
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<CarResponse> call, @NonNull Throwable t) {
                if (binding == null) return;
                showError("Could not load cars.\n\nCheck your connection and try again.\n\n"
                        + t.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showError(String msg) {
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(msg);
        binding.tvCarCount.setText("Failed to load");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
