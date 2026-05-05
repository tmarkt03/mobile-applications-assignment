package com.mobile.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.assignment.data.repositories.api.CarItem;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarListHolder> {

    private List<CarItem> car;

    public CarAdapter(List<CarItem> car) {this.car = car;}

    @NonNull
    @Override
    public CarListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_single, parent, false);
        return new CarListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListHolder holder, int position) {
        CarItem list = car.get(position);
        holder.idTextView.setText(list.getId());
        holder.modelTextView.setText(list.getCarModel());
        holder.makeTextView.setText(list.getCarMake());
        holder.priceTextView.setText(list.getPrice());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), list.getId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CarListHolder extends RecyclerView.ViewHolder{

        public TextView modelTextView;
        public TextView makeTextView;
        public TextView idTextView;
        public TextView priceTextView;
        public CarListHolder(@NonNull View itemView) {
            super(itemView);
            modelTextView = itemView.findViewById(R.id.modelTextView);
            makeTextView = itemView.findViewById(R.id.makeTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
