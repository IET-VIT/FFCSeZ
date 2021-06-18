package com.tfd.ffcsez.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TTDetails;
import com.tfd.ffcsez.database.TimeTableData;
import com.tfd.ffcsez.fragments.timetable.FridayFragment;
import com.tfd.ffcsez.fragments.timetable.MondayFragment;
import com.tfd.ffcsez.fragments.timetable.ProjectFragment;
import com.tfd.ffcsez.fragments.timetable.ThursdayFragment;
import com.tfd.ffcsez.fragments.timetable.TuesdayFragment;
import com.tfd.ffcsez.fragments.timetable.WednesdayFragment;
import com.tfd.ffcsez.models.Coord;

import java.util.List;


public class TTDetailsAdapter extends RecyclerView.Adapter<TTDetailsAdapter.RecyclerViewHolder> {
    private List<TTDetails> list;
    private final Context context;
    private FacultyDatabase database;
    private SharedPreferences preferences;

    public TTDetailsAdapter(List<TTDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());
        preferences = context.getSharedPreferences("com.tfd.ffcsez", Context.MODE_PRIVATE);
        View view = LayoutInflater.from(context).inflate(R.layout.timetables_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView timeTableName;
        private final ImageButton ttDeleteButton, ttEditButton;
        private final ImageView selectedImageView;
        private final CardView timeTableInfo;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);

            timeTableName = view.findViewById(R.id.timeTableName);
            ttDeleteButton = view.findViewById(R.id.ttDeleteButton);
            ttEditButton = view.findViewById(R.id.ttEditButton);
            selectedImageView = view.findViewById(R.id.selectedImageView);
            timeTableInfo = view.findViewById(R.id.timeTableInfo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        TTDetails details = list.get(position);

        Log.d("HelloTT", details.getTimeTableId() + details.getTimeTableName());

        if (details.getTimeTableName().equals("XXDefault TimetableXX")) {
            holder.timeTableName.setText("Default Timetable");
            holder.ttDeleteButton.setVisibility(View.GONE);
            holder.ttEditButton.setVisibility(View.GONE);
        }else{
            holder.timeTableName.setText(details.getTimeTableName());
            holder.ttDeleteButton.setVisibility(View.VISIBLE);
            holder.ttEditButton.setVisibility(View.VISIBLE);
        }

        if (details.getTimeTableId() == ConstantsActivity.getSelectedTimeTableId())
            holder.selectedImageView.setVisibility(View.VISIBLE);
        else
            holder.selectedImageView.setVisibility(View.GONE);

        holder.ttDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.timeTableDao().deleteAllTTSlots(details.getTimeTableId());
                        database.ttDetailsDao().deleteTimeTable(details);
                    }
                });
            }
        });

        holder.timeTableInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantsActivity.setSelectedTimeTableId(details.getTimeTableId());
                preferences.edit().putInt("lastTT", ConstantsActivity.getSelectedTimeTableId()).apply();
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

                ExecutorClass.getInstance().diskIO().execute(() -> {
                    List<Coord> coords = database.timeTableDao().getChosenSlots(ConstantsActivity.getSelectedTimeTableId());
                    for (Coord coord: coords){
                        if (coord.getRow() != -1)
                            MainActivity.chosenSlots[coord.getRow()][coord.getColumn()] = 1;
                    }
                });
                MainActivity.facultyAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<TTDetails> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
