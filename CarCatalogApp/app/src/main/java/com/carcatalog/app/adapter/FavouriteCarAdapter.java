package com.carcatalog.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carcatalog.app.databinding.ItemFavouriteCarBinding;
import com.carcatalog.app.db.FavouriteCar;

import java.util.List;

/**
 * RecyclerView adapter for the Favourites list.
 * Displays favourite cars stored in Room and provides a "Remove" button.
 */
public class FavouriteCarAdapter
        extends RecyclerView.Adapter<FavouriteCarAdapter.FavViewHolder> {

    public interface OnRemoveListener {
        void onRemove(FavouriteCar car);
    }

    private final List<FavouriteCar> items;
    private final OnRemoveListener listener;

    public FavouriteCarAdapter(List<FavouriteCar> items, OnRemoveListener listener) {
        this.items = items;
        this.listener = listener;
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        final ItemFavouriteCarBinding binding;

        FavViewHolder(ItemFavouriteCarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavouriteCarBinding binding = ItemFavouriteCarBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        FavouriteCar car = items.get(position);
        holder.binding.tvFavTitle.setText(car.getTitle());
        holder.binding.tvFavPrice.setText(car.price);
        holder.binding.tvFavColor.setText("Colour: " + car.color);
        holder.binding.btnRemoveFav.setOnClickListener(v -> listener.onRemove(car));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
