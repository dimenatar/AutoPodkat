package com.example.autopodkat.ui.home;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.example.autopodkat.MainActivity;
import com.example.autopodkat.R;

public class HomeActivity extends FragmentActivity
{
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        Log.e("hereeeee","?");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
