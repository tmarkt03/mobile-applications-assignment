package com.carcatalog.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carcatalog.app.R;
import com.carcatalog.app.databinding.ItemCarBinding;
import com.carcatalog.app.model.Car;

import java.util.List;

/**
 * RecyclerView adapter for the main car list and search results.
 * Each card is clickable to open the Car Detail screen.
 */
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    public interface OnCarClickListener {
        void onCarClick(Car car);
    }

    private final List<Car> cars;
    private final OnCarClickListener listener;

    public CarAdapter(List<Car> cars, OnCarClickListener listener) {
        this.cars = cars;
        this.listener = listener;
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        final ItemCarBinding binding;

        CarViewHolder(ItemCarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarBinding binding = ItemCarBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);

        holder.binding.tvTitle.setText(car.getTitle());
        holder.binding.tvPrice.setText(car.getPrice());
        holder.binding.tvColor.setText(car.getColor());

        // Availability badge
        if (car.isAvailability()) {
            holder.binding.tvAvailability.setText("Available");
            holder.binding.tvAvailability.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            holder.binding.tvAvailability.setText("Unavailable");
            holder.binding.tvAvailability.setBackgroundColor(Color.parseColor("#C62828"));
        }

        // Car image via Glide
        Glide.with(holder.binding.ivCar.getContext())
                .load(car.getImageUrl())
                .thumbnail(0.25f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_car_placeholder)
                .error(R.drawable.ic_car_placeholder)
                .centerCrop()
                .into(holder.binding.ivCar);

        // Whole card click -> open detail
        holder.itemView.setOnClickListener(v -> listener.onCarClick(car));

        // "View & Buy" button also opens detail (user can buy from detail screen)
        holder.binding.btnBuy.setOnClickListener(v -> listener.onCarClick(car));
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
