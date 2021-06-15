package com.tfd.ffcsez.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tfd.ffcsez.R;
import com.tfd.ffcsez.models.FacultyDetails;

import java.util.ArrayList;
import java.util.List;


public class FacultyACAdapter extends ArrayAdapter<FacultyDetails> {
    private List<FacultyDetails> facultyDetailsList;
    private TextView text1, text2;


    public FacultyACAdapter(@NonNull Context context, @NonNull List<FacultyDetails> facultyList) {
        super(context, 0, facultyList);
        facultyDetailsList = new ArrayList<>(facultyList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return facultyFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocompletefac_layout, parent, false
            //);
        }

        //text1 = convertView.findViewById(R.id.textView);
        //text2 = convertView.findViewById(R.id.textView2);

        FacultyDetails faculty = getItem(position);

        if (faculty != null) {
            text1.setText(faculty.getEmpName());
            text2.setText(faculty.getEmpSchool());
        }

        return convertView;
    }

    private Filter facultyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<FacultyDetails> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(facultyDetailsList);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (FacultyDetails item : facultyDetailsList) {
                    if (item.getEmpName().toUpperCase().contains(filterPattern) ||
                            item.getEmpSchool().toUpperCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((FacultyDetails) resultValue).getEmpName();
        }
    };
}