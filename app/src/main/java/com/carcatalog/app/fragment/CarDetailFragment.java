package com.carcatalog.app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carcatalog.app.R;
import com.carcatalog.app.databinding.FragmentCarDetailBinding;
import com.carcatalog.app.db.AppDatabase;
import com.carcatalog.app.db.FavouriteCar;
import com.carcatalog.app.model.Car;

import java.util.concurrent.Executors;

/**
 * CAR DETAIL FRAGMENT
 * Shows full details for the selected car.
 * Allows the user to add / remove the car from their local Room favourites.
 * "View & Buy" opens a Google search in the system browser.
 */
public class CarDetailFragment extends Fragment {

    private static final String ARG_CAR = "car";

    private FragmentCarDetailBinding binding;
    private Car car;
    private boolean isFavourite = false;

    public static CarDetailFragment newInstance(Car car) {
        CarDetailFragment fragment = new CarDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CAR, car);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            car = (Car) getArguments().getSerializable(ARG_CAR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (car == null) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        // Action bar
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(car.getTitle());
            activity.getSupportActionBar().setSubtitle(null);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Populate UI
        binding.tvDetailTitle.setText(car.getTitle());
        binding.tvDetailPrice.setText(car.getPrice());
        binding.tvDetailColor.setText("Colour: " + car.getColor());
        binding.tvDetailVin.setText("VIN: " + car.getVin());

        if (car.isAvailability()) {
            binding.tvDetailAvailability.setText("Available");
            binding.tvDetailAvailability.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            binding.tvDetailAvailability.setText("Unavailable");
            binding.tvDetailAvailability.setBackgroundColor(Color.parseColor("#C62828"));
        }

        Glide.with(this)
                .load(car.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_car_placeholder)
                .error(R.drawable.ic_car_placeholder)
                .centerCrop()
                .into(binding.ivDetailCar);

        // Check Room for favourite status on a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            isFavourite = AppDatabase.getInstance(requireContext())
                    .favouriteCarDao().isFavourite(car.getId()) > 0;
            if (binding != null) {
                requireActivity().runOnUiThread(this::updateFavButton);
            }
        });

        // Toggle favourite
        binding.btnFavourite.setOnClickListener(v -> toggleFavourite());

        // View & Buy — open Google search in browser
        binding.btnBuy.setOnClickListener(v -> {
            String query = Uri.encode(car.getTitle() + " buy");
            Uri uri = Uri.parse("https://www.google.com/search?q=" + query);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
    }

    private void toggleFavourite() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            if (isFavourite) {
                FavouriteCar fav = new FavouriteCar(car.getId(), car.getBrand(),
                        car.getModel(), car.getColor(), car.getYear(),
                        car.getVin(), car.getPrice());
                db.favouriteCarDao().delete(fav);
                isFavourite = false;
            } else {
                FavouriteCar fav = new FavouriteCar(car.getId(), car.getBrand(),
                        car.getModel(), car.getColor(), car.getYear(),
                        car.getVin(), car.getPrice());
                db.favouriteCarDao().insert(fav);
                isFavourite = true;
            }
            if (binding != null) {
                requireActivity().runOnUiThread(() -> {
                    updateFavButton();
                    Toast.makeText(requireContext(),
                            isFavourite ? "Added to favourites" : "Removed from favourites",
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void updateFavButton() {
        if (binding == null) return;
        if (isFavourite) {
            binding.btnFavourite.setText("\u2605 Remove from Favourites");
        } else {
            binding.btnFavourite.setText("\u2605 Add to Favourites");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
