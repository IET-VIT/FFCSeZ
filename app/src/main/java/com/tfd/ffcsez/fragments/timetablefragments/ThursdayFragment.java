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


public class ThursdayFragment extends Fragment {

    RecyclerView thursdayRecyclerView;
    LottieAnimationView thursdayAnimation;
    TextView thursdayText;
    List<TimeTableData> thuTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_thursday, container, false);
        thursdayAnimation = view.findViewById(R.id.thursdayAnimation);
        thursdayRecyclerView = view.findViewById(R.id.thursdayRecyclerView);
        thursdayText = view.findViewById(R.id.thursdayText);

        TimeTableAdapter adapter = new TimeTableAdapter(thuTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        thursdayRecyclerView.setLayoutManager(layoutManager);
        thursdayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> thuTimeTable = database.timeTableDao().loadTTDetails(1, 3, 8);
        thuTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    thursdayAnimation.cancelAnimation();
                    thursdayAnimation.setVisibility(View.GONE);
                    thursdayText.setVisibility(View.GONE);
                }else{
                    thursdayAnimation.playAnimation();
                    thursdayAnimation.setVisibility(View.VISIBLE);
                    thursdayText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}