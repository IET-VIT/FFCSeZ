package com.tfd.ffcsez.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

        if (getActivity() != null) {
            LiveData<List<TTDetails>> ttDetailsListLD = database.ttDetailsDao().loadAllTimeTables();
            ttDetailsListLD.observe(getActivity(), adapter::updateAdapter);
        }

        createTTButton.setOnClickListener(v -> {
            MainActivity.doVibration();
            View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_new_timetable, null);
            EditText timeTableName = dialogView.findViewById(R.id.timeTableNameText);
            TextInputLayout timeTableNameLayout = dialogView.findViewById(R.id.timeTableNameLayout);
            Button doneButton = dialogView.findViewById(R.id.doneButton);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);

            AlertDialog newTTDialog = new AlertDialog.Builder(getActivity())
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();
            newTTDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            newTTDialog.show();

            newTTDialog.setOnCancelListener(dialog -> {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(timeTableName.getWindowToken(), 0);
            });

            cancelButton.setOnClickListener(v1 -> {
                MainActivity.doVibration();
                newTTDialog.cancel();
            });

            doneButton.setOnClickListener(v12 -> {
                MainActivity.doVibration();
                timeTableNameLayout.setError(null);
                if (timeTableName.getText().toString().trim().isEmpty()){
                    timeTableNameLayout.setError("Timetable name cannot be empty");
                } else if (timeTableName.getText().toString().length() > 15){
                    timeTableNameLayout.setError("Name cannot be greater than 15 characters");
                } else{
                    timeTableName.setEnabled(false);
                    ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            TTDetails details = new TTDetails(timeTableName.getText().toString().trim());
                            database.ttDetailsDao().insertTimeTable(details);

                            List<TTDetails> timeTable = database.ttDetailsDao().getTimeTable(details.getTimeTableName());

                            for (int i = 0; i < 10; i++){
                                for(int j = 0; j < 6; j++){
                                    ConstantsActivity.getChosenSlots()[i][j] = 0;
                                }
                            }

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConstantsActivity.getTimeTableId().setValue(timeTable.get(0).getTimeTableId());
                                        preferences.edit().putInt("lastTT", timeTable.get(0).getTimeTableId()).apply();

                                        newTTDialog.cancel();
                                        Snackbar.make(view, "Created new timetable",
                                                Snackbar.LENGTH_SHORT)
                                                .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                                                .setTextColor(getResources().getColor(R.color.snackbar_text))
                                                .show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        });

        return view;
    }
}