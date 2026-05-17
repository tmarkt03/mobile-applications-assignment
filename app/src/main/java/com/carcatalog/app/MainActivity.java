package com.carcatalog.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.carcatalog.app.databinding.ActivityMainBinding;
import com.carcatalog.app.fragment.CarDetailFragment;
import com.carcatalog.app.fragment.CarListFragment;
import com.carcatalog.app.fragment.FavouritesFragment;
import com.carcatalog.app.fragment.LoginFragment;
import com.carcatalog.app.fragment.SearchFragment;
import com.carcatalog.app.model.Car;

/**
 * SINGLE ACTIVITY
 * Manages two distinct UI states:
 *   1. Auth state  - shows LoginFragment or RegisterFragment (no bottom nav)
 *   2. Main state  - shows CarList / Search / Favourites via BottomNavigationView
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (UserPrefs.getLoggedInUser(this) != null) {
            showMainUI();
        } else {
            showAuthUI();
        }
    }

    public void showAuthUI() {
        binding.authContainer.setVisibility(View.VISIBLE);
        binding.mainContainer.setVisibility(View.GONE);
        binding.bottomNav.setVisibility(View.GONE);

        getSupportFragmentManager().popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        loadFragment(binding.authContainer.getId(), new LoginFragment(), false);
    }

    public void showMainUI() {
        binding.authContainer.setVisibility(View.GONE);
        binding.mainContainer.setVisibility(View.VISIBLE);
        binding.bottomNav.setVisibility(View.VISIBLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        loadFragment(binding.mainContainer.getId(), new CarListFragment(), false);
        binding.bottomNav.setSelectedItemId(R.id.nav_cars);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_cars) {
                fragment = new CarListFragment();
            } else if (itemId == R.id.nav_search) {
                fragment = new SearchFragment();
            } else {
                fragment = new FavouritesFragment();
            }
            getSupportFragmentManager().popBackStack(null,
                    androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            loadFragment(binding.mainContainer.getId(), fragment, false);
            return true;
        });
    }

    public void openCarDetail(Car car) {
        loadFragment(binding.mainContainer.getId(),
                CarDetailFragment.newInstance(car), true);
    }

    private void loadFragment(int containerId, Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction tx =
                getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment);
        if (addToBackStack) {
            tx.addToBackStack(null);
        }
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
