package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tfd.ffcsez.R;
import com.tfd.ffcsez.models.CreditDetails;
import java.util.List;


public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.RecyclerViewHolder>{

    List<CreditDetails> list;
    Context context;

    public CreditsAdapter(List<CreditDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registered_courses, parent, false);
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private final TextView rCode, rType, rCredits;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);

            rCode = view.findViewById(R.id.registeredCode);
            rType = view.findViewById(R.id.registeredType);
            rCredits = view.findViewById(R.id.registeredCredits);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        CreditDetails details = list.get(position);
        holder.rCode.setText(details.getCourseCode());
        holder.rType.setText(details.getCourseType());
        holder.rCredits.setText(Integer.toString(details.getC()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<CreditDetails> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
