package com.example.autopodkat;

import java.util.Date;

public class Order
{
    public int UserID;
    public int CarID;
    public Date StartDate;
    public Date EndDate;
    public int AmountHours;
    public float TotalPrice;

    public Order(int userID, int carID, Date startDate, Date endDate, int amountHours)
    {
        UserID = userID;
        CarID = carID;
        StartDate = startDate;
        EndDate = endDate;
        AmountHours = amountHours;
    }
}
