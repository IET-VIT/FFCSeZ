package com.tfd.ffcsez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.roacult.backdrop.BackdropLayout;
import com.tfd.ffcsez.adapters.FacultyAdapter;
import com.tfd.ffcsez.adapters.TimetablePagerAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.models.CourseData;
import com.tfd.ffcsez.models.CourseDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity {

    BackdropLayout backdropLayout;

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
    private static final String MDB_URI = "mongodb+srv://admin:Arjun1407@cluster0.1oqwt.mongodb.net/FacultyList";
    FacultyDatabase database;
    public static int[][] chosenSlots;
    public static HashMap<String, int[]> slotList = new HashMap<>();
    List<FacultyData> facultyList = new ArrayList<>();
    ArrayList<HashMap<String, String>> courseList = new ArrayList<>();
    FacultyAdapter adapter;
    Realm realm;
    User user;
    App app;
    //@BindView(R.id.facultyRecyclerView) RecyclerView facultyRecyclerView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.morningChip)
    Chip morningChip;
    @BindView(R.id.afternoonChip)
    Chip afternoonChip;
    @BindView(R.id.theoryChip)
    Chip theoryChip;
    @BindView(R.id.labChip)
    Chip labChip;
    @BindView(R.id.projectChip)
    Chip projectChip;
    @BindView(R.id.animation)
    LottieAnimationView animation;
    @BindView(R.id.animation2)
    LottieAnimationView notFound;
    @BindView(R.id.errorText)
    TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#61cde9"));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        backdropLayout = findViewById(R.id.container);

        ButterKnife.bind(this);

        database = FacultyDatabase.getInstance(getApplicationContext());
        back_layout = backdropLayout.getChildAt(0);
        courseCodeEditText = back_layout.findViewById(R.id.courseCodeEditText);

        RecyclerView facultyRecyclerView = back_layout.findViewById(R.id.facultyRecyclerView);
        adapter = new FacultyAdapter(facultyList, this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this);
        facultyRecyclerView.setLayoutManager(layoutManager);
        facultyRecyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.prasoonsoni.ffcs",
                Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstTime", true)) {
            Log.d(LOG_TAG, "firstTime");
            Realm.init(this);
            app = new App(new AppConfiguration.Builder("ffcsapp-mwjba").build());

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Retrieving data");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {
                    Log.d(LOG_TAG, "Successfully authenticated anonymously.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setMessage("Please restart the app to see the updated changes");
                        }
                    });

                } else {
                    Log.d(LOG_TAG, "Failed to authenticate anonymously. " + result.getError());
                    progress.dismiss();
                }
            });

            user = app.currentUser();
            if (user != null) {
                SyncConfiguration config = new SyncConfiguration.Builder(user, "Open")
                        .waitForInitialRemoteData()
                        .build();

                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.d(LOG_TAG, "Realm created");
                        MainActivity.this.realm = realm;

                        RealmResults<CourseData> data = realm.where(CourseData.class).findAllAsync();
                        data.addChangeListener(new RealmChangeListener<RealmResults<CourseData>>() {
                            @Override
                            public void onChange(RealmResults<CourseData> courseData) {
                                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        database.facultyDao().deleteAll();
                                    }
                                });
                                for (CourseData course : data) {
                                    FacultyData faculty = new FacultyData(course);
                                    ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            database.facultyDao().insertDetail(faculty);
                                        }
                                    });
                                }
                                sharedPreferences.edit().putBoolean("firstTime", false).apply();
                                int size = courseData.size();
                                Log.d(LOG_TAG, Integer.toString(size));
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
                                progress.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable exception) {
                        super.onError(exception);
                        progress.dismiss();
                        Log.d(LOG_TAG, "Failed to create Realm" + exception.getMessage());
                    }
                });
            }
        }

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
            if (toggle.isChecked()) {
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
                } else {
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
                } else {
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
//                ExecutorClass.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        facultyList = database.facultyDao().getData(courseCodeEditText.getText().toString().toUpperCase().trim() + "%",
//                                courseTitleEditText.getText().toString().toUpperCase().trim() + "%");
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d(LOG_TAG, facultyList.toString());
//                                adapter.updateAdapter(facultyList);
//                            }
//                        });
//                    }
//                });
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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}