package com.tfd.ffcsez.fragments.timetablefragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.R;
public class FridayFragment extends Fragment {

    RecyclerView fridayRecyclerView;
    LottieAnimationView fridayAnimation;
    TextView fridayText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friday, container, false);
        fridayAnimation = view.findViewById(R.id.tuesdayAnimation);
        fridayRecyclerView = view.findViewById(R.id.tuesdayRecyclerView);
        fridayText = view.findViewById(R.id.tuesdayText);
        return view;
    }
}