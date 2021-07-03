package com.tfd.ffcsez;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.cuberto.liquid_swipe.LiquidPager;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.tfd.ffcsez.adapters.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetStartedActivity extends AppCompatActivity {
    @BindView(R.id.wormDotIndicator) WormDotsIndicator wormDotsIndicator;
    @BindView(R.id.pager) ViewPager2 pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.dark_purple));

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        pager.setAdapter(viewPagerAdapter);
        wormDotsIndicator.setViewPager2(pager);
    }

}