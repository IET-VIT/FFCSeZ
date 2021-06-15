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

public class WednesdayFragment extends Fragment {
    RecyclerView wednesdayRecyclerView;
    LottieAnimationView wednesdayAnimation;
    TextView wednesdayText;
    List<TimeTableData> wedTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);
        wednesdayAnimation = view.findViewById(R.id.wednesdayAnimation);
        wednesdayRecyclerView = view.findViewById(R.id.wednesdayRecyclerView);
        wednesdayText = view.findViewById(R.id.wednesdayText);

        TimeTableAdapter adapter = new TimeTableAdapter(wedTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        wednesdayRecyclerView.setLayoutManager(layoutManager);
        wednesdayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> wedTimeTable = database.timeTableDao().loadTTDetails(1, 2, 7);
        wedTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    wednesdayAnimation.cancelAnimation();
                    wednesdayAnimation.setVisibility(View.GONE);
                    wednesdayText.setVisibility(View.GONE);
                }else{
                    wednesdayAnimation.playAnimation();
                    wednesdayAnimation.setVisibility(View.VISIBLE);
                    wednesdayText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}