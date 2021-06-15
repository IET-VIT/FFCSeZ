package com.tfd.ffcsez;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.adapters.CourseACAdapter;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.models.CourseData;
import com.tfd.ffcsez.models.CourseDetails;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Hello";
    Realm realm;
    User user;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView loadAnimation = findViewById(R.id.loadAnimation);
        LinearLayout loadLayout = findViewById(R.id.loadLayout);
        TextView loadText = findViewById(R.id.loadTextView);
        FacultyDatabase database = FacultyDatabase.getInstance(getApplicationContext());

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.tfd.ffczez",
                Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstTime", true)) {
            Log.d(LOG_TAG, "firstTime");
            Realm.init(this);
            app = new App(new AppConfiguration.Builder("ffcsapp-mwjba").build());

            loadLayout.setVisibility(View.VISIBLE);
            loadAnimation.playAnimation();
            loadText.setText("Setting up for first time use...");

            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {
                    Log.d(LOG_TAG, "Successfully authenticated anonymously.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadText.setText("Please restart the app to see the updated changes");
                        }
                    });

                } else {
                    loadText.setText(result.getError().toString());
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
                        SplashActivity.this.realm = realm;

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
                                loadAnimation.cancelAnimation();
                                loadLayout.setVisibility(View.GONE);
                                startActivity(new Intent(SplashActivity.this, GetStartedActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable exception) {
                        super.onError(exception);
                        loadText.setText(exception.getMessage());
                        Log.d(LOG_TAG, "Failed to create Realm" + exception.getMessage());
                    }
                });
            }
        }else{
            loadLayout.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();

        if (user != null) {
            user.logOutAsync(result -> {
                if (result.isSuccess()) {
                    Log.d(LOG_TAG, "Successfully logged out.");
                } else {
                    Log.d(LOG_TAG, "Failed to log out, error: " + result.getError());
                }
            });
        }
    }

}