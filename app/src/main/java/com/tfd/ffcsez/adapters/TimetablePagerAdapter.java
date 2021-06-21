package com.tfd.ffcsez.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tfd.ffcsez.fragments.timetable.MondayFragment;
import com.tfd.ffcsez.fragments.timetable.ProjectFragment;
import com.tfd.ffcsez.fragments.timetable.ThursdayFragment;
import com.tfd.ffcsez.fragments.timetable.TuesdayFragment;
import com.tfd.ffcsez.fragments.timetable.WednesdayFragment;
import com.tfd.ffcsez.fragments.timetable.FridayFragment;


public class TimetablePagerAdapter extends FragmentStateAdapter {
    public TimetablePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new TuesdayFragment();
            case 2: return new WednesdayFragment();
            case 3: return new ThursdayFragment();
            case 4: return new FridayFragment();
            case 5: return new ProjectFragment();
        }
        return new MondayFragment();
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
