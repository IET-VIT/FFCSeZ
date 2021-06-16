package com.tfd.ffcsez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.tfd.ffcsez.adapters.CourseACAdapter;
import com.tfd.ffcsez.adapters.FacultyACAdapter;
import com.tfd.ffcsez.adapters.FacultyAdapter;
import com.tfd.ffcsez.adapters.TimetablePagerAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.models.Coord;
import com.tfd.ffcsez.models.CourseData;
import com.tfd.ffcsez.models.CourseDetails;
import com.tfd.ffcsez.models.FacultyDetails;
import com.roacult.backdrop.BackdropLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    BackdropLayout backdropLayout;
    Toolbar toolbar;
    View back_layout;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    TimetablePagerAdapter timetablePagerAdapter;
    AutoCompleteTextView courseCodeEditText, facultyNameEditText;
    Switch toggle;
    TextView cText, fText;
    TextInputLayout courseCodeLayout, facultyNameLayout;
    String courseTH = "", courseETH = "", courseELA = "", courseEPJ = "", courseSS = "",
            courseLO = "", timeFN = "", timeAN = "";
    private static final String LOG_TAG = "Hello";
    FacultyDatabase database;
    public static int[][] chosenSlots;
    List<FacultyData> facultyList = new ArrayList<>();
    public static FacultyAdapter adapter;

    @BindView(R.id.facultyRecyclerView) RecyclerView facultyRecyclerView;
    @BindView(R.id.button) Button button;
    @BindView(R.id.morningChip) Chip morningChip;
    @BindView(R.id.afternoonChip) Chip afternoonChip;
    @BindView(R.id.theoryChip) Chip theoryChip;
    @BindView(R.id.labChip) Chip labChip;
    @BindView(R.id.projectChip) Chip projectChip;
    @BindView(R.id.animation) LottieAnimationView animation;
    @BindView(R.id.animation2) LottieAnimationView notFound;
    @BindView(R.id.errorText) TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#61cde9"));
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        backdropLayout = findViewById(R.id.container);

        ButterKnife.bind(this);
        initialize();

        back_layout = backdropLayout.getChildAt(0);
        courseCodeEditText = back_layout.findViewById(R.id.courseCodeEditText);
        //RecyclerView facultyRecyclerView = back_layout.findViewById(R.id.facultyRecyclerView);
        adapter = new FacultyAdapter(facultyList, this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this);
        facultyRecyclerView.setLayoutManager(layoutManager);
        facultyRecyclerView.setAdapter(adapter);

        ExecutorClass.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<CourseDetails> test = database.facultyDao().loadCourses();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, Integer.toString(test.size()));
                        CourseACAdapter searchAdapter = new CourseACAdapter(MainActivity.this, test);
                        courseCodeEditText.setAdapter(searchAdapter);
                    }
                });
            }
        });

        // Back Layout Code //
        facultyNameEditText = back_layout.findViewById(R.id.facultyNameEditText);

        courseCodeLayout = back_layout.findViewById(R.id.courseCodeLayout);
        facultyNameLayout = back_layout.findViewById(R.id.facultyNameLayout);
        toggle = back_layout.findViewById(R.id.toggle);
        cText = back_layout.findViewById(R.id.cText);
        fText = back_layout.findViewById(R.id.fText);

        toggle.setOnClickListener(v -> {
            if(toggle.isChecked()){
                courseCodeLayout.setVisibility(View.INVISIBLE);
                facultyNameLayout.setVisibility(View.VISIBLE);
                cText.setTextColor(Color.parseColor("#fff5eb"));
                fText.setTextColor(Color.parseColor("#03256c"));
            } else {
                courseCodeLayout.setVisibility(View.VISIBLE);
                facultyNameLayout.setVisibility(View.INVISIBLE);
                fText.setTextColor(Color.parseColor("#fff5eb"));
                cText.setTextColor(Color.parseColor("#03256c"));
            }
        });



        courseCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facultyNameEditText.setText("");
                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<FacultyDetails> test = database.facultyDao().loadFaculties(s.toString().toUpperCase() + "%");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(LOG_TAG, Integer.toString(test.size()));
                                FacultyACAdapter facAdapter = new FacultyACAdapter(MainActivity.this, test);
                                facultyNameEditText.setAdapter(facAdapter);
                            }
                        });
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        morningChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morningChip.isChecked())
                    timeFN = "FN";
                else
                    timeFN = "";
                updateFilters();
            }
        });

        afternoonChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (afternoonChip.isChecked())
                    timeAN = "AN";
                else
                    timeAN = "";
                updateFilters();
            }
        });

        theoryChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theoryChip.isChecked()) {
                    courseTH = "TH";
                    courseETH = "ETH";
                    courseSS = "SS";
                }else {
                    courseTH = "";
                    courseETH = "";
                    courseSS = "";
                }
                updateFilters();
            }
        });

        labChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labChip.isChecked()) {
                    courseELA = "ELA";
                    courseLO = "LO";
                }else {
                    courseELA = "";
                    courseLO = "";
                }
                updateFilters();
            }
        });

        projectChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectChip.isChecked())
                    courseEPJ = "EPJ";
                else
                    courseEPJ = "";
                updateFilters();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilters();
            }
        });

        //Back Layout Code Ends //

        FragmentManager fm = getSupportFragmentManager();
        timetablePagerAdapter = new TimetablePagerAdapter(fm, getLifecycle());
        viewPager.setAdapter(timetablePagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fullScreen:
                        startActivity(new Intent(MainActivity.this, LandscapeActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initialize() {
        database = FacultyDatabase.getInstance(getApplicationContext());

        chosenSlots = new int[10][6];
        for (int i = 0; i < 10; i++){
            for(int j = 0; j < 6; j++){
                chosenSlots[i][j] = 0;
            }
        }
        ExecutorClass.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Coord> coords = database.timeTableDao().getChosenSlots(1);
                for (Coord coord: coords){
                    if (coord.getRow() != -1)
                        chosenSlots[coord.getRow()][coord.getColumn()] = 1;
                }
            }
        });


    }

    private void updateFilters(){
        ExecutorClass.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!(courseTH.isEmpty() && courseETH.isEmpty() && courseSS.isEmpty() && courseELA.isEmpty()
                        && courseLO.isEmpty() && courseEPJ.isEmpty()) && !(timeFN.isEmpty() && timeAN.isEmpty())) {
                    facultyList = database.facultyDao().loadAsPerFilterAND(courseCodeEditText.getText().toString().toUpperCase().trim() + "%",
                            facultyNameEditText.getText().toString().toUpperCase().trim() + "%", courseTH, courseETH,
                            courseELA, courseEPJ, courseSS, courseLO, timeFN, timeAN);
                }else if (courseTH.isEmpty() && courseETH.isEmpty() && courseSS.isEmpty() && courseELA.isEmpty()
                        && courseLO.isEmpty() && courseEPJ.isEmpty() && timeFN.isEmpty() && timeAN.isEmpty()){
                    facultyList = database.facultyDao().loadData(courseCodeEditText.getText().toString().toUpperCase().trim() + "%",
                            facultyNameEditText.getText().toString().toUpperCase().trim() + "%");
                }else {
                    facultyList = database.facultyDao().loadAsPerFilterOR(courseCodeEditText.getText().toString().toUpperCase().trim() + "%",
                            facultyNameEditText.getText().toString().toUpperCase().trim() + "%", courseTH, courseETH,
                            courseELA, courseEPJ, courseSS, courseLO, timeFN, timeAN);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Hellofilter", Integer.toString(facultyList.size()));
                        if(facultyList.size()==0){
                            animation.setVisibility(View.INVISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            errorText.setText("No Course Available");
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            animation.setVisibility(View.INVISIBLE);
                            notFound.setVisibility(View.INVISIBLE);
                            errorText.setVisibility(View.INVISIBLE);
                        }
                        adapter.updateAdapter(facultyList);
                    }
                });
            }
        });
    }
}