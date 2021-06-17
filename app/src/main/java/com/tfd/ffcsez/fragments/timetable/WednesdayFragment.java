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


public class WednesdayFragment extends Fragment {

    @BindView(R.id.wednesdayRecyclerView) RecyclerView wednesdayRecyclerView;
    @BindView(R.id.wednesdayAnimation) LottieAnimationView wednesdayAnimation;
    @BindView(R.id.wednesdayText) TextView wednesdayText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> wedTimeTable = new ArrayList<>();
        TimeTableAdapter adapter = new TimeTableAdapter(wedTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wednesdayRecyclerView.setLayoutManager(layoutManager);
        wednesdayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> wedTimeTableLD = database.timeTableDao().loadTTDetails(1, 2, 7);
        wedTimeTableLD.observe(getActivity(), timeTableData -> {
            adapter.updateAdapter(timeTableData);

            if (timeTableData.size() != 0){
                wednesdayAnimation.cancelAnimation();
                wednesdayAnimation.setVisibility(View.GONE);
                wednesdayText.setVisibility(View.GONE);
            }else{
                wednesdayAnimation.playAnimation();
                wednesdayAnimation.setVisibility(View.VISIBLE);
                wednesdayText.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}