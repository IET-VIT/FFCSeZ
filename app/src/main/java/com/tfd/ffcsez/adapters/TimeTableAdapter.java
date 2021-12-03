package com.tfd.ffcsez.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tfd.ffcsez.database.TimeTableData;
import com.tfd.ffcsez.models.CodeFac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.RecyclerViewHolder> {
    private List<TimeTableData> list;
    private final Context context;
    private FacultyDatabase database;
    
    public TimeTableAdapter(List<TimeTableData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private boolean isClashing(int r, int c, boolean lab) {

        if (lab && (c - 1) >= 0) {
            ArrayList<String> list = ConstantsActivity.getChosenSlotsType().get((r * 6) + c);
            if (list != null && list.contains("T"))
                return true;
        }

        if (!lab && (c + 1) < 6) {
            ArrayList<String> list = ConstantsActivity.getChosenSlotsType().get((r * 6) + (c + 2));
            if (list != null && list.contains("L"))
                return true;
        }

        return false;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());
        View view = LayoutInflater.from(context).inflate(R.layout.home_timetable_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView startTime, endTime, slotNumber, courseCode, roomNumber, empName, slot,
                courseName, l, t, p, j, c;
        private final View typeSelector;
        private final Button deleteButton;
        private final CardView longPress;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);

            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);
            slotNumber = view.findViewById(R.id.slotNumberHome);
            courseCode = view.findViewById(R.id.courseCodeHome);
            roomNumber = view.findViewById(R.id.roomNoHome);
            empName = view.findViewById(R.id.empNameHome);
            typeSelector = view.findViewById(R.id.typeSelector);
            deleteButton = view.findViewById(R.id.deleteButton);
            longPress = view.findViewById(R.id.longPress);
            courseName = view.findViewById(R.id.courseTitleHome);
            slot = view.findViewById(R.id.slotHome);
            l = view.findViewById(R.id.l);
            t = view.findViewById(R.id.t);
            p = view.findViewById(R.id.p);
            j = view.findViewById(R.id.j);
            c = view.findViewById(R.id.c);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        TimeTableData model = list.get(position);
        Log.d("HelloPost", model.getCourseType());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.deleteButton.setTooltipText("Delete Course");
        }

        holder.startTime.setText(model.getStartTime());
        holder.endTime.setText(model.getEndTime());
        holder.slotNumber.setText(model.getCurrentSlot());
        holder.courseCode.setText(model.getCourseCode() + " - " + model.getCourseType());
        holder.empName.setText(model.getEmpName());
        holder.roomNumber.setText(model.getRoomNumber());
        holder.courseName.setText(model.getCourseTitle());
        holder.slot.setText(model.getSlot());
        holder.l.setText(model.getL());
        holder.t.setText(model.getT());
        holder.p.setText(model.getP());
        holder.j.setText(model.getJ());
        holder.c.setText(model.getC());

        //holder.deleteButton.setEnabled(!model.getCourseType().equals("EPJ"));

        if (list.get(position).isClash()) {
            holder.typeSelector.setBackgroundColor(context.getColor(R.color.clash_color));
            holder.slotNumber.setTextColor(context.getColor(R.color.clash_color));

        }else {

            if (list.get(position).getCourseType().equals("LO") || list.get(position).getCourseType().equals("ELA")) {
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.lab_color));
                holder.slotNumber.setTextColor(context.getColor(R.color.lab_color));

            }else if (list.get(position).getCourseType().equals("EPJ") || list.get(position).getCourseType().equals("PJT")){
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.project_color));
                holder.slotNumber.setTextColor(context.getColor(R.color.project_color));

            }else{
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.theory_color));
                holder.slotNumber.setTextColor(context.getColor(R.color.theory_color));
            }
        }

        holder.deleteButton.setOnClickListener(v -> {
            MainActivity.doVibration();

            TimeTableData data = list.get(position);

            if (!(data.getCourseType().equals("EPJ") || data.getCourseType().equals("PJT"))) {
                ExecutorClass.getInstance().diskIO().execute(() -> {
                    List<TimeTableData> slots = database.timeTableDao()
                            .loadSlotDetails(data.getEmpName(), data.getSlot());

                    for (TimeTableData timeTableData : slots) {
                        database.timeTableDao().deleteSlot(timeTableData);

                        if (ConstantsActivity.getChosenSlotsType().get((timeTableData.getRow()*6) + (timeTableData.getColumn() + 1)) != null) {
                            ArrayList<String> alist = ConstantsActivity.getChosenSlotsType().get((timeTableData.getRow()*6) + (timeTableData.getColumn() + 1));
                            if (timeTableData.getCourseType().equals("ETH") || timeTableData.getCourseType().equals("TH") || timeTableData.getCourseType().equals("SS"))
                                alist.remove("T");
                            else if (timeTableData.getCourseType().equals("ELA") || timeTableData.getCourseType().equals("LO"))
                                alist.remove("L");
                            ConstantsActivity.getChosenSlotsType().put((timeTableData.getRow()*6) + (timeTableData.getColumn() + 1), alist);
                        }

                        if (!timeTableData.isClash()) {
                            ConstantsActivity.getChosenSlots()[timeTableData.getRow()][timeTableData.getColumn()] = 0;
                        } else {
                            List<TimeTableData> clashSlots = database.timeTableDao()
                                    .loadClashSlots(timeTableData.getRow(), timeTableData.getColumn());

                            if (clashSlots.size() == 0 || clashSlots.size() == 1) {
                                if (ConstantsActivity.getExceptionSlots().contains((timeTableData.getRow() * 6) + (timeTableData.getColumn() + 1) + 1) && (timeTableData.getCourseType().equals("TH") || timeTableData.getCourseType().equals("ETH") || timeTableData.getCourseType().equals("SS")) && timeTableData.getColumn() + 1 < 6) {
                                    List<TimeTableData> clashSlots1 = database.timeTableDao()
                                            .loadClashSlots(timeTableData.getRow(), timeTableData.getColumn() + 1);
                                    Log.i("Helloclash1", list.toString());

                                    for (TimeTableData ttdata : clashSlots1) {
                                        Log.i("Helloclashtt", ttdata.getCurrentSlot());
                                        if ((ttdata.getCourseType().equals("ELA") || ttdata.getCourseType().equals("LO")) && ttdata.getColumn() - 1 >= 0) {
                                            if (!isClashing(ttdata.getRow(), ttdata.getColumn(), true) && clashSlots1.size() == 1) {
                                                ttdata.setClash(false);
                                                database.timeTableDao().updateDetail(ttdata);
                                            }
                                        }
                                    }
                                } else if (ConstantsActivity.getExceptionSlots().contains((timeTableData.getRow() * 6) + (timeTableData.getColumn() + 1)) && (timeTableData.getCourseType().equals("ELA") || timeTableData.getCourseType().equals("LO")) && timeTableData.getColumn() - 1 >= 0) {
                                    List<TimeTableData> clashSlots1 = database.timeTableDao()
                                            .loadClashSlots(timeTableData.getRow(), timeTableData.getColumn() - 1);

                                    for (TimeTableData ttdata : clashSlots1) {
                                        if ((ttdata.getCourseType().equals("TH") || ttdata.getCourseType().equals("ETH") || ttdata.getCourseType().equals("SS")) && ttdata.getColumn() + 1 < 6) {
                                            if (!isClashing(ttdata.getRow(), ttdata.getColumn(), false) && clashSlots1.size() == 1) {
                                                ttdata.setClash(false);
                                                database.timeTableDao().updateDetail(ttdata);
                                            }
                                        }
                                    }
                                } else {
                                    if (clashSlots.size() == 1) {
                                        clashSlots.get(0).setClash(false);
                                        database.timeTableDao().updateDetail(clashSlots.get(0));
                                    }
                                }
                            }
                        }
                    }

                    List<FacultyData> epjSlot = database.facultyDao().loadEPJData(data.getCourseCode(), data.getEmpName());
                    if (epjSlot.size() != 0) {
                        List<CodeFac> epjClashSlot = database.timeTableDao().loadEpjClash(data.getCourseCode(), data.getEmpName());
                        Log.d("HelloSlots", epjClashSlot.toString());
                        if (epjClashSlot.size() == 0)
                            database.timeTableDao().deleteEpj(data.getCourseCode(), data.getEmpName());
                    }

                });

                Snackbar.make(v, "Course removed successfully - " + data.getCourseCode() + " - " + data.getCourseType(),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(context.getResources().getColor(R.color.snackbar_bg))
                        .setTextColor(context.getResources().getColor(R.color.snackbar_text))
                        .show();
            }else if (data.getCourseType().equals("EPJ")){
                Toast.makeText(context, "Cannot remove a project component alone. Remove the corresponding theory/lab slot(s).",
                        Toast.LENGTH_LONG).show();
            } else {
                ExecutorClass.getInstance().diskIO().execute(() -> database.timeTableDao().deleteSlot(data));
                Snackbar.make(v, "Course removed successfully - " + data.getCourseCode() + " - " + data.getCourseType(),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(context.getResources().getColor(R.color.snackbar_bg))
                        .setTextColor(context.getResources().getColor(R.color.snackbar_text))
                        .show();
            }

            if (MainActivity.facultyAdapter != null)
                MainActivity.facultyAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<TimeTableData> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
