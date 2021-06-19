package com.tfd.ffcsez.fragments.timetable;

import android.os.Bundle;
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
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TuesdayFragment extends Fragment {

    @BindView(R.id.tuesdayRecyclerView) RecyclerView tuesdayRecyclerView;
    @BindView(R.id.tuesdayAnimation) LottieAnimationView tuesdayAnimation;
    @BindView(R.id.tuesdayText) TextView tuesdayText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> tueTimeTable = new ArrayList<>();
        TimeTableAdapter adapter = new TimeTableAdapter(tueTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tuesdayRecyclerView.setLayoutManager(layoutManager);
        tuesdayRecyclerView.setAdapter(adapter);

        ConstantsActivity.getTimeTableId().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                LiveData<List<TimeTableData>> tueTimeTableLD = database.timeTableDao().loadTTDetails(integer, 1, 6);
                if (getActivity() != null) {
                    tueTimeTableLD.observe(getActivity(), timeTableData -> {
                        adapter.updateAdapter(timeTableData);

                        if (timeTableData.size() != 0) {
                            tuesdayAnimation.cancelAnimation();
                            tuesdayAnimation.setVisibility(View.GONE);
                            tuesdayText.setVisibility(View.GONE);
                        } else {
                            tuesdayAnimation.playAnimation();
                            tuesdayAnimation.setVisibility(View.VISIBLE);
                            tuesdayText.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        return view;
    }
}