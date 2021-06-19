package com.tfd.ffcsez.adapters;

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
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TTDetails;
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

        if (details.getTimeTableId() == ConstantsActivity.getTimeTableId().getValue())
            holder.selectedImageView.setVisibility(View.VISIBLE);
        else
            holder.selectedImageView.setVisibility(View.GONE);

        holder.ttDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getTimeTableId() == ConstantsActivity.getTimeTableId().getValue()){
                    ConstantsActivity.getTimeTableId().setValue(1);
                }

                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.timeTableDao().deleteAllTTSlots(details.getTimeTableId());
                        database.ttDetailsDao().deleteTimeTable(details);
                    }
                });

                Snackbar.make(v, "Timetable deleted",
                        Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#232323"))
                        .setTextColor(Color.parseColor("#fff5eb"))
                        .show();

            }
        });

        holder.timeTableInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantsActivity.getTimeTableId().setValue(details.getTimeTableId());
                preferences.edit().putInt("lastTT", ConstantsActivity.getTimeTableId().getValue()).apply();

                for (int i = 0; i < 10; i++){
                    for(int j = 0; j < 6; j++){
                        ConstantsActivity.getChosenSlots()[i][j] = 0;
                    }
                }

                ExecutorClass.getInstance().diskIO().execute(() -> {
                    List<Coord> coords = database.timeTableDao().getChosenSlots(ConstantsActivity.getTimeTableId().getValue());
                    for (Coord coord: coords){
                        if (coord.getRow() != -1)
                            ConstantsActivity.getChosenSlots()[coord.getRow()][coord.getColumn()] = 1;
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