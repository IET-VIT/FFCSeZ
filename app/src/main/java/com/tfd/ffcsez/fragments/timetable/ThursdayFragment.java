package com.tfd.ffcsez.fragments.timetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ThursdayFragment extends Fragment {

    @BindView(R.id.thursdayRecyclerView) RecyclerView thursdayRecyclerView;
    @BindView(R.id.thursdayAnimation) LottieAnimationView thursdayAnimation;
    @BindView(R.id.thursdayText) TextView thursdayText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thursday, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> thuTimeTable = new ArrayList<>();
        TimeTableAdapter adapter = new TimeTableAdapter(thuTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        thursdayRecyclerView.setLayoutManager(layoutManager);
        thursdayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> thuTimeTableLD = database.timeTableDao().loadTTDetails(1, 3, 8);
        thuTimeTableLD.observe(getActivity(), timeTableData -> {
            adapter.updateAdapter(timeTableData);

            if (timeTableData.size() != 0){
                thursdayAnimation.cancelAnimation();
                thursdayAnimation.setVisibility(View.GONE);
                thursdayText.setVisibility(View.GONE);
            }else{
                thursdayAnimation.playAnimation();
                thursdayAnimation.setVisibility(View.VISIBLE);
                thursdayText.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}