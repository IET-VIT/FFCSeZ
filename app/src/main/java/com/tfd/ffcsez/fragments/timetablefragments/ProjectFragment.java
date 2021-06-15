package com.tfd.ffcsez.fragments.timetablefragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends Fragment {

    RecyclerView projectRecyclerView;
    LottieAnimationView projectAnimation;
    TextView projectText;
    List<TimeTableData> projTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_project, container, false);
        projectAnimation = view.findViewById(R.id.projectAnimation);
        projectRecyclerView = view.findViewById(R.id.projectRecyclerView);
        projectText = view.findViewById(R.id.projectText);

        TimeTableAdapter adapter = new TimeTableAdapter(projTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        projectRecyclerView.setLayoutManager(layoutManager);
        projectRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> projTimeTable = database.timeTableDao().loadTTDetails(1, -1, -1);
        projTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    projectAnimation.cancelAnimation();
                    projectAnimation.setVisibility(View.GONE);
                    projectText.setVisibility(View.GONE);
                }else{
                    projectAnimation.playAnimation();
                    projectAnimation.setVisibility(View.VISIBLE);
                    projectText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}