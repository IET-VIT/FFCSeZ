package com.tfd.ffcsez.fragments.getstartedfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tfd.ffcsez.GetStartedActivity;
import com.tfd.ffcsez.R;

public class FragmentC extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_c, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GetStartedActivity)getActivity()).updateStatusBarColor("#11cbd7");
    }
}