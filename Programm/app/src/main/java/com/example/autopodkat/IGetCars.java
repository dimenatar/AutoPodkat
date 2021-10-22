package com.example.autopodkat;

import android.content.Context;

import java.util.List;

public interface IGetCars
{
    void get(Context context, List<Car> listCar, String request);
}
