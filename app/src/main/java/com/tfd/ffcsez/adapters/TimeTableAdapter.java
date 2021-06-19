package com.tfd.ffcsez.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.tfd.ffcsez.database.TimeTableData;

import java.util.List;


public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.RecyclerViewHolder> {
    private List<TimeTableData> list;
    private final Context context;
    private FacultyDatabase database;

    public TimeTableAdapter(List<TimeTableData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());

        View view = LayoutInflater.from(context).inflate(R.layout.home_timetable_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView startTime, endTime, slotNumber, courseCode, roomNumber;
        private final View typeSelector;
        private final ImageButton deleteButton;
        private final CardView longPress;

        public RecyclerViewHolder(@NonNull View view) {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
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

        holder.deleteButton.setOnClickListener(v -> {
            TimeTableData data = list.get(position);

            if (!data.getCourseType().equals("EPJ")) {
                ExecutorClass.getInstance().diskIO().execute(() -> {
                    List<TimeTableData> slots = database.timeTableDao()
                            .loadSlotDetails(data.getEmpName(), data.getSlot());

                    for (TimeTableData timeTableData : slots) {
                        database.timeTableDao().deleteSlot(timeTableData);

                        if (!timeTableData.isClash()) {
                            ConstantsActivity.getChosenSlots()[timeTableData.getRow()][timeTableData.getColumn()] = 0;
                        } else {
                            List<TimeTableData> clashSlots = database.timeTableDao()
                                    .loadClashSlots(timeTableData.getRow(), timeTableData.getColumn());

                            if (clashSlots.size() == 1) {
                                clashSlots.get(0).setClash(false);
                                database.timeTableDao().updateDetail(clashSlots.get(0));
                            }
                        }
                    }
                });
            }else{
                ExecutorClass.getInstance().diskIO().execute(() -> database.timeTableDao().deleteSlot(data));
            }

            MainActivity.facultyAdapter.notifyDataSetChanged();
            Snackbar.make(v, "Course removed successfully - " + data.getCourseCode() + " - " + data.getCourseType(),
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.parseColor("#232323"))
                    .setTextColor(Color.parseColor("#fff5eb"))
                    .show();
        });

        holder.longPress.setOnLongClickListener(v -> {

            View facultyView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);

            TextView facultyName = facultyView.findViewById(R.id.digFacultyName);
            TextView facultySchool = facultyView.findViewById(R.id.digFacultySchool);
            TextView courseName = facultyView.findViewById(R.id.digCourseName);
            TextView l = facultyView.findViewById(R.id.l);
            TextView t = facultyView.findViewById(R.id.t);
            TextView p = facultyView.findViewById(R.id.p);
            TextView j = facultyView.findViewById(R.id.j);
            TextView c = facultyView.findViewById(R.id.c);
            TextView digSlot = facultyView.findViewById(R.id.digSlot);
            TextView digCourseCode = facultyView.findViewById(R.id.digCourseCode);
            TextView digCourseType = facultyView.findViewById(R.id.digCourseType);
            TextView digRoomNo = facultyView.findViewById(R.id.digRoomNo);
            CardView cardView = facultyView.findViewById(R.id.customDialogCardView);

            if (list.get(position).isClash()) {
                cardView.setCardBackgroundColor(context.getColor(R.color.light_light_pink));

            }else {

                if (list.get(position).getCourseType().equals("LO") || list.get(position).getCourseType().equals("ELA")) {
                    cardView.setCardBackgroundColor(context.getColor(R.color.teal_100));

                }else if (list.get(position).getCourseType().equals("EPJ")){
                    cardView.setCardBackgroundColor(context.getColor(R.color.sky_blue));

                }else{
                    cardView.setCardBackgroundColor(context.getColor(R.color.light_orange));
                }
            }

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
                    .setView(facultyView)
                    .setCancelable(true)
                    .create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            return true;
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
