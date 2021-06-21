package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.models.RegisteredCourses;
import java.util.List;


public class RegisteredCourseAdapter extends RecyclerView.Adapter<RegisteredCourseAdapter.MyViewHolder>{

    List<RegisteredCourses> list;
    Context context;

    public RegisteredCourseAdapter(List<RegisteredCourses> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registered_courses, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RegisteredCourses model = list.get(position);
        holder.rCode.setText(model.getCourseCode());
        holder.rType.setText(model.getCourseType());
        holder.rCredits.setText(model.getC());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<RegisteredCourses> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rCode, rType, rCredits;
        public MyViewHolder(@NonNull View view) {
            super(view);
            rCode = view.findViewById(R.id.rCode);
            rType = view.findViewById(R.id.rType);
            rCredits = view.findViewById(R.id.rCredits);
        }
    }
}
