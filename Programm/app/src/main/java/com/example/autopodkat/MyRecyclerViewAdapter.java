package com.example.autopodkat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>
{

    private List<Car> carList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Car> carList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.carList = carList;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recyclerview_item_business, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Car car = carList.get(position);
        holder.carImage.setImageBitmap(car.Photo);
        holder.carMarkText.setText(car.CarMark);
        holder.carModelText.setText(car.CarModel);
        holder.carBodyTypeText.setText(holder.carBodyTypeText.getText()+car.BodyType);
        holder.carTransmissionTypeText.setText(holder.carTransmissionTypeText.getText()+car.TransmissionType);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return carList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView carImage;
        TextView carMarkText, carModelText, carBodyTypeText, carTransmissionTypeText;


        ViewHolder(View itemView)
        {

            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carMarkText = itemView.findViewById(R.id.carMark);
            carModelText = itemView.findViewById(R.id.carModel);
            carBodyTypeText = itemView.findViewById(R.id.carBodyTypeText);
            carTransmissionTypeText = itemView.findViewById(R.id.carTransmissionType);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Car getItem(int id) {
        return carList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}