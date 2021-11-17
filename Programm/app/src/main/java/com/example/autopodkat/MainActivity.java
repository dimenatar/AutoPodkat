package com.example.autopodkat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
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
    private TextView userNameHeader;
    private MenuItem logMenuItem;
    Button tariff, transmissionTypeButton;
    Context context;
    private MyRecyclerViewAdapter carAdapter;
    public static User user = null;
    private String carRequest = "select cars.carid,cars.carmark, cars.carmodel, cars.descr, cars.bodytype, cars.transmissiontype, cars.photo, cars.hp, cars.volume, tariffs.tariffname, locations.longitude, locations.latitude from cars inner join tariffs on cars.tariffID=tariffs.tariffID inner join locations on cars.locationID=locations.locationID";
    private String orderRequest = "select userid, carid, startdate, enddate, amounthoures,totalprice from orders";
    String BASE_URL = "http://192.168.0.102/getCars.php";
    public static List<Car> carList = new ArrayList<>();
    public static List<Order> orderList = new ArrayList<>();
    private AppBarConfiguration mAppBarConfiguration;
    public static IGetCars get;
    public static IFillAdapter fill;
    public static IGetOrders getOrders;
    public static ISetName iSetName;
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
        getOrders = new IGetOrders() {
            @Override
            public void get_orders(String request) {
                GetOrders(request);
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        userNameHeader=navigationView.getHeaderView(0).findViewById(R.id.userName_header);
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

        transmissionTypeButton = findViewById(R.id.transmissionButton);
        transmissionTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.showDialog.showDialog(new String[]{"all","mechanical","automatic"}, 2);
            }
        });
        user = SaveManager.LoadUser(this);
        iSetName = new ISetName()
        {
            @Override
            public void SetName(String text, boolean isUser)
            {
                if (!isUser)

                {
                    logMenuItem.setTitle("Log in");
                    Log.e("out","out");
                }
                else logMenuItem.setTitle("Log out");
                userNameHeader.setText(text);
            }
        };
        if (MainActivity.user != null) {
            getOrders.get_orders(orderRequest + " where UserID = " + MainActivity.user.UserID);
            Log.e("get","orders");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        logMenuItem = menu.findItem(R.id.action_log);
        if (user != null)
        {
            userNameHeader.setText(user.UserName);
            logMenuItem.setTitle("Log out");
        }
        else
        {
           userNameHeader.setText("AutoPodkat");
            logMenuItem.setTitle("Log in");

        }
        logMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (user != null)
                {
                    if (logMenuItem == item)
                    {
                        //удалить инфу
                        user = null;
                        userNameHeader.setText("AutoPodkat");
                        logMenuItem.setTitle("Log in");
                        SaveManager.EraseUser(getApplicationContext());
                    }
                }
                else
                {
                    if (logMenuItem == item)
                    {
                        Intent intent = new Intent(MainActivity.this, AuthorisationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        MainActivity.this.startActivity(intent);
                    }
                }
                return false;
            }
        });
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
                                Log.e("carresponse", response);
                                JSONArray array = new JSONArray(response);
                                carList = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    carList.add(new Car(object.getInt("carid"), object.getString("carmark"), object.getString("carmodel"), object.getString("descr"), object.getString("bodytype"), object.getString("transmissiontype"), new BitmapDataObject(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(object.getString("photo"), "drawable", getPackageName()))), object.getInt("hp"), object.getDouble("volume"), new Location(object.getDouble("longitude"), object.getDouble("latitude")), object.getString("tariff")));
                                }
                                FillCarAdapter(context, carList);
                                saveCarsThread thread = new saveCarsThread(context);
                                thread.start();
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
                            Toast.makeText(context, "Unable connect to server. Loading saved data...", Toast.LENGTH_LONG).show();
                            loadCarsThread thread = new loadCarsThread(context, MainActivity.this);
                            thread.start();
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
            List<Car> tempList = SaveManager.LoadCars(context);
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
        if (user != null) {
            if (isConnected(MainActivity.this)) {
                RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.0.102/getOrders.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.e("orderResponse", response);
                                    JSONArray array = new JSONArray(response);
                                    orderList = new ArrayList<>();

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        orderList.add(new Order(object.getInt("UserID"), object.getInt("CarID"), new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("StartDate")), new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("EndDate")), object.getInt("AmountHoures"), (float) object.getDouble("TotalPrice")));
                                    }
                                    saveOrdersThread thread = new saveOrdersThread(context);
                                    thread.start();
                                } catch (Exception e) {
                                    Log.e("errH", e.getMessage() + "  " + e.getLocalizedMessage());
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loadOrdersThread thread = new loadOrdersThread(context);
                                thread.start();
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
            } else {
                // do warning
                // load from saved
            }
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
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        } catch (Exception e)
        {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return false;
    }

}
class loadCarsThread extends Thread
{
    Context mContext;
    Activity mActivity;
    public loadCarsThread(Context context, Activity activity)
    {
        mContext = context;
        mActivity = activity;
    }
    @Override
    public void run()
    {

        List<Car> tempList = SaveManager.LoadCars(mContext);
        if (tempList != null)
        {
            MainActivity.carList = tempList;
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    MainActivity.fill.Fill(mContext, MainActivity.carList);
                }
            });

        }
    }

}
class loadOrdersThread extends Thread
{
    Context mContext;
    Activity mActivity;
    public loadOrdersThread(Context context)
    {
        mContext = context;
    }
    @Override
    public void run()
    {
        List<Order> tempList = SaveManager.LoadOrders(mContext);
        if (tempList != null)
        {
            MainActivity.orderList = tempList;
        }
    }

}
class saveCarsThread extends Thread
{
    Context mContext;

    public saveCarsThread(Context context)
    {
        mContext = context;
    }
    @Override
    public void run()
    {
        SaveManager.SaveCars(MainActivity.carList, mContext);
    }

}
class saveOrdersThread extends Thread
{
    Context mContext;
    public saveOrdersThread(Context context)
    {
        mContext = context;
    }
    @Override
    public void run()
    {
        SaveManager.SaveOrder(MainActivity.orderList, mContext);
    }
}