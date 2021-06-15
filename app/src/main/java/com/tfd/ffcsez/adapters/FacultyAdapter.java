package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.RecyclerViewHolder> {

    private List<FacultyData> list;
    private Context context;
    private FacultyDatabase database;
    private int defaultTimeTable;
    private boolean exists;

    public FacultyAdapter() {

    }

    public FacultyAdapter(List<FacultyData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FacultyDatabase.getInstance(context.getApplicationContext());
        SharedPreferences preferences = context.getSharedPreferences("com.prasoonsoni.ffcs",
                Context.MODE_PRIVATE);
        defaultTimeTable = preferences.getInt("defaultTT", 1);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        if(list.get(position).getCourseType().equals("ELA") || list.get(position).getCourseType().equals("LO")){
//            holder.constraintLayout.setBackgroundColor(Color.parseColor("#333333"));
//        }
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
        }
        holder.clash.setText(clashString);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d("Hellox", Integer.toString(list.size()));
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseCode, courseTitle, courseType, empName, empSchool,
                slot, roomNumber, l, t, p, j, c, clash;
        private CardView cardView;
        private ConstraintLayout constraintLayout;

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
            constraintLayout = view.findViewById(R.id.constraintLayout);
        }
    }

    private String checkSlot(String slots){
        String[] slot = slots.split("[+]");
        String clash = "";
        Pattern pattern = Pattern.compile("^L");
        Matcher matcher;
        for (String slotNum: slot){
            Log.d("Hellox", slotNum);
            matcher = pattern.matcher(slotNum);
            if (matcher.find()){
                if (isClashing(Integer.parseInt(slotNum.substring(1)))){
                    clash += slotNum + " ";
                }
            }else{
                if (MainActivity.slotList.get(slotNum) != null) {
                    for (int i = 0; i < MainActivity.slotList.get(slotNum).length; i++) {
                        if (isClashing(MainActivity.slotList.get(slotNum)[i])) {
                            clash += slotNum + " ";
                            break;
                        }
                    }
                }
            }
        }
        Log.d("Hellox", clash);
        if (!clash.isEmpty()){
            return "Clash with " + clash;
        }
        return clash;
    }

    private boolean isClashing(int num){
        int r, c;
        Log.d("Hellonum", Integer.toString(num));

        if (num%6 == 0){
            r = num/6 - 1;
            c = 5;
        }else{
            r = num/6;
            c = num%6 - 1;
        }
        Log.d("Hellor", Integer.toString(r));
        Log.d("Helloc", Integer.toString(c));
        Log.d("Helloslot", Integer.toString(MainActivity.chosenSlots[r][c]));

        return MainActivity.chosenSlots[r][c] == 1;
    }

    public void updateAdapter(List<FacultyData> list){
        this.list = list;
        notifyDataSetChanged();
    }
}