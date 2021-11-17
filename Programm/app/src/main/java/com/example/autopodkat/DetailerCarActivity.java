package com.example.autopodkat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.jetbrains.annotations.NotNull;

public class DetailerCarActivity extends AppCompatActivity
{
    private GoogleMap googleMap;
    private String API = "AIzaSyDGCh8gwsFfAheslv1rrziSBcI7rZE8kOM";
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView carImage;
    private TextView transmission, body, volume, hp, description;
    private MapView carPosition;
    private Button takeCarButton;
    private Location carLocation;
    private int CarID, carTariff;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_car_activity);

        carImage = findViewById(R.id.detailed_car_image);
        mToolbar = findViewById(R.id.toolbar);
        transmission = findViewById(R.id.detailed_car_transmission_type);
        body = findViewById(R.id.detailed_car_body);
        volume = findViewById(R.id.detailed_car_volume);
        hp = findViewById(R.id.detailed_car_hp);
        description = findViewById(R.id.detailed_descr);
        carPosition = findViewById(R.id.carPosition);
        takeCarButton = findViewById(R.id.take_car_button);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_button));
        Intent intent = getIntent();


        if (intent != null)
        {
            carImage.setImageBitmap(MainActivity.saveBitmap);
            transmission.setText(transmission.getText() + " " + intent.getStringExtra("TransmissionType"));
            body.setText(body.getText() + " " + intent.getStringExtra("BodyType"));
            volume.setText(volume.getText() + " " + intent.getDoubleExtra("Volume",0));
            hp.setText(hp.getText() + " " + intent.getIntExtra("HP",0));
            description.setText(description.getText() + " " + intent.getStringExtra("Description"));
            carLocation = new Location(intent.getDoubleArrayExtra("Location")[0], intent.getDoubleArrayExtra("Location")[1]);
            mToolbar.setTitle("123");
            mActionBar.setTitle(intent.getStringExtra("carMark") + "  " + intent.getStringExtra("carModel"));
            CarID=intent.getIntExtra("carID",1);
            carTariff = intent.getIntExtra("carTariff", 1);
        }
        carPosition.onCreate(savedInstanceState);
        carPosition.onResume();
        try
        {
            MapsInitializer.initialize(DetailerCarActivity.this.getApplicationContext());
        }
        catch (Exception e)
        {
            Log.e("Map",e.getMessage());
        }
        carPosition.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap carPosition)
            {
                googleMap = carPosition;
                LatLng pos = new LatLng(carLocation.Latitude, carLocation.Longitude);
                googleMap.addMarker(new MarkerOptions().position(pos).title(intent.getStringExtra("carMark")));
                CameraPosition cameraPos = new CameraPosition.Builder().target(pos).zoom(10).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
            }
        });
        takeCarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(DetailerCarActivity.this, RentCarActivity.class);
                intent1.putExtra("carID", CarID);
                intent1.putExtra("carTariff", carTariff);
                intent1.putExtra("carMark", intent.getStringExtra("carMark"));
                intent1.putExtra("Location",new double[]{carLocation.Longitude, carLocation.Latitude});
                intent1.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                DetailerCarActivity.this.startActivity(intent1);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        carPosition.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        carPosition.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        carPosition.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        carPosition.onLowMemory();
    }

}
