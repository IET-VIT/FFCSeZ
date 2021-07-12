package com.tfd.ffcsez.fragments.getstarted;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tfd.ffcsez.MainActivity;
import com.tfd.ffcsez.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentE extends Fragment {

    @BindView(R.id.nextButton2) Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e, container, false);
        ButterKnife.bind(this, view);

        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });
        return view;
    }
}