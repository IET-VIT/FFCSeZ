package com.tfd.ffcsez.fragments.timetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MondayFragment extends Fragment {

    @BindView(R.id.mondayRecyclerView) RecyclerView mondayRecyclerView;
    @BindView(R.id.mondayAnimation) LottieAnimationView mondayAnimation;
    @BindView(R.id.mondayText) TextView mondayText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());

        List<TimeTableData> monTimeTable = new ArrayList<>();
        TimeTableAdapter adapter = new TimeTableAdapter(monTimeTable, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mondayRecyclerView.setLayoutManager(layoutManager);
        mondayRecyclerView.setAdapter(adapter);

        ConstantsActivity.getTimeTableId().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                LiveData<List<TimeTableData>> monTimeTableLD = database.timeTableDao().loadTTDetails(integer, 0, 5);
                if (getActivity() != null) {
                    monTimeTableLD.observe(getActivity(), timeTableData -> {
                        adapter.updateAdapter(timeTableData);
                        mondayRecyclerView.smoothScrollToPosition(0);

                        if (timeTableData.size() != 0) {
                            mondayAnimation.cancelAnimation();
                            mondayAnimation.setVisibility(View.GONE);
                            mondayText.setVisibility(View.GONE);
                        } else {
                            mondayAnimation.playAnimation();
                            mondayAnimation.setVisibility(View.VISIBLE);
                            mondayText.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        return view;
    }
}