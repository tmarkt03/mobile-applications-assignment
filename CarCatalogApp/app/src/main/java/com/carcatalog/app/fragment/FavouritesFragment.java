package com.carcatalog.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carcatalog.app.adapter.FavouriteCarAdapter;
import com.carcatalog.app.databinding.FragmentFavouritesBinding;
import com.carcatalog.app.db.AppDatabase;
import com.carcatalog.app.db.FavouriteCar;

import java.util.concurrent.Executors;

/**
 * FAVOURITES FRAGMENT
 * Displays the user's locally stored favourite cars from Room.
 * Supports deleting individual entries.
 * Observed via LiveData — list updates automatically.
 */
public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Favourites");
            activity.getSupportActionBar().setSubtitle(null);
        }

        binding.rvFavourites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavourites.setHasFixedSize(false);

        // Observe Room LiveData — auto-updates when items are added/removed
        AppDatabase.getInstance(requireContext())
                .favouriteCarDao()
                .getAll()
                .observe(getViewLifecycleOwner(), favourites -> {
                    if (binding == null) return;
                    if (favourites == null || favourites.isEmpty()) {
                        binding.rvFavourites.setVisibility(View.GONE);
                        binding.tvFavEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.rvFavourites.setVisibility(View.VISIBLE);
                        binding.tvFavEmpty.setVisibility(View.GONE);
                        binding.rvFavourites.setAdapter(
                                new FavouriteCarAdapter(favourites, this::deleteFavourite));
                    }
                });
    }

    private void deleteFavourite(FavouriteCar car) {
        Executors.newSingleThreadExecutor().execute(() ->
                AppDatabase.getInstance(requireContext())
                        .favouriteCarDao()
                        .delete(car)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
