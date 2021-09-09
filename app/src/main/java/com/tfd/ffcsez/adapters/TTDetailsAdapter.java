package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tfd.ffcsez.ConstantsActivity;
import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TTDetails;
import com.tfd.ffcsez.models.Coord;
import com.tfd.ffcsez.models.CreditDetails;

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
        private final Button ttDeleteButton, ttEditButton, ttClearButton;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.ttClearButton.setTooltipText("Clear Timetable");
            holder.ttDeleteButton.setTooltipText("Delete Timetable");
            holder.ttEditButton.setTooltipText("Edit Timetable Name");
        }

        if (details.getTimeTableId() == 1) {
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
                TextInputLayout timeTableNameLayout = dialogView.findViewById(R.id.timeTableNameLayout);
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
                        timeTableNameLayout.setError(null);
                        if (timeTableNameEditText.getText().toString().trim().isEmpty()) {
                            timeTableNameLayout.setError("Timetable name cannot be empty");
                        } else if (timeTableNameEditText.getText().toString().length() > 16){
                            timeTableNameLayout.setError("Name cannot be greater than 16 characters");
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
                    preferences.edit().putInt("lastTT", ConstantsActivity.getTimeTableId().getValue()).apply();
                }

                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.timeTableDao().deleteAllTTSlots(details.getTimeTableId());
                        database.ttDetailsDao().deleteTimeTable(details);
                    }
                });

                Toast.makeText(context, "Deleted " + details.getTimeTableName(), Toast.LENGTH_SHORT).show();
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

                for (int i = 0; i < 10; i++){
                    for(int j = 0; j < 6; j++){
                        ConstantsActivity.getChosenSlots()[i][j] = 0;
                    }
                }

                if (details.getTimeTableId() == 1)
                    Toast.makeText(context, "Cleared the default timetable", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Cleared " + details.getTimeTableName(), Toast.LENGTH_SHORT).show();

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
