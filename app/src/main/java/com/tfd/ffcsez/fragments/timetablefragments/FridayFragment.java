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

public class FridayFragment extends Fragment {

    RecyclerView fridayRecyclerView;
    LottieAnimationView fridayAnimation;
    TextView fridayText;
    List<TimeTableData> friTimeTable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_friday, container, false);
        fridayAnimation = view.findViewById(R.id.fridayAnimation);
        fridayRecyclerView = view.findViewById(R.id.fridayRecyclerView);
        fridayText = view.findViewById(R.id.fridayText);

        TimeTableAdapter adapter = new TimeTableAdapter(friTimeTable, getContext());
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        fridayRecyclerView.setLayoutManager(layoutManager);
        fridayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> monTimeTable = database.timeTableDao().loadTTDetails(1, 4, 9);
        monTimeTable.observe(getActivity(), new Observer<List<TimeTableData>>() {
            @Override
            public void onChanged(List<TimeTableData> timeTableData) {
                adapter.updateAdapter(timeTableData);
                Log.d("Helloex", Integer.toString(timeTableData.size()));
                if (timeTableData.size() != 0){
                    fridayAnimation.cancelAnimation();
                    fridayAnimation.setVisibility(View.GONE);
                    fridayText.setVisibility(View.GONE);
                }else{
                    fridayAnimation.playAnimation();
                    fridayAnimation.setVisibility(View.VISIBLE);
                    fridayText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}