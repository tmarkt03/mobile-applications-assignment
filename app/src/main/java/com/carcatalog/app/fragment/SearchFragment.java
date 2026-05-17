package com.carcatalog.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carcatalog.app.MainActivity;
import com.carcatalog.app.adapter.CarAdapter;
import com.carcatalog.app.databinding.FragmentSearchBinding;
import com.carcatalog.app.model.Car;
import com.carcatalog.app.model.CarResponse;
import com.carcatalog.app.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SEARCH FRAGMENT
 * Fetches all cars from the API and filters them client-side by brand or model.
 * Displays results in a RecyclerView; tapping a result opens CarDetailFragment.
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private List<Car> allCars = new ArrayList<>();
    private boolean dataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Search Cars");
            activity.getSupportActionBar().setSubtitle(null);
        }

        binding.rvSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSearch.setHasFixedSize(true);

        binding.btnSearch.setOnClickListener(v -> performSearch());
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Pre-load all cars in the background
        loadAllCars();
    }

    private void loadAllCars() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getApi().getCars().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(@NonNull Call<CarResponse> call,
                                   @NonNull Response<CarResponse> response) {
                if (binding == null) return;
                binding.searchProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null
                        && response.body().getCars() != null) {
                    allCars = response.body().getCars();
                    dataLoaded = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarResponse> call, @NonNull Throwable t) {
                if (binding != null) {
                    binding.searchProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void performSearch() {
        if (binding == null) return;
        String query = binding.etSearch.getText() == null ? ""
                : binding.etSearch.getText().toString().trim().toLowerCase(Locale.ROOT);

        if (query.isEmpty()) {
            binding.tvSearchEmpty.setText("Enter a brand or model to search");
            binding.tvSearchEmpty.setVisibility(View.VISIBLE);
            binding.tvSearchCount.setVisibility(View.GONE);
            binding.rvSearch.setAdapter(null);
            return;
        }

        List<Car> results = new ArrayList<>();
        for (Car car : allCars) {
            if (car.getBrand().toLowerCase(Locale.ROOT).contains(query)
                    || car.getModel().toLowerCase(Locale.ROOT).contains(query)) {
                results.add(car);
            }
        }

        binding.tvSearchEmpty.setVisibility(View.GONE);
        binding.tvSearchCount.setVisibility(View.VISIBLE);
        binding.tvSearchCount.setText(results.size() + " result(s) for \"" + query + "\"");

        CarAdapter adapter = new CarAdapter(results,
                car -> ((MainActivity) requireActivity()).openCarDetail(car));
        binding.rvSearch.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
