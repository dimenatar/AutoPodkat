package com.example.autopodkat;

import android.graphics.Bitmap;
import android.util.Log;

public class Car
{
    public Car(String carMar,String carModel, String description, String bodyType, String transmissionType, Bitmap photo, int hp, double volume, Location location, String tariff)
    {
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
    public String CarMark;
    public String CarModel;
    public String Description;
    public String BodyType;
    public String TransmissionType;
    public Bitmap Photo;
    public int HP;
    public double Volume;
    public Location Location;
    public String Tariff;
    public void Print()
    {
        Log.e("out", "CarMark: " + CarMark + " ,CarModel; " + CarModel + "Description: " + Description +"BodyType: " + BodyType + "TransmissionType: " + TransmissionType);
    }

}
