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


public class TuesdayFragment extends Fragment {
    RecyclerView tuesdayRecyclerView;
    LottieAnimationView tuesdayAnimation;
    TextView tuesdayText;
    List<TimeTableData> tueTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        tuesdayAnimation = view.findViewById(R.id.tuesdayAnimation);
        tuesdayRecyclerView = view.findViewById(R.id.tuesdayRecyclerView);
        tuesdayText = view.findViewById(R.id.tuesdayText);

        TimeTableAdapter adapter = new TimeTableAdapter(tueTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        tuesdayRecyclerView.setLayoutManager(layoutManager);
        tuesdayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> monTimeTable = database.timeTableDao().loadTTDetails(1, 1, 6);
        monTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    tuesdayAnimation.cancelAnimation();
                    tuesdayAnimation.setVisibility(View.GONE);
                    tuesdayText.setVisibility(View.GONE);
                }else{
                    tuesdayAnimation.playAnimation();
                    tuesdayAnimation.setVisibility(View.VISIBLE);
                    tuesdayText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}