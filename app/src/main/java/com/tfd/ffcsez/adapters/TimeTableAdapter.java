package com.tfd.ffcsez.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
        if (list.get(position).isClash())
            holder.typeSelector.setBackgroundColor(Color.parseColor("#B00020"));
        else
            holder.typeSelector.setBackgroundResource(R.color.orange);
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
        }
    }

    public void updateAdapter(List<TimeTableData> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
