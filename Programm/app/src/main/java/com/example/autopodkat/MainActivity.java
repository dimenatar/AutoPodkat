package com.example.autopodkat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.autopodkat.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener
{
    public static Bitmap saveBitmap;


    Button tariff, clearButton, transmissionTypeButton;
    Context context;
    private MyRecyclerViewAdapter carAdapter;

    private String carRequest = "select cars.carid,cars.carmark, cars.carmodel, cars.descr, cars.bodytype, cars.transmissiontype, cars.photo, cars.hp, cars.volume, tariffs.tariffname, locations.longitude, locations.latitude from cars inner join tariffs on cars.tariffID=tariffs.tariffID inner join locations on cars.locationID=locations.locationID";
    private String orderRequest = "select userid, carid, startdate, enddate, amounthoures from orders ";
    String BASE_URL = "http://192.168.0.102/getCars.php";
    public static List<Car> carList = new ArrayList<>();
    public static List<Order> orderList = new ArrayList<>();
    private AppBarConfiguration mAppBarConfiguration;
    public static IGetCars get;
    public static IFillAdapter fill;
    public static IGetOrders getOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        context = getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = new IGetCars()
        {
            @Override
            public void get( List<Car> listCar, String request)
            {
                getCars(context, request);
            }
        };
        fill = new IFillAdapter()
        {
            @Override
            public void Fill(Context context, List<Car> carList)
            {
                FillCarAdapter(context, carList);
            }
        };
        getOrders = (request) -> GetOrders(request);
        getOrders.get_orders( orderRequest);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       // setContentView(R.layout.business_activity);

        get.get(carList, carRequest);

        tariff = findViewById(R.id.tariffIDButton);
        tariff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HomeFragment.showDialog.showDialog(new String[]{"all","business","standard","economy"}, 1);
            }
        });
        clearButton = findViewById(R.id.ClearFilters);
        clearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        transmissionTypeButton = findViewById(R.id.transmissionButton);
        transmissionTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.showDialog.showDialog(new String[]{"all","mechanical","automatic"}, 2);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void getCars(Context context, String request)
    {
        if (isConnected(MainActivity.this)) {
            RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                carList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    carList.add(new Car(object.getInt("carid"), object.getString("carmark"), object.getString("carmodel"), object.getString("descr"), object.getString("bodytype"), object.getString("transmissiontype"), BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(object.getString("photo"), "drawable", getPackageName())), object.getInt("hp"), object.getDouble("volume"), new Location(object.getDouble("longitude"), object.getDouble("latitude")), object.getString("tariff")));
                                }
                                FillCarAdapter(context, carList);
                                SaveManager.SaveCars(carList, context);
                                for (int i = 0; i < carList.size(); i++) {
                                    carList.get(i).Print();
                                }
                            } catch (Exception e) {
                                Log.e("err", e.getMessage() + "  " + e.getLocalizedMessage());
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(MainActivity.this, "Unable connect to server. Loading saved data...", Toast.LENGTH_LONG).show();
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("query", request);
                    return params;
                }
            };
            Volley.newRequestQueue(MainActivity.this).add(stringRequest1);
        }
        else
        {
            List<Car> tempList = SaveManager.LoadCars();
            if (tempList != null)
            {
                FillCarAdapter(MainActivity.this, tempList);
            }
            else
            {
                Toast.makeText(this, "Nothing is saved", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void GetOrders(String request)
    {
        if (isConnected(MainActivity.this))
        {
            RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.0.102/getOrders.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                orderList = new ArrayList<>();
                                Log.e("resp", response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    orderList.add(new Order(object.getInt("UserID"), object.getInt("CarID"), new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("StartDate")), new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("EndDate")), object.getInt("UserID")));
                                }

                            } catch (Exception e) {
                                Log.e("err", e.getMessage() + "  " + e.getLocalizedMessage());
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("query", request);
                    return params;
                }
            };
            Volley.newRequestQueue(MainActivity.this).add(stringRequest1);
        }
        else
        {
            // do warning
            // load from saved
        }
    }
    void FillCarAdapter(Context context, List<Car> carList)
    {
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.RecyclerCarBusiness);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        carAdapter = new MyRecyclerViewAdapter(context, carList);
        carAdapter.setClickListener(this);
        recyclerView.setAdapter(carAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + carAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }
    public static boolean isConnected(Context context)
    {
        boolean connected = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e)
        {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}
