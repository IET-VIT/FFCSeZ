  package com.tfd.ffcsez;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.roacult.backdrop.BackdropLayout;
import com.tfd.ffcsez.adapters.CourseACAdapter;
import com.tfd.ffcsez.adapters.CreditsAdapter;
import com.tfd.ffcsez.adapters.FacultyACAdapter;
import com.tfd.ffcsez.adapters.FacultyAdapter;
import com.tfd.ffcsez.adapters.TimetablePagerAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TimeTableData;
import com.tfd.ffcsez.fragments.BottomSheetFragment;
import com.tfd.ffcsez.models.Coord;
import com.tfd.ffcsez.models.CourseData;
import com.tfd.ffcsez.models.CourseDetails;
import com.tfd.ffcsez.models.CreditDetails;
import com.tfd.ffcsez.models.FacultyDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


  public class MainActivity extends AppCompatActivity {
    private boolean backdropIsOpen;
    // Views
    @BindView(R.id.container) BackdropLayout backdropLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.facultyRecyclerView) RecyclerView facultyRecyclerView;
    @BindView(R.id.searchButton) Button searchButton;
    @BindView(R.id.toggle) SwitchMaterial toggle;
    @BindView(R.id.viewPager) ViewPager2 viewPager;
    @BindView(R.id.animation) LottieAnimationView animation;
    @BindView(R.id.animation2) LottieAnimationView notFound;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.registeredCoursesRecyclerView) RecyclerView rRecyclerView;

    // TextViews
    @BindView(R.id.courseCodeEditText) AutoCompleteTextView courseCodeEditText;
    @BindView(R.id.facultyNameEditText) AutoCompleteTextView facultyNameEditText;
    @BindView(R.id.courseCodeLayout) TextInputLayout courseCodeLayout;
    @BindView(R.id.facultyNameLayout) TextInputLayout facultyNameLayout;
    @BindView(R.id.cText) TextView cText;
    @BindView(R.id.fText) TextView fText;
    @BindView(R.id.errorText) TextView errorText;
    @BindView(R.id.creditsNumber) TextView creditsNumber;
    @BindView(R.id.searchCountText) TextView searchCount;

    // Chips
    @BindView(R.id.morningChip) Chip morningChip;
    @BindView(R.id.afternoonChip) Chip afternoonChip;
    @BindView(R.id.theoryChip) Chip theoryChip;
    @BindView(R.id.labChip) Chip labChip;
    @BindView(R.id.projectChip) Chip projectChip;

    // Variables
    private String courseTH = "", courseETH = "", courseELA = "", courseEPJ = "", courseSS = "";
    private String courseLO = "", timeFN = "", timeAN = "";
    private boolean exists;
    private AlertDialog customDialog;
    private SharedPreferences preferences;

    // Database
    private FacultyDatabase database;
    private List<FacultyData> facultyList = new ArrayList<>();
    private List<CourseDetails> allCourses = new ArrayList<>();
    private List<FacultyDetails> allFaculties = new ArrayList<>();
    public static FacultyAdapter facultyAdapter;

    // Vibration
    public static Vibrator vibrator;
    public static void doVibration(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.EFFECT_TICK));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initialize();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("refreshNotif", false))
                refreshRealm();
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        backdropLayout.setOnBackdropChangeStateListener(new Function1<BackdropLayout.State, Unit>() {
            @Override
            public Unit invoke(BackdropLayout.State state) {
                switch(state){
                    case OPEN:
                        backdropIsOpen = true;
                        break;
                    case CLOSE:
                        backdropIsOpen = false;
                        break;
                }
                return null;
            }
        });
        // Back Layout
        View back_layout = backdropLayout.getChildAt(0);

        facultyAdapter = new FacultyAdapter(facultyList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        facultyRecyclerView.setLayoutManager(layoutManager);
        facultyRecyclerView.setAdapter(facultyAdapter);
        updateFilters();

        toggle.setOnClickListener(v -> {
            doVibration();
            if(toggle.isChecked()){
                courseCodeLayout.setVisibility(View.INVISIBLE);
                facultyNameLayout.setVisibility(View.VISIBLE);
                cText.setTextColor(Color.parseColor("#ffffff"));
                fText.setTextColor(getColor(R.color.selected_option));

            } else {
                courseCodeLayout.setVisibility(View.VISIBLE);
                facultyNameLayout.setVisibility(View.INVISIBLE);
                fText.setTextColor(Color.parseColor("#ffffff"));
                cText.setTextColor(getColor(R.color.selected_option));
            }
        });

        loadAutoComplete();

        // Chips
        morningChip.setOnClickListener(v -> {
            doVibration();
            if (morningChip.isChecked())
                timeFN = "FN";
            else
                timeFN = "";
            updateFilters();
        });

        afternoonChip.setOnClickListener(v -> {
            doVibration();
            if (afternoonChip.isChecked())
                timeAN = "AN";
            else
                timeAN = "";
            updateFilters();
        });

        theoryChip.setOnClickListener(v -> {
            doVibration();
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
        });

        labChip.setOnClickListener(v -> {
            doVibration();
            if (labChip.isChecked()) {
                courseELA = "ELA";
                courseLO = "LO";
            }else {
                courseELA = "";
                courseLO = "";
            }
            updateFilters();
        });

        projectChip.setOnClickListener(v -> {
            doVibration();
            if (projectChip.isChecked())
                courseEPJ = "EPJ";
            else
                courseEPJ = "";
            updateFilters();
        });

        searchButton.setOnClickListener(v -> {
            doVibration();
            updateFilters();
        });

        // Custom Dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_course_dialog, null);
        Button customButton = dialogView.findViewById(R.id.customButton);
        EditText customCourseCode = dialogView.findViewById(R.id.customCourseCode);
        EditText customCourseType = dialogView.findViewById(R.id.customCourseType);
        EditText customEmpName = dialogView.findViewById(R.id.customEmpName);
        EditText customSlot = dialogView.findViewById(R.id.customSlot);
        EditText customRoomNum = dialogView.findViewById(R.id.customRoomNum);
        EditText customCredits = dialogView.findViewById(R.id.customCredits);

        customDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        customButton.setOnClickListener(v -> {
            doVibration();
            int flag = 0;
            customSlot.setError(null);

            FacultyData data = new FacultyData();
            data.setCourseCode(customCourseCode.getText().toString().toUpperCase().trim());
            data.setCourseType(customCourseType.getText().toString().toUpperCase().trim());
            data.setEmpName(customEmpName.getText().toString().toUpperCase().trim());
            data.setC(customCredits.getText().toString());
            data.setRoomNumber(customRoomNum.getText().toString().toUpperCase().trim());
            data.setSlot(customSlot.getText().toString().toUpperCase().trim());

            String[] customSlots = data.getSlot().split("[+]");
            for (String slots: customSlots){
                slots = slots.trim();
                Pattern pattern = Pattern.compile("^L");
                Matcher matcher = pattern.matcher(slots);

                if (matcher.find()){

                    try {
                        if (!(1 <= Integer.parseInt(slots.substring(1)) && Integer.parseInt(slots.substring(1)) <= 60)) {
                            customSlot.setError("Invalid slot(s)");
                            flag = 1;
                            break;
                        }
                    }catch (Exception e){
                        customSlot.setError("Invalid slot(s)");
                        flag = 1;
                        break;
                    }

                }else{

                    if (!(ConstantsActivity.getSlotList().containsKey(slots))){
                        customSlot.setError("Invalid slot(s)");
                        flag = 1;
                        break;
                    }
                }
            }

            if (flag != 1){
                customDialog.dismiss();
                data.setSlot(customSlot.getText().toString().toUpperCase().trim());
                setTTSlot(data, back_layout);
            }
        });

        // Front Layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        TimetablePagerAdapter timetablePagerAdapter = new TimetablePagerAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(timetablePagerAdapter);

        setupTimeTable();

        // Side Sheet
        List<CreditDetails> creditsList = new ArrayList<>();
        CreditsAdapter creditsAdapter = new CreditsAdapter(creditsList, this);
        LinearLayoutManager rLayoutManager = new LinearLayoutManager(getApplicationContext());
        rRecyclerView.setLayoutManager(rLayoutManager);
        rRecyclerView.setAdapter(creditsAdapter);

        LiveData<List<CreditDetails>> creditsListLD = database.timeTableDao().loadCreditDetails(ConstantsActivity.getTimeTableId().getValue());
        creditsListLD.observe(MainActivity.this, new Observer<List<CreditDetails>>() {
            @Override
            public void onChanged(List<CreditDetails> registeredCourses) {
                creditsAdapter.updateAdapter(registeredCourses);
                int credits = 0;
                for (CreditDetails credit: registeredCourses)
                    credits += credit.getC();
                creditsNumber.setText(Integer.toString(credits));
                rRecyclerView.smoothScrollToPosition(0);
            }
        });

        //Theme Dialog
        View themeSelector = LayoutInflater.from(this).inflate(R.layout.theme_dialog, null);
        RadioGroup themeRG = themeSelector.findViewById(R.id.themeRadioGroup);
        RadioButton systemThemeRB = themeSelector.findViewById(R.id.systemRadioButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            systemThemeRB.setVisibility(View.VISIBLE);
        else
            systemThemeRB.setVisibility(View.GONE);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
            themeRG.check(R.id.lightRadioButton);
        else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            themeRG.check(R.id.darkRadioButton);
        else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            themeRG.check(R.id.systemRadioButton);
        else {
            if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_NO)
                themeRG.check(R.id.lightRadioButton);
            else if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_YES)
                themeRG.check(R.id.darkRadioButton);
            else if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_UNDEFINED)
                themeRG.check(R.id.darkRadioButton);
        }

        themeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lightRadioButton){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferences.edit().putInt("appTheme", AppCompatDelegate.MODE_NIGHT_NO).apply();
                }else if (checkedId == R.id.darkRadioButton){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.edit().putInt("appTheme", AppCompatDelegate.MODE_NIGHT_YES).apply();
                }else if (checkedId == R.id.systemRadioButton){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    preferences.edit().putInt("appTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply();
                }
            }
        });

        AlertDialog themeDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(themeSelector)
                .setCancelable(true)
                .create();
        themeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        toolbar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()){

                case R.id.fullScreen:

                    startActivity(new Intent(MainActivity.this, LandscapeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    doVibration();
                    return true;

                case R.id.custom:
                    customDialog.show();
                    doVibration();
                    return true;

                case R.id.refresh:
                    refreshRealm();
                    doVibration();
                    return true;

                case R.id.timetable:
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    doVibration();
                    return true;

                case R.id.registered:
                    drawerLayout.openDrawer(GravityCompat.END);
                    doVibration();
                    return true;

                case R.id.appTheme:
                    themeDialog.show();
                    doVibration();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getCurrentFocus().clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initialize() {
        preferences = this.getSharedPreferences("com.tfd.ffcsez", Context.MODE_PRIVATE);
        database = FacultyDatabase.getInstance(getApplicationContext());
        int lastTTId = preferences.getInt("lastTT", 1);
        ConstantsActivity.getTimeTableId().setValue(lastTTId);

        ExecutorClass.getInstance().diskIO().execute(() -> {
            List<Coord> coords = database.timeTableDao().getChosenSlots(ConstantsActivity.getTimeTableId().getValue());
            for (Coord coord: coords){
                if (coord.getRow() != -1)
                    ConstantsActivity.getChosenSlots()[coord.getRow()][coord.getColumn()] = 1;
            }
        });
    }

    private void loadAutoComplete() {
        CourseACAdapter searchAdapter = new CourseACAdapter(MainActivity.this, allCourses);
        courseCodeEditText.setAdapter(searchAdapter);
        FacultyACAdapter facAdapter = new FacultyACAdapter(MainActivity.this, allFaculties);
        facultyNameEditText.setAdapter(facAdapter);

        ExecutorClass.getInstance().diskIO().execute(() -> {
            allCourses = database.facultyDao().loadCourses();

            runOnUiThread(() -> searchAdapter.updateAdapter(allCourses));
        });

        courseCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facultyNameEditText.setText("");
                courseTH = "";
                courseETH = "";
                courseELA = "";
                courseEPJ = "";
                courseSS = "";
                courseLO = "";
                timeFN = "";
                timeAN = "";
                morningChip.setChecked(false);
                afternoonChip.setChecked(false);
                theoryChip.setChecked(false);
                labChip.setChecked(false);
                projectChip.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ExecutorClass.getInstance().diskIO().execute(() -> {
                    allFaculties = database.facultyDao().loadFaculties(s.toString().toUpperCase() + "%");

                    runOnUiThread(() -> facAdapter.updateAdapter(allFaculties));
                });
            }
        });

        facultyNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                courseTH = "";
                courseETH = "";
                courseELA = "";
                courseEPJ = "";
                courseSS = "";
                courseLO = "";
                timeFN = "";
                timeAN = "";
                morningChip.setChecked(false);
                afternoonChip.setChecked(false);
                theoryChip.setChecked(false);
                labChip.setChecked(false);
                projectChip.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setupTimeTable() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void updateFilters() {
        ExecutorClass.getInstance().diskIO().execute(() -> {

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

            runOnUiThread(() -> {

                if(facultyList.size() == 0){
                    animation.setVisibility(View.INVISIBLE);
                    notFound.setVisibility(View.VISIBLE);
                    errorText.setText("No Courses Available");
                    errorText.setVisibility(View.VISIBLE);

                } else {
                    animation.setVisibility(View.INVISIBLE);
                    notFound.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }

                facultyAdapter.updateAdapter(facultyList);
                facultyRecyclerView.smoothScrollToPosition(0);

                searchCount.setText(facultyList.size() + " result(s)");
            });
        });
    }

    public void setTTSlot(FacultyData facultyData, View v) {
        if (!facultyData.getCourseType().equals("EPJ")) {

            String[] slot = facultyData.getSlot().split("[+]");
            Pattern pattern = Pattern.compile("^L");
            Matcher matcher;
            exists = false;

            for (String slotNum : slot) {
                slotNum = slotNum.trim();
                matcher = pattern.matcher(slotNum);

                if (matcher.find()) {
                    int[] coord = getCoord(Integer.parseInt(slotNum.substring(1)));
                    int num;
                    if(coord[1] == 5){
                        num = ((coord[0] + 1)*6);
                    }else{
                        num = ((coord[0])*6) + (coord[1] + 1);
                    }
                    TimeTableData data;

                    if (ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] == 1) {
                        data = new TimeTableData(facultyData, 1, coord[0],
                                coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], true);

                        ExecutorClass.getInstance().diskIO().execute(() -> {
                            List<TimeTableData> clashSlots = database.timeTableDao()
                                    .loadClashSlots(coord[0], coord[1]);
                            for (TimeTableData timeTableData : clashSlots) {

                                if (timeTableData.getEmpName().equals(data.getEmpName()) && timeTableData.getSlot().equals(data.getSlot())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                for (TimeTableData timeTableData : clashSlots) {
                                    timeTableData.setClash(true);
                                    database.timeTableDao().updateDetail(timeTableData);
                                }
                                database.timeTableDao().insertSlot(data);
                                ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                            }
                        });
                    }else {

                        data = new TimeTableData(facultyData, 1, coord[0],
                                coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], false);

                        ExecutorClass.getInstance().diskIO().execute(() -> {
                            database.timeTableDao().insertSlot(data);
                            ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                        });
                    }
                }else {

                    if (ConstantsActivity.getSlotList().get(slotNum) != null) {
                        for (int i = 0; i < ConstantsActivity.getSlotList().get(slotNum).length; i++) {
                            int[] coord = getCoord(ConstantsActivity.getSlotList().get(slotNum)[i]);
                            int num;
                            if(coord[1] == 5){
                                num = ((coord[0] + 1)*6);
                            }else{
                                num = ((coord[0])*6) + (coord[1] + 1);
                            }
                            TimeTableData data;

                            if (ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] == 1) {
                                data = new TimeTableData(facultyData, 1, coord[0],
                                        coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], true);

                                ExecutorClass.getInstance().diskIO().execute(() -> {
                                    List<TimeTableData> clashSlots = database.timeTableDao()
                                            .loadClashSlots(coord[0], coord[1]);

                                    for (TimeTableData timeTableData : clashSlots) {
                                        if (timeTableData.getEmpName().equals(data.getEmpName()) && timeTableData.getSlot().equals(data.getSlot())) {
                                            exists = true;
                                            break;
                                        }
                                    }

                                    if (!exists) {
                                        for (TimeTableData timeTableData : clashSlots) {
                                            timeTableData.setClash(true);
                                            database.timeTableDao().updateDetail(timeTableData);
                                        }

                                        database.timeTableDao().insertSlot(data);
                                        ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                                    }
                                });
                            } else {
                                data = new TimeTableData(facultyData, 1, coord[0],
                                        coord[1], slotNum, ConstantsActivity.getLabTiming().get(num)[0], ConstantsActivity.getLabTiming().get(num)[1], false);

                                ExecutorClass.getInstance().diskIO().execute(() -> {
                                    database.timeTableDao().insertSlot(data);
                                    ConstantsActivity.getChosenSlots()[coord[0]][coord[1]] = 1;
                                });
                            }
                        }
                    }
                }
            }
        }else {
            ExecutorClass.getInstance().diskIO().execute(() -> {
                TimeTableData data = new TimeTableData(facultyData, 1, -1,
                        -1, facultyData.getSlot(), "-:-", "-:-", false);
                database.timeTableDao().insertSlot(data);
            });
        }

        facultyAdapter.notifyDataSetChanged();

        Snackbar.make(v, "Course added successfully - " + facultyData.getCourseCode() + " - " + facultyData.getCourseType(),
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                .setTextColor(getResources().getColor(R.color.snackbar_text))
                .show();
    }

    private int[] getCoord(int num) {
        int[] coord = new int[2];
        int r, c;

        if (num%6 == 0){
            r = num/6 - 1;
            c = 5;
        }else{
            r = num/6;
            c = num%6 - 1;
        }

        coord[0] = r;
        coord[1] = c;
        return coord;
    }

    private void refreshRealm() {
        Snackbar.make(backdropLayout, "Fetching the latest data from the server...",
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                .setTextColor(getResources().getColor(R.color.snackbar_text))
                .show();

        final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("cloudload.json",true);
        lottieDialog.setCancelable(false);
        lottieDialog.show(getFragmentManager(),"refreshDialog");

        Realm.init(this);
        App app = new App(new AppConfiguration.Builder("ffcsapp-mwjba").build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.d("Hello", "Successfully authenticated anonymously.");

                Log.d("Hello", "afterlogin");
                User user = app.currentUser();

                if (user != null) {
                    SyncConfiguration config = new SyncConfiguration.Builder(user, "Open")
                            .waitForInitialRemoteData()
                            .build();
                    Log.d("Hello", "config");

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(@NonNull Realm realm) {
                            Log.d("Hello", "Realm created");
                            RealmResults<CourseData> data = realm.where(CourseData.class).findAllAsync();
                            data.addChangeListener(courseData -> {
                                ExecutorClass.getInstance().diskIO().execute(() ->
                                        database.facultyDao().deleteAll());

                                for (CourseData course : data) {
                                    FacultyData faculty = new FacultyData(course);
                                    ExecutorClass.getInstance().diskIO().execute(() ->
                                            database.facultyDao().insertDetail(faculty));
                                }

                                int size = courseData.size();
                                Log.d("Hello", Integer.toString(size));

                                if (data.size() > 0) {
                                    lottieDialog.dismiss();
                                    Snackbar.make(backdropLayout, "You've got the latest updates. Enjoy!",
                                            Snackbar.LENGTH_LONG)
                                            .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                                            .setTextColor(getResources().getColor(R.color.snackbar_text))
                                            .show();

                                    realm.close();

                                    user.logOutAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.d("Hello", "Successfully logged out.");
                                        } else {
                                            Log.d("Hello", "Failed to log out, error: " + result.getError());
                                        }
                                    });
                                }
                            });
                        }

                        /*@Override
                        public void onError(Throwable exception) {
                            super.onError(exception);
                            Log.d("Hello", "Failed to create Realm" + exception.getMessage());
                            lottieDialog.dismiss();

                            Snackbar.make(backdropLayout, "Failed to get data. " + exception.getMessage(),
                                    Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                                    .setTextColor(getResources().getColor(R.color.snackbar_text))
                                    .show();

                            user.logOutAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.d("Hello", "Successfully logged out.");
                                } else {
                                    Log.d("Hello", "Failed to log out, error: " + result.getError());
                                }
                            });
                        }*/
                    });
                }

            } else {
                lottieDialog.dismiss();

                Snackbar.make(backdropLayout, "Couldn't fetch the data. Please try again in sometime.",
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                        .setTextColor(getResources().getColor(R.color.snackbar_text))
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if(backdropIsOpen){
            backdropLayout.close();
        } else {
            super.onBackPressed();
        }
    }

}
