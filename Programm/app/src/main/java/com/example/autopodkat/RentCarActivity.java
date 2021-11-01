package com.example.autopodkat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RentCarActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener {
    private String carMark;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private GoogleMap googleMap;
    private final String BASE_URL = "http://192.168.0.102/createOrder.php";
    private int CarID;
    boolean isFirstButton =false;
    boolean isSecondButton = false;
    private Bitmap carImage;
    private ImageView carView;
    private MapView mMapView;
    private ImageButton infoButton;
    private Button startRideBTN, endRideBTN,takeCar;
    private DatePickerDialog datePickerDialog;
    private Date startDate, endDate;
    private Location carLocation;
    private Location newCarLocation = new Location(-420, -420);
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_car_activity);

        carView = findViewById(R.id.rent_car_image);
        mMapView = findViewById(R.id.rent_car_map);
        carImage = MainActivity.saveBitmap;
        carView.setImageBitmap(carImage);
        startRideBTN = findViewById(R.id.chose_start_ride_date_button);
        endRideBTN = findViewById(R.id.chose_end_ride_date_button);
        initDatePicker();
        Calendar c = Calendar.getInstance();
        startDate = c.getTime();
        startRideBTN.setText(new SimpleDateFormat("dd/MM").format(c.getTime()));
        c.setTime(c.getTime());
        c.add(Calendar.DATE, 1);
        endDate = c.getTime();
        endRideBTN.setText(new SimpleDateFormat("dd/MM").format(c.getTime()));
        startRideBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isFirstButton=true;
                datePickerDialog.show();
            }
        });
        endRideBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isSecondButton=true;
                datePickerDialog.show();
            }
        });
        takeCar = findViewById(R.id.take_car_rent);
        takeCar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (startDate.compareTo(endDate) <0 && newCarLocation.Latitude != 420 && newCarLocation.Longitude != 420)
                {
                    CreateOrder("default","1",String.valueOf(CarID),startDate, endDate, TimeUnit.DAYS.convert(endDate.getTime()-startDate.getTime(), TimeUnit.MILLISECONDS));
                    Toast.makeText(RentCarActivity.this, "Success!", Toast.LENGTH_LONG).show();
                    MainActivity.getOrders.get_orders("select userid, carid, startdate, enddate, amounthoures from orders");
                }
                else if (newCarLocation.Longitude == 420 && newCarLocation.Latitude==420)
                {
                    Toast.makeText(getApplicationContext(), "pick new location!", Toast.LENGTH_LONG).show();
                }
                else if (startDate.compareTo(endDate)>=1)
                {
                    Toast.makeText(RentCarActivity.this, "start date must be earlier then end date!", Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null)
        {
            CarID = intent.getIntExtra("carID", 1);
            carLocation = new Location(intent.getDoubleArrayExtra("Location")[0],intent.getDoubleArrayExtra("Location")[1]);
            carMark = intent.getStringExtra("carMark");
        }
        else CarID = 1;

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try
        {
            MapsInitializer.initialize(RentCarActivity.this.getApplicationContext());
        }
        catch (Exception e)
        {
            Log.e("Map",e.getMessage());
        }
        mMapView.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap carPosition)
            {
                googleMap = carPosition;
                LatLng pos = new LatLng(carLocation.Longitude, carLocation.Latitude);
                googleMap.addMarker(new MarkerOptions().position(pos).title("carMark"));
                CameraPosition cameraPos = new CameraPosition.Builder().target(pos).zoom(10).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
                googleMap.setOnMapClickListener(RentCarActivity.this);

            }
        });
        mToolbar = findViewById(R.id.rent_tool_bar);
        setSupportActionBar(mToolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_button));
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month++;
                if (isFirstButton)
                {
                    startDate.setDate(day);
                    startDate.setMonth(month);
                    startRideBTN.setText(day + "/"+ month);
                    isFirstButton=false;
                }
                else if (isSecondButton)
                {
                    endDate.setDate(day);
                    endDate.setMonth(month);
                    endRideBTN.setText(day + "/"+ month);
                    isSecondButton=false;
                }
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    @Override
    public void onMapClick(LatLng latLng)
    {
        this.googleMap.clear();
        LatLng pos = new LatLng(carLocation.Longitude, carLocation.Latitude);
        googleMap.addMarker(new MarkerOptions().position(pos).title(carMark));
        CameraPosition cameraPos = new CameraPosition.Builder().target(pos).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Destination").icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(carImage, 75,50)));
        this.googleMap.addMarker(marker);
        newCarLocation = new Location(latLng.latitude, latLng.longitude);
    }
    public void CreateOrder(String OrderID, String UserID, String CarID, Date StartDate, Date EndDate, float AmountHoures)
    {

        RequestQueue mRequestQueue = Volley.newRequestQueue(RentCarActivity.this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("Response", response);
                        ChangeLocation(CarID, newCarLocation);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                })
        {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("OrderID", OrderID);
                params.put("UserID", UserID);
                params.put("CarID", CarID);
                params.put("StartDate", new SimpleDateFormat("yyyy-MM-dd").format(startDate));
                params.put("EndDate", new SimpleDateFormat("yyyy-MM-dd").format(endDate));;
                params.put("AmountHoures", String.valueOf(AmountHoures));
                return params;
            }
        };

        Volley.newRequestQueue(RentCarActivity.this).add(stringRequest1);
    }
    public void ChangeLocation(String CarID, Location newLocation)
    {

        RequestQueue mRequestQueue = Volley.newRequestQueue(RentCarActivity.this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.0.102/changeLocation.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("Response", response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                })
        {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("CarID", CarID);
                params.put("Longitude", String.valueOf(newLocation.Longitude));
                params.put("Latitude", String.valueOf(newLocation.Latitude));
                return params;
            }
        };

        Volley.newRequestQueue(RentCarActivity.this).add(stringRequest1);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
