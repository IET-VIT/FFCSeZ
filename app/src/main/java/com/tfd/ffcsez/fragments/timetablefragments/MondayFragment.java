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


public class MondayFragment extends Fragment {
    RecyclerView mondayRecyclerView;
    LottieAnimationView mondayAnimation;
    TextView mondayText;
    List<TimeTableData> monTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        mondayAnimation = view.findViewById(R.id.mondayAnimation);
        mondayRecyclerView = view.findViewById(R.id.mondayRecyclerView);
        mondayText = view.findViewById(R.id.mondayText);

        TimeTableAdapter adapter = new TimeTableAdapter(monTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        mondayRecyclerView.setLayoutManager(layoutManager);
        mondayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> monTimeTable = database.timeTableDao().loadTTDetails(1, 0, 5);
        monTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    mondayAnimation.cancelAnimation();
                    mondayAnimation.setVisibility(View.GONE);
                    mondayText.setVisibility(View.GONE);
                }else{
                    mondayAnimation.playAnimation();
                    mondayAnimation.setVisibility(View.VISIBLE);
                    mondayText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}