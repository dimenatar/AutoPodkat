package com.example.autopodkat.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.autopodkat.Car;
import com.example.autopodkat.MainActivity;
import com.example.autopodkat.MyRecyclerViewAdapter;
import com.example.autopodkat.R;

import java.util.List;
import java.util.concurrent.*;

public class HomeFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener
{
    MyRecyclerViewAdapter adapter;
    private String request = "select carmark, carmodel, descr, bodytype, transmissiontype, photo, hp, volume from cars";
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FillAdapter(getContext(), MainActivity.carList, root);



        return root;
    }
    void FillAdapter(Context context, List<Car> carList, View view)
    {
        // set up the RecyclerView
        Log.e("here","??");
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerCarBusiness);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        adapter = new MyRecyclerViewAdapter(context, carList);

        adapter.setClickListener(HomeFragment.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }
}
