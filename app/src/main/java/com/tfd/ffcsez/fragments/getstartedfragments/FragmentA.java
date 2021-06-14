package com.tfd.ffcsez.fragments.getstartedfragments;

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

public class FragmentA extends Fragment {

    private TextView skipButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        skipButton = view.findViewById(R.id.skipButton1);
        skipButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });
        return view;
    }

    @Override
    public void onResume() {
        ((GetStartedActivity)getActivity()).updateStatusBarColor("#867ae9");
        super.onResume();
    }
}