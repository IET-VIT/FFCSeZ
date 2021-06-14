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


public class TuesdayFragment extends Fragment {
    RecyclerView tuesdayRecyclerView;
    LottieAnimationView tuesdayAnimation;
    TextView tuesdayText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        tuesdayAnimation = view.findViewById(R.id.tuesdayAnimation);
        tuesdayRecyclerView = view.findViewById(R.id.tuesdayRecyclerView);
        tuesdayText = view.findViewById(R.id.tuesdayText);
        return view;
    }
}