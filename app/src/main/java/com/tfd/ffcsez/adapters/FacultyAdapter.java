package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tfd.ffcsez.database.TimeTableData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.RecyclerViewHolder> {

    private List<FacultyData> list;
    private final Context context;
    private FacultyDatabase database;
    private boolean exists;

    public FacultyAdapter(List<FacultyData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseCode, courseTitle, courseType, empName, empSchool,
                slot, roomNumber, l, t, p, j, c, clash;
        private final CardView cardView;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);

            courseCode = view.findViewById(R.id.courseCode);
            courseTitle = view.findViewById(R.id.courseTitle);
            courseType = view.findViewById(R.id.courseType);
            l = view.findViewById(R.id.lhrs);
            t = view.findViewById(R.id.thrs);
            p = view.findViewById(R.id.phrs);
            j = view.findViewById(R.id.jhrs);
            c = view.findViewById(R.id.credits);
            empName = view.findViewById(R.id.empName);
            empSchool = view.findViewById(R.id.empSchool);
            slot = view.findViewById(R.id.slot);
            roomNumber = view.findViewById(R.id.roomNumber);
            clash = view.findViewById(R.id.clash);
            cardView = view.findViewById(R.id.cardView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.courseCode.setText(list.get(position).getCourseCode());
        holder.courseTitle.setText(list.get(position).getCourseTitle());
        holder.courseType.setText(list.get(position).getCourseType());
        holder.empName.setText(list.get(position).getEmpName());
        holder.empSchool.setText(list.get(position).getEmpSchool());
        holder.roomNumber.setText(list.get(position).getRoomNumber());
        holder.slot.setText(list.get(position).getSlot());
        holder.l.setText(list.get(position).getL());
        holder.t.setText(list.get(position).getT());
        holder.p.setText(list.get(position).getP());
        holder.j.setText(list.get(position).getJ());
        holder.c.setText(list.get(position).getC());

        String clashString = checkSlot(list.get(position).getSlot());
        if(clashString.isEmpty()){
            holder.clash.setVisibility(View.GONE);

            if (list.get(position).getCourseType().equals("LO") || list.get(position).getCourseType().equals("ELA")) {
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.teal_100));

            }else if (list.get(position).getCourseType().equals("EPJ")){
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.sky_blue));

            }else{
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.light_orange));
            }

        }else{
            holder.clash.setVisibility(View.VISIBLE);
            holder.clash.setText(clashString);
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.light_light_pink));
        }

        holder.cardView.setOnClickListener(v -> {
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
            }
            setTTSlot(list.get(position),v);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<FacultyData> list){
        this.list = list;
        notifyDataSetChanged();
    }

    private String checkSlot(String slots){
        String[] slot = slots.split("[+]");
        String clash = "";
        Pattern pattern = Pattern.compile("^L");
        Matcher matcher;

        for (String slotNum: slot){
            matcher = pattern.matcher(slotNum);

            if (matcher.find()){

                if (isClashing(Integer.parseInt(slotNum.substring(1)))){
                    clash += slotNum + " ";
                }

            }else{

                if (ConstantsActivity.getSlotList().get(slotNum) != null) {
                    for (int i = 0; i < ConstantsActivity.getSlotList().get(slotNum).length; i++) {

                        if (isClashing(ConstantsActivity.getSlotList().get(slotNum)[i])) {
                            clash += slotNum + " ";
                            break;
                        }
                    }
                }
            }
        }

        if (!clash.isEmpty()){
            return "Clash with " + clash;
        }
        return clash;
    }

    private boolean isClashing(int num){
        int r, c;

        if (num%6 == 0){
            r = num/6 - 1;
            c = 5;

        }else{
            r = num/6;
            c = num%6 - 1;
        }

        return ConstantsActivity.getChosenSlots()[r][c] == 1;
    }

    public void setTTSlot(FacultyData facultyData, View v){

        exists = false;
        if (!facultyData.getCourseType().equals("EPJ")) {
            String[] slot = facultyData.getSlot().split("[+]");
            Pattern pattern = Pattern.compile("^L");
            Matcher matcher;

            for (String slotNum : slot) {
                matcher = pattern.matcher(slotNum);

                if (matcher.find()) {
                    int[] coord = getCoord(Integer.parseInt(slotNum.substring(1)));
                    int num;
                    if(coord[1] == 5){
                        num = ((coord[0] + 1)*6);
                    }else{
                        num = ((coord[0])*6) + (coord[1] + 1);
                    }
                    TimeTableData data;

                    if (ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] == 1) {

                        data = new TimeTableData(facultyData, ConstantsActivity.getTimeTableId().getValue(), coord[0],
                                coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], true);

                        ExecutorClass.getInstance().diskIO().execute(() -> {
                            List<TimeTableData> clashSlots = database.timeTableDao()
                                    .loadClashSlots(coord[0], coord[1]);

                            for (TimeTableData timeTableData : clashSlots) {
                                if (timeTableData.getEmpName().equals(data.getEmpName()) && timeTableData.getSlot().equals(data.getSlot())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                for (TimeTableData timeTableData : clashSlots) {
                                    timeTableData.setClash(true);
                                    database.timeTableDao().updateDetail(timeTableData);
                                }

                                database.timeTableDao().insertSlot(data);
                                ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                            }
                        });

                    }else {
                        data = new TimeTableData(facultyData, ConstantsActivity.getTimeTableId().getValue(), coord[0],
                                coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], false);

                        ExecutorClass.getInstance().diskIO().execute(() -> {
                            database.timeTableDao().insertSlot(data);
                            ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                        });
                    }

                }else {
                    if (ConstantsActivity.getSlotList().get(slotNum) != null) {
                        for (int i = 0; i < ConstantsActivity.getSlotList().get(slotNum).length; i++) {
                            int[] coord = getCoord(ConstantsActivity.getSlotList().get(slotNum)[i]);
                            int num;
                            if(coord[1] == 5){
                                num = ((coord[0] + 1)*6);
                            }else{
                                num = ((coord[0])*6) + (coord[1] + 1);
                            }
                            TimeTableData data;

                            if (ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] == 1) {

                                data = new TimeTableData(facultyData, ConstantsActivity.getTimeTableId().getValue(), coord[0],
                                        coord[1], slotNum, ConstantsActivity.getTheoryTiming().get(num)[0], ConstantsActivity.getTheoryTiming().get(num)[1], true);

                                ExecutorClass.getInstance().diskIO().execute(() -> {
                                    List<TimeTableData> clashSlots = database.timeTableDao()
                                            .loadClashSlots(coord[0], coord[1]);

                                    for (TimeTableData timeTableData : clashSlots) {
                                        if (timeTableData.getEmpName().equals(data.getEmpName()) && timeTableData.getSlot().equals(data.getSlot())) {
                                            exists = true;
                                            break;
                                        }
                                    }

                                    if (!exists) {
                                        for (TimeTableData timeTableData : clashSlots) {
                                            timeTableData.setClash(true);
                                            database.timeTableDao().updateDetail(timeTableData);
                                        }

                                        database.timeTableDao().insertSlot(data);
                                        ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                                    }
                                });

                            } else {
                                data = new TimeTableData(facultyData, ConstantsActivity.getTimeTableId().getValue(), coord[0],
                                        coord[1], slotNum, ConstantsActivity.getTheoryTiming().get(num)[0], ConstantsActivity.getTheoryTiming().get(num)[1], false);

                                ExecutorClass.getInstance().diskIO().execute(() -> {
                                    database.timeTableDao().insertSlot(data);
                                    ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                                });
                            }
                        }
                    }
                }
            }
        }else{
            TimeTableData data = new TimeTableData(facultyData, ConstantsActivity.getTimeTableId().getValue(), -1,
                    -1, facultyData.getSlot(), "-:-", "-:-", false);

            ExecutorClass.getInstance().diskIO().execute(() -> {
                List<TimeTableData> clashSlots = database.timeTableDao()
                        .loadClashSlots(-1, -1);

                for (TimeTableData timeTableData : clashSlots) {
                    if (timeTableData.getEmpName().equals(data.getEmpName()) && timeTableData.getCourseCode().equals(data.getCourseCode())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    database.timeTableDao().insertSlot(data);
                }
            });
        }

        notifyDataSetChanged();
        Snackbar.make(v, "Course added successfully - " + facultyData.getCourseCode() + " - " + facultyData.getCourseType(),
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getResources().getColor(R.color.snackbar_bg))
                .setTextColor(context.getResources().getColor(R.color.snackbar_text))
                .show();
    }

    private int[] getCoord(int num){
        int[] coord = new int[2];
        int r, c;

        if (num%6 == 0){
            r = num/6 - 1;
            c = 5;
        }else{
            r = num/6;
            c = num%6 - 1;
        }

        coord[0] = r;
        coord[1] = c;
        return coord;
    }
}