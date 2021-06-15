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

public class WednesdayFragment extends Fragment {
    RecyclerView wednesdayRecyclerView;
    LottieAnimationView wednesdayAnimation;
    TextView wednesdayText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);

        wednesdayAnimation = view.findViewById(R.id.wednesdayAnimation);
        wednesdayRecyclerView = view.findViewById(R.id.wednesdayRecyclerView);
        wednesdayText = view.findViewById(R.id.wednesdayText);
        return view;
    }
}