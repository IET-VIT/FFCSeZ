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
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FridayFragment extends Fragment {

    @BindView(R.id.fridayRecyclerView) RecyclerView fridayRecyclerView;
    @BindView(R.id.fridayAnimation) LottieAnimationView fridayAnimation;
    @BindView(R.id.fridayText) TextView fridayText;
    public static TimeTableAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friday, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> friTimeTable = new ArrayList<>();
        adapter = new TimeTableAdapter(friTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        fridayRecyclerView.setLayoutManager(layoutManager);
        fridayRecyclerView.setAdapter(adapter);

        LiveData<List<TimeTableData>> friTimeTableLD = database.timeTableDao().loadTTDetails(ConstantsActivity.getSelectedTimeTableId(), 4, 9);
        friTimeTableLD.observe(getActivity(), timeTableData -> {
            adapter.updateAdapter(timeTableData);

            if (timeTableData.size() != 0){
                fridayAnimation.cancelAnimation();
                fridayAnimation.setVisibility(View.GONE);
                fridayText.setVisibility(View.GONE);
            }else{
                fridayAnimation.playAnimation();
                fridayAnimation.setVisibility(View.VISIBLE);
                fridayText.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}