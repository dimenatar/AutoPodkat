package com.example.autopodkat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class RidesRecyclerAdapter extends RecyclerView.Adapter<RidesRecyclerAdapter.ViewHolder>
{

    private List<Order> ridesList;
    private LayoutInflater mInflater;
    private Context mContext;
    // data is passed into the constructor
    public RidesRecyclerAdapter(Context context, List<Order> ridesList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.ridesList = ridesList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        view = mInflater.inflate(R.layout.recyclerview_orders, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Order order = ridesList.get(position);
        // здесь могут быть баги
        Car car = null;
        
        for (int i =0; i < MainActivity.carList.size(); i ++)
        {
            if (MainActivity.carList.get(i).CarID == order.CarID)
            {
                car = MainActivity.carList.get(i);
            }
        }
        
        holder.carMark.setText(car.CarMark + " " + car.CarModel);
        holder.startDate.setText(new SimpleDateFormat("dd-MM").format(order.StartDate));
        holder.endDate.setText(new SimpleDateFormat("dd-MM").format(order.EndDate));
        holder.amountHours.setText(String.valueOf(order.AmountHours));
        holder.totalPrice.setText(String.valueOf(order.TotalPrice));
    }

    // total number of rows
    @Override
    public int getItemCount() {return ridesList.size(); }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView carMark, startDate, endDate, amountHours, totalPrice;


        ViewHolder(View itemView)
        {

            super(itemView);
            carMark = itemView.findViewById(R.id.recycler_carMark);
            startDate = itemView.findViewById(R.id.recycler_startDate);
            endDate = itemView.findViewById(R.id.recycler_endDate);
            amountHours = itemView.findViewById(R.id.recycler_amountHours);
            totalPrice = itemView.findViewById(R.id.recycler_totalPrice);
        }
    }

}