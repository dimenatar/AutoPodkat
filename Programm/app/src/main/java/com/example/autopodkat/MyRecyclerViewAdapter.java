package com.example.autopodkat;

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

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>
{

    private List<Car> carList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Car> carList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.carList = carList;
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        view = mInflater.inflate(R.layout.recyclerview_car, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Car car = carList.get(position);
        holder.carImage.setImageBitmap(car.Photo.currentImage);
        holder.carMarkText.setText(car.CarMark);
        holder.carModelText.setText(car.CarModel);
        holder.carTariff.setText(holder.carTariff.getText() + car.Tariff);
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
        TextView carMarkText, carModelText, carTariff;
        ViewHolder(View itemView)
        {

            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carMarkText = itemView.findViewById(R.id.carMark);
            carModelText = itemView.findViewById(R.id.carModel);
            carTariff = itemView.findViewById(R.id.tariffMode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null)

                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Car getItem(int id) {
        return carList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Log.e("click","click");
                Car car = getItem(position);
                Intent intent = new Intent(mContext, DetailerCarActivity.class);
                MainActivity.saveBitmap = car.Photo.currentImage;
                intent.putExtra("carID", car.CarID);
                intent.putExtra("carMark", car.CarMark);
                intent.putExtra("carModel", car.CarModel);
                intent.putExtra("Description", car.Description);
                intent.putExtra("BodyType", car.BodyType);
                intent.putExtra("TransmissionType", car.TransmissionType);
                intent.putExtra("HP", car.HP);
                intent.putExtra("Volume", car.Volume);
                intent.putExtra("Location", new double[]{car.Location.Longitude,car.Location.Latitude});
                intent.putExtra("carTariff", car.Tariff);



                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);
            }
        };
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}