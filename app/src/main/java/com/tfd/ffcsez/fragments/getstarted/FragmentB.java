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


public class FragmentB extends Fragment {
    @BindView(R.id.skipButton1)TextView skipButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        ButterKnife.bind(this, view);

        skipButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GetStartedActivity)getActivity()).updateStatusBarColor("#974ea0");
    }
}