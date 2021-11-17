package com.example.autopodkat.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.autopodkat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class HomeFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener
{
    AlertDialog alert;
    private int tariffChecked = 0;
    private int transmissionChecked = 0;
    public static String tariffQuery="";
    public static String transmissionQuery="";
    public static IShowDialog showDialog;
    MyRecyclerViewAdapter carAdapter;
    Button clearButton;
    private boolean isTariff = false;
    private boolean isTransmission = false;
    private Tariffs tariffs;
    private String baseRequest = "select cars.carid, cars.carmark, cars.carmodel, cars.descr, cars.bodytype, cars.transmissiontype, cars.photo, cars.hp, cars.volume, tariffs.tariffname, locations.longitude, locations.latitude from cars inner join tariffs on cars.tariffID=tariffs.tariffID inner join locations on cars.locationID=locations.locationID";

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FillAdapter(getContext(), MainActivity.carList, root);
        showDialog = new IShowDialog()
        {
            @Override
            public void showDialog(String[] items, int button)
            {
                ShowDialog(items, button);
            }
        };
        clearButton = root.findViewById(R.id.ClearFilters);
        clearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isTariff || isTransmission)
                {
                    isTransmission = false; isTariff = false;
                    MainActivity.get.get(MainActivity.carList, baseRequest);
                }
            }
        });
        return root;
    }
    void FillAdapter(Context context, List<Car> carList, View view)
    {
        // set up the RecyclerView
        Log.e("here","??");
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerCarBusiness);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        carAdapter = new MyRecyclerViewAdapter(context, carList);
        carAdapter.setClickListener(this);
        recyclerView.setAdapter(carAdapter);
    }

    public void ShowDialog(String[] items, int button)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alert = alertDialog.create();

        switch (button)
        {
            case 1:
            {
                alertDialog.setTitle("choose tariff");
                alertDialog.setSingleChoiceItems(items, tariffChecked, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                            {
                                alert.cancel();
                                tariffChecked = 0;
                                tariffQuery="";
                                isTariff=false;
                                break;
                            }
                            case 1:
                            {
                                alert.cancel();
                                tariffChecked = 1;
                                tariffQuery="business";
                                isTariff=true;
                                break;
                            }
                            case 2:
                            {
                                alert.cancel();
                                tariffChecked = 2;
                                tariffQuery="standard";
                                isTariff=true;
                                break;
                            }
                            case 3:
                            {
                                alert.cancel();
                                tariffChecked = 3;
                                tariffQuery="economy";
                                isTariff=true;
                                break;
                            }
                        }
                        tariffChecked = which;
                    }
                });
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        ManageRequest();
                    }
                });

                alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
                break;
            }
            case 2:
            {
                alertDialog.setTitle("choose transmission");
                alertDialog.setSingleChoiceItems(items, transmissionChecked, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                            {
                                transmissionQuery="";
                                isTransmission=false;
                                break;
                            }
                            case 1:
                            {
                                transmissionQuery="mechanical";
                                isTransmission=true;
                                break;
                            }
                            case 2:
                            {
                                transmissionQuery="automatic";
                                isTransmission=true;
                                break;
                            }
                        }
                        transmissionChecked = which;
                    }
                });
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        ManageRequest();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
                break;
            }
        }

    }
    void ManageRequest()
    {
        Log.e("tra","tr");
        String query = "";
        if (isTariff && isTransmission)
        {
            Log.e("tran","tr");
            query = baseRequest + " where tariffs.tariffID="+ (Tariffs.valueOf(tariffQuery).ordinal()+1) + " and cars.transmissiontype like '" + transmissionQuery + "'";
        }
        else if (isTariff)
        {
            query = baseRequest + " where tariffs.tariffID="+ (Tariffs.valueOf(tariffQuery).ordinal()+1);

        }
        else if (isTransmission)
        {
            Log.e("tr","tr");
            query = baseRequest + " where cars.transmissiontype like '" + transmissionQuery + "'";
        }
        else
        {
            query = baseRequest;
        }
        Log.e("query", query);
        MainActivity.get.get(MainActivity.carList, query);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Toast.makeText(getContext(), "You clicked " + carAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }
}
