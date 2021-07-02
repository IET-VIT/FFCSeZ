package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView timeTableName;
        private final ImageButton ttDeleteButton, ttEditButton, ttClearButton;
        private final ImageView selectedImageView;
        private final CardView timeTableInfo;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);

            timeTableName = view.findViewById(R.id.timeTableName);
            ttDeleteButton = view.findViewById(R.id.ttDeleteButton);
            ttEditButton = view.findViewById(R.id.ttEditButton);
            ttClearButton = view.findViewById(R.id.ttClearButton);
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

        if (details.getTimeTableId() == ConstantsActivity.getTimeTableId().getValue()) {
            holder.selectedImageView.setVisibility(View.VISIBLE);
            holder.ttClearButton.setVisibility(View.VISIBLE);
        }else {
            holder.selectedImageView.setVisibility(View.GONE);
            holder.ttClearButton.setVisibility(View.GONE);
        }

        holder.ttEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.doVibration();
                View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_new_timetable, null);
                EditText timeTableNameEditText = dialogView.findViewById(R.id.timeTableNameText);
                Button doneButton = dialogView.findViewById(R.id.doneButton);
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);

                timeTableNameEditText.setText(details.getTimeTableName());

                AlertDialog newTTDialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setCancelable(true)
                        .create();
                newTTDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                newTTDialog.show();

                newTTDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(timeTableNameEditText.getWindowToken(), 0);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.doVibration();
                        newTTDialog.cancel();
                    }
                });

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.doVibration();
                        timeTableNameEditText.setError(null);
                        if (timeTableNameEditText.getText().toString().trim().isEmpty()) {
                            timeTableNameEditText.setError("Timetable name cannot be empty");
                        } else {
                            timeTableNameEditText.setEnabled(false);
                            newTTDialog.cancel();
                            ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    details.setTimeTableName(timeTableNameEditText.getText().toString().trim());
                                    database.ttDetailsDao().updateTimeTable(details);
                                }
                            });
                        }
                    }
                });
            }
        });

        holder.ttDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.doVibration();
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

                Snackbar.make(v, "Deleted " + details.getTimeTableName(),
                        Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(context.getResources().getColor(R.color.snackbar_bg))
                        .setTextColor(context.getResources().getColor(R.color.snackbar_text))
                        .show();

            }
        });

        holder.ttClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.doVibration();
                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.timeTableDao().deleteAllTTSlots(details.getTimeTableId());
                    }
                });

                Snackbar.make(v, "Cleared " + details.getTimeTableName(),
                        Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(context.getResources().getColor(R.color.snackbar_bg))
                        .setTextColor(context.getResources().getColor(R.color.snackbar_text))
                        .show();

                for (int i = 0; i < 10; i++){
                    for(int j = 0; j < 6; j++){
                        ConstantsActivity.getChosenSlots()[i][j] = 0;
                    }
                }

                if (MainActivity.facultyAdapter != null)
                    MainActivity.facultyAdapter.notifyDataSetChanged();
            }
        });

        holder.timeTableInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.doVibration();
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

                if (MainActivity.facultyAdapter != null)
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
