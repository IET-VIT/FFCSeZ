package com.tfd.ffcsez.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tfd.ffcsez.fragments.getstarted.FragmentA;
import com.tfd.ffcsez.fragments.getstarted.FragmentB;
import com.tfd.ffcsez.fragments.getstarted.FragmentC;
import com.tfd.ffcsez.fragments.getstarted.FragmentD;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new FragmentA();
            case 1: return new FragmentB();
            case 2: return new FragmentD();
            case 3: return new FragmentC();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
