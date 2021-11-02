package com.example.autopodkat;

import java.io.Serializable;

public class Location implements Serializable
{
    public double Latitude;
    public double Longitude;
    public Location(double Latitude, double Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude=Longitude;
    }
}
