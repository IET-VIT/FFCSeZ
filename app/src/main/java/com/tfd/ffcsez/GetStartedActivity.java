package com.tfd.ffcsez;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cuberto.liquid_swipe.LiquidPager;
import com.tfd.ffcsez.adapters.ViewPagerAdapter;

public class GetStartedActivity extends AppCompatActivity {
    LiquidPager pager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        pager = findViewById(R.id.pager);
        pager.setButtonDrawable(R.drawable.next_button);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        pager.setAdapter(viewPagerAdapter);
    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal format
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}