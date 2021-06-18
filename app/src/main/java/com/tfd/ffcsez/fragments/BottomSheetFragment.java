package com.tfd.ffcsez.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.adapters.TTDetailsAdapter;
import com.tfd.ffcsez.adapters.TimeTableAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TTDetails;
import com.tfd.ffcsez.fragments.timetable.FridayFragment;
import com.tfd.ffcsez.fragments.timetable.MondayFragment;
import com.tfd.ffcsez.fragments.timetable.ProjectFragment;
import com.tfd.ffcsez.fragments.timetable.ThursdayFragment;
import com.tfd.ffcsez.fragments.timetable.TuesdayFragment;
import com.tfd.ffcsez.fragments.timetable.WednesdayFragment;
import com.tfd.ffcsez.models.Coord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.timeTablesRecyclerView) RecyclerView ttRecyclerView;
    @BindView(R.id.createTTButton) Button createTTButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        FacultyDatabase database = FacultyDatabase.getInstance(getActivity().getApplicationContext());
        SharedPreferences preferences = getActivity().getSharedPreferences("com.tfd.ffcsez", Context.MODE_PRIVATE);

        List<TTDetails> ttDetailsList = new ArrayList<>();
        TTDetailsAdapter adapter = new TTDetailsAdapter(ttDetailsList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ttRecyclerView.setLayoutManager(layoutManager);
        ttRecyclerView.setAdapter(adapter);

        LiveData<List<TTDetails>> ttDetailsListLD = database.ttDetailsDao().loadAllTimeTables();
        ttDetailsListLD.observe(getActivity(), adapter::updateAdapter);

        createTTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        TTDetails details = new TTDetails("Timetable " + r.nextInt(50));
                        database.ttDetailsDao().insertTimeTable(details);
                        List<TTDetails> timeTable = database.ttDetailsDao().getTimeTable(details.getTimeTableName());
                        preferences.edit().putInt("lastTT", timeTable.get(0).getTimeTableId()).apply();
                        ConstantsActivity.setSelectedTimeTableId(preferences.getInt("lastTT", 1));

                        if (MondayFragment.adapter != null)
                            MondayFragment.adapter.notifyDataSetChanged();
                        if (TuesdayFragment.adapter != null)
                            TuesdayFragment.adapter.notifyDataSetChanged();
                        if (WednesdayFragment.adapter != null)
                            WednesdayFragment.adapter.notifyDataSetChanged();
                        if (ThursdayFragment.adapter != null)
                            ThursdayFragment.adapter.notifyDataSetChanged();
                        if (FridayFragment.adapter != null)
                            FridayFragment.adapter.notifyDataSetChanged();
                        if (ProjectFragment.adapter != null)
                            ProjectFragment.adapter.notifyDataSetChanged();

                        for (int i = 0; i < 10; i++){
                            for(int j = 0; j < 6; j++){
                                MainActivity.chosenSlots[i][j] = 0;
                            }
                        }

                        MainActivity.facultyAdapter.notifyDataSetChanged();

                    }
                });
            }
        });

        return view;
    }
}