package com.mobile.assignment.data.repositories;

import java.util.ArrayList;
import java.util.List;

import com.mobile.assignment.data.repositories.api.IApiService;
import com.mobile.assignment.domain.Car;
public class CarsRepository implements ICarsRepository {


    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();

        return cars;
    }
}
