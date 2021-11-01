package com.example.autopodkat.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.autopodkat.MainActivity;
import com.example.autopodkat.Order;
import com.example.autopodkat.R;
import com.example.autopodkat.RidesRecyclerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryFragment extends Fragment
{
    private RidesRecyclerAdapter orderAdapter;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        Log.e("inflater","daaa");
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        return root;
    }
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState)
    {
        FillOrderAdapter(getContext(), MainActivity.orderList);
    }
    void FillOrderAdapter(Context context, List<Order> orderList)
    {
        Log.e("hereee","?///?");
        RecyclerView recyclerView = getView().findViewById(R.id.order_recycleView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        orderAdapter = new RidesRecyclerAdapter(context, orderList);
        recyclerView.setAdapter(orderAdapter);
    }
}