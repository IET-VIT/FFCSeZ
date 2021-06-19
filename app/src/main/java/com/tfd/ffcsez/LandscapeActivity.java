package com.tfd.ffcsez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.tfd.ffcsez.adapters.LandscapePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandscapeActivity extends AppCompatActivity {

    @BindView(R.id.landscapeTabLayout) TabLayout landscapeTabLayout;
    @BindView(R.id.landscapeContainer) ViewPager2 container;
    @BindView(R.id.backToHome) ImageButton backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landscape);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        LandscapePagerAdapter landscapePagerAdapter = new LandscapePagerAdapter(fragmentManager, getLifecycle());
        container.setAdapter(landscapePagerAdapter);

        backToHome.setOnClickListener(v -> {
            startActivity(new Intent(LandscapeActivity.this, MainActivity.class));
            finish();
        });

        landscapeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                landscapeTabLayout.selectTab(landscapeTabLayout.getTabAt(position));

            }
        });
    }
}