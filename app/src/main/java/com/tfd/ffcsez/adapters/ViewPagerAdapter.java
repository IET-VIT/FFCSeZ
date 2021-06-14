package com.tfd.ffcsez.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tfd.ffcsez.fragments.getstartedfragments.FragmentA;
import com.tfd.ffcsez.fragments.getstartedfragments.FragmentB;
import com.tfd.ffcsez.fragments.getstartedfragments.FragmentC;
import com.tfd.ffcsez.fragments.getstartedfragments.FragmentD;
import com.tfd.ffcsez.fragments.getstartedfragments.FragmentE;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FragmentA();
            case 1: return new FragmentB();
            case 2: return new FragmentC();
            case 3: return new FragmentD();
            case 4: return new FragmentE();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
