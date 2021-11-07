package com.example.autopodkat;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

public class Car implements Serializable
{
    public Car(int carID, String carMar,String carModel, String description, String bodyType, String transmissionType, BitmapDataObject photo, int hp, double volume, Location location, String tariff)
    {
        CarID = carID;
        CarMark = carMar;
        CarModel = carModel;
        Description = description;
        BodyType = bodyType;
        TransmissionType = transmissionType;
        Photo = photo;
        HP = hp;
        Volume = volume;
        this.Location = location;
        Tariff = tariff;
    }
    public int CarID;
    public String CarMark;
    public String CarModel;
    public String Description;
    public String BodyType;
    public String TransmissionType;
    public BitmapDataObject Photo;
    public int HP;
    public double Volume;
    public Location Location;
    public String Tariff;
    public void Print()
    {
        Log.e("out", "CarMark: " + CarMark + " ,CarModel; " + CarModel + "Description: " + Description +"BodyType: " + BodyType + "TransmissionType: " + TransmissionType);
    }

}
