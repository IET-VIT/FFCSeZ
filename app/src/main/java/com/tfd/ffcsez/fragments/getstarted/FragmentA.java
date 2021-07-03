package com.tfd.ffcsez.fragments.getstarted;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tfd.ffcsez.GetStartedActivity;
import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentA extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GetStartedActivity)getActivity()).updateStatusBarColor("#ffffff");
    }
}