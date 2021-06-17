package com.tfd.ffcsez.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tfd.ffcsez.fragments.landscapefragments.AfternoonFragment;
import com.tfd.ffcsez.fragments.landscapefragments.MorningFragment;


public class LandscapePagerAdapter extends FragmentStateAdapter {
    public LandscapePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new AfternoonFragment();
        }
        return new MorningFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
