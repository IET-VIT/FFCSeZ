package com.tfd.ffcsez.fragments.timetablefragments;

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


public class ProjectFragment extends Fragment {

    @BindView(R.id.projectRecyclerView) RecyclerView projectRecyclerView;
    @BindView(R.id.projectAnimation) LottieAnimationView projectAnimation;
    @BindView(R.id.projectText) TextView projectText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> projectTimeTable = new ArrayList<>();
        TimeTableAdapter adapter = new TimeTableAdapter(projectTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        projectRecyclerView.setLayoutManager(layoutManager);
        projectRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> projectTimeTableLD = database.timeTableDao().loadTTDetails(1, -1, -1);
        projectTimeTableLD.observe(getActivity(), timeTableData -> {
            adapter.updateAdapter(timeTableData);

            if (timeTableData.size() != 0){
                projectAnimation.cancelAnimation();
                projectAnimation.setVisibility(View.GONE);
                projectText.setVisibility(View.GONE);
            }else{
                projectAnimation.playAnimation();
                projectAnimation.setVisibility(View.VISIBLE);
                projectText.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}