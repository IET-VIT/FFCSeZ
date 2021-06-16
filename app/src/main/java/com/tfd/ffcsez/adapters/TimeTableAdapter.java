package com.tfd.ffcsez.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;

import java.util.List;


public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {
    List<TimeTableData> list;
    Context context;
    FacultyDatabase database;

    public TimeTableAdapter(List<TimeTableData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());

        View view = LayoutInflater.from(context).inflate(R.layout.home_timetable_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimeTableData model = list.get(position);

        holder.startTime.setText(model.getStartTime());
        holder.endTime.setText(model.getEndTime());
        holder.slotNumber.setText(model.getCurrentSlot());
        holder.courseCode.setText(model.getCourseCode() + " - " + model.getCourseType());
        holder.roomNumber.setText(model.getRoomNumber());
        if (list.get(position).isClash()) {
            holder.typeSelector.setBackgroundColor(context.getColor(R.color.blood_red));
            holder.slotNumber.setTextColor(context.getColor(R.color.blood_red));
        }else {
            if (list.get(position).getCourseType().equals("LO") || list.get(position).getCourseType().equals("ELA")) {
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.teal_500));
                holder.slotNumber.setTextColor(context.getColor(R.color.teal_500));
            }else if (list.get(position).getCourseType().equals("EPJ")){
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.dark_blue));
                holder.slotNumber.setTextColor(context.getColor(R.color.dark_blue));
            }else{
                holder.typeSelector.setBackgroundColor(context.getColor(R.color.orange));
                holder.slotNumber.setTextColor(context.getColor(R.color.orange));
            }
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeTableData data = list.get(position);
                if (!data.getCourseType().equals("EPJ")) {
                    ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            List<TimeTableData> slots = database.timeTableDao().loadSlotDetails(data.getEmpName(), data.getSlot());
                            for (TimeTableData timeTableData : slots) {
                                database.timeTableDao().deleteSlot(timeTableData);
                                if (!timeTableData.isClash()) {
                                    MainActivity.chosenSlots[timeTableData.getRow()][timeTableData.getColumn()] = 0;
                                } else {
                                    List<TimeTableData> clashSlots = database.timeTableDao()
                                            .loadClashSlots(timeTableData.getRow(), timeTableData.getColumn());
                                    //clashSlots.removeAll(Collections.singleton(data));
                                    if (clashSlots.size() == 1) {
                                        clashSlots.get(0).setClash(false);
                                        database.timeTableDao().updateDetail(clashSlots.get(0));
                                    }
                                }
                            }
                        }
                    });
                }else{
                    ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.timeTableDao().deleteSlot(data);
                        }
                    });
                }
                MainActivity.adapter.notifyDataSetChanged();
                Snackbar.make(v, "Course removed successfully - " + data.getCourseCode() + " - " + data.getCourseType(),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.parseColor("#232323"))
                        .setTextColor(Color.parseColor("#fff5eb"))
                        .show();
                //Toast.makeText(context, "Course removed successfully - " + data.getCourseCode() + " - " + data.getCourseType(), Toast.LENGTH_LONG).show();
            }
        });
        holder.longPress.setOnLongClickListener(v -> {

            View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);

            TextView facultyName = view.findViewById(R.id.digFacultyName);
            TextView facultySchool = view.findViewById(R.id.digFacultySchool);
            TextView courseName = view.findViewById(R.id.digCourseName);
            TextView l = view.findViewById(R.id.l);
            TextView t = view.findViewById(R.id.t);
            TextView p = view.findViewById(R.id.p);
            TextView j = view.findViewById(R.id.j);
            TextView c = view.findViewById(R.id.c);
            TextView digSlot = view.findViewById(R.id.digSlot);
            TextView digCourseCode = view.findViewById(R.id.digCourseCode);
            TextView digCourseType = view.findViewById(R.id.digCourseType);
            TextView digRoomNo = view.findViewById(R.id.digRoomNo);

            facultyName.setText(model.getEmpName());
            facultySchool.setText(model.getEmpSchool());
            courseName.setText(model.getCourseTitle());
            l.setText(model.getL());
            t.setText(model.getT());
            p.setText(model.getP());
            j.setText(model.getJ());
            c.setText(model.getC());
            digSlot.setText(model.getSlot());
            digCourseCode.setText(model.getCourseCode());
            digCourseType.setText(model.getCourseType());
            digRoomNo.setText(model.getRoomNumber());

            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setView(view)
                    .setCancelable(true)
                    .create();
            alertDialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView startTime, endTime, slotNumber, courseCode, roomNumber;
        View typeSelector;
        ImageView deleteButton;
        CardView longPress;

        public MyViewHolder(@NonNull View view) {
            super(view);
            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);
            slotNumber = view.findViewById(R.id.slotNumberHome);
            courseCode = view.findViewById(R.id.courseCodeHome);
            roomNumber = view.findViewById(R.id.roomNoHome);
            typeSelector = view.findViewById(R.id.typeSelector);
            deleteButton = view.findViewById(R.id.deleteButton);
            longPress = view.findViewById(R.id.longPress);
        }
    }

    public void updateAdapter(List<TimeTableData> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
