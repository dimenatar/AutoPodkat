package com.example.autopodkat;

import android.graphics.Bitmap;

public class Car
{
    public Car(String carMar,String carModel, String description, String bodyType, String transmissionType, Bitmap photo, int hp, float volume)
    {
        CarMark = carMar;
        CarModel = carModel;
        Description = description;
        BodyType = bodyType;
        TransmissionType = transmissionType;
        Photo = photo;
        HP = hp;
        Volume = volume;
    }
    public String CarMark;
    public String CarModel;
    public String Description;
    public String BodyType;
    public String TransmissionType;
    public Bitmap Photo;
    public int HP;
    public float Volume;
}
