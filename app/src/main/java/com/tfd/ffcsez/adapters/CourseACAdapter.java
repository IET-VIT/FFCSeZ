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
import com.tfd.ffcsez.models.CourseDetails;
import com.tfd.ffcsez.models.FacultyDetails;

import java.util.ArrayList;
import java.util.List;


public class CourseACAdapter extends ArrayAdapter<CourseDetails> {
    private List<CourseDetails> courseDetailsList;

    public CourseACAdapter(@NonNull Context context, @NonNull List<CourseDetails> courseList) {
        super(context, 0, courseList);
        courseDetailsList = new ArrayList<>(courseList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return courseFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.autocomplete_layout, parent, false);
        }

        TextView text1 = convertView.findViewById(R.id.textView);
        TextView text2 = convertView.findViewById(R.id.textView2);

        CourseDetails course = getItem(position);

        if (course != null) {
            text1.setText(course.getCourseCode());
            text2.setText(course.getCourseTitle());
        }

        return convertView;
    }

    private Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<CourseDetails> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(courseDetailsList);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (CourseDetails item : courseDetailsList) {
                    if (item.getCourseCode().toUpperCase().contains(filterPattern) ||
                            item.getCourseTitle().toUpperCase().contains(filterPattern)) {
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
            return ((CourseDetails) resultValue).getCourseCode();
        }
    };

    public void updateAdapter(List<CourseDetails> list){
        this.courseDetailsList = list;
        notifyDataSetChanged();
    }
}