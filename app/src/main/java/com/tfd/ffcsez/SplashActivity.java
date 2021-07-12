package com.tfd.ffcsez;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.database.TTDetails;
import com.tfd.ffcsez.models.CourseData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class SplashActivity extends AppCompatActivity {

    private Realm realm;
    private User user;
    private App app;
    private int count;

    @BindView(R.id.loadAnimation) LottieAnimationView loadAnimation;
    @BindView(R.id.loadTextView) TextView loadText;
    @BindView(R.id.loadLayout) LinearLayout loadLayout;
    @BindView(R.id.ietLogo) ImageView ietLogo;
    @BindView(R.id.tfdLogo) ImageView tfdLogo;
    @BindView(R.id.madeText) TextView madeText;
    @BindView(R.id.splashProgressBar) ProgressBar progressBar;
    @BindView(R.id.splashProgressText) TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        //getWindow().setEnterTransition(new Fade().setDuration(5000));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.splash_bg));
        getWindow().setNavigationBarColor(getColor(R.color.custom_course_bg));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.tfd.ffcsez", Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt("appTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));

        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        FacultyDatabase database = FacultyDatabase.getInstance(getApplicationContext());

        if (sharedPreferences.getBoolean("firstTime", true)) {
            Log.d("Hello", "firstTime");

            FirebaseMessaging.getInstance().subscribeToTopic("all").addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sharedPreferences.edit().putBoolean("fcmsubs", false).apply();
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sharedPreferences.edit().putBoolean("fcmsubs", true).apply();
                }
            });

            ExecutorClass.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    database.timeTableDao().deleteAll();
                    database.ttDetailsDao().deleteAll();

                    TTDetails details = new TTDetails("XXDefault TimetableXX");
                    database.ttDetailsDao().insertTimeTable(details);
                    List<TTDetails> timeTable = database.ttDetailsDao().getTimeTable(details.getTimeTableName());
                    runOnUiThread(() -> {
                        sharedPreferences.edit().putInt("lastTT", timeTable.get(0).getTimeTableId()).apply();
                        ConstantsActivity.getTimeTableId().setValue(sharedPreferences.getInt("lastTT", 1));
                    });

                }
            });

            Realm.init(SplashActivity.this);
            app = new App(new AppConfiguration.Builder("ffcsapp-mwjba").build());
            ietLogo.setVisibility(View.GONE);
            tfdLogo.setVisibility(View.GONE);
            madeText.setVisibility(View.GONE);
            loadLayout.setVisibility(View.VISIBLE);
            loadAnimation.playAnimation();
            loadText.setText("Setting up for first time use...Please do not close the app");
            progressBar.setIndeterminate(true);

            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {
                    Log.d("Hello", "Successfully authenticated anonymously.");

                    Log.d("Hello", "afterlogin");
                    user = app.currentUser();

                    if (user != null) {
                        SyncConfiguration config = new SyncConfiguration.Builder(user, "Open")
                                .waitForInitialRemoteData()
                                .build();
                        Log.d("Hello", "config");

                        count = 0;
                        FirebaseDatabase.getInstance().getReference().child("resultCount").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    if (dataSnapshot.exists()) {
                                        count = Integer.parseInt(dataSnapshot.getValue().toString());
                                        sharedPreferences.edit().putInt("realmCount", count).apply();
                                    }
                                } else {
                                    count = 0;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                count = 0;
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Realm.getInstanceAsync(config, new Realm.Callback() {
                                    @Override
                                    public void onSuccess(@NonNull Realm realm) {
                                        Log.d("Hello", "Realm created");
                                        SplashActivity.this.realm = realm;

                                        RealmResults<CourseData> data = realm.where(CourseData.class).findAllAsync();
                                        data.addChangeListener(courseData -> {
                                            int size = courseData.size();
                                            Log.d("Hello", Integer.toString(size));
                                            Log.d("HelloCount", Integer.toString(count));

                                            if (count != 0){
                                                progressBar.setIndeterminate(false);
                                                progressBar.setMax(count);
                                                progressBar.setProgress(0);
                                                progressText.setVisibility(View.VISIBLE);
                                                progressText.setText(progressBar.getProgress() + "/" + count);
                                                progressBar.setSecondaryProgress(courseData.size());
                                            }

                                            if ((count == 0 && courseData.size() > count) || (count != 0 && courseData.size() == count)) {
                                                ExecutorClass.getInstance().diskIO().execute(() ->
                                                        database.facultyDao().deleteAll());

                                                for (CourseData course : courseData) {
                                                    FacultyData faculty = new FacultyData(course);
                                                    ExecutorClass.getInstance().diskIO().execute(() -> {
                                                            database.facultyDao().insertDetail(faculty);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (!progressBar.isIndeterminate()) {
                                                                        progressBar.setProgress(progressBar.getProgress() + 1);
                                                                        progressText.setText(progressBar.getProgress() + "/" + count);
                                                                        Log.i("HelloProgress", Integer.toString(progressBar.getProgress()));
                                                                        if (progressBar.getProgress() == count) {
                                                                            loadAnimation.cancelAnimation();
                                                                            loadLayout.setVisibility(View.GONE);
                                                                            ietLogo.setVisibility(View.VISIBLE);
                                                                            tfdLogo.setVisibility(View.VISIBLE);
                                                                            madeText.setVisibility(View.VISIBLE);
                                                                            startActivity(new Intent(SplashActivity.this, GetStartedActivity.class)
                                                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                            finish();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                    });
                                                }

                                                sharedPreferences.edit().putBoolean("firstTime", false).apply();

                                                FirebaseDatabase.getInstance().getReference().child("lastUpdated").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot != null) {
                                                            if (dataSnapshot.exists()) {
                                                                String text = dataSnapshot.getValue().toString();
                                                                sharedPreferences.edit().putString("lastUpdated", text).apply();
                                                            }
                                                        } else {
                                                            String text = "Last updated for";
                                                            sharedPreferences.edit().putString("lastUpdated", text).apply();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        String text = "Last updated for";
                                                        sharedPreferences.edit().putString("lastUpdated", text).apply();
                                                    }
                                                });

                                                if (count == 0) {
                                                    loadAnimation.cancelAnimation();
                                                    loadLayout.setVisibility(View.GONE);
                                                    ietLogo.setVisibility(View.VISIBLE);
                                                    tfdLogo.setVisibility(View.VISIBLE);
                                                    madeText.setVisibility(View.VISIBLE);
                                                }

                                                realm.close();

                                                if (user != null) {
                                                    user.logOutAsync(result -> {
                                                        if (result.isSuccess()) {
                                                            Log.d("Hello", "Successfully logged out.");
                                                        } else {
                                                            Log.d("Hello", "Failed to log out, error: " + result.getError());
                                                        }
                                                    });
                                                }

                                                if (progressBar.isIndeterminate()) {
                                                    startActivity(new Intent(SplashActivity.this, GetStartedActivity.class)
                                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    finish();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(@NotNull Throwable exception) {
                                        super.onError(exception);
                                        loadText.setText(exception.getMessage());
                                        Log.d("Hello", "Failed to create Realm" + exception.getMessage());
                                        Toast.makeText(SplashActivity.this,
                                                "Couldn't login securely to the server. " + exception.getMessage(),
                                                Toast.LENGTH_LONG).show();

                                        if (user != null) {
                                            user.logOutAsync(result -> {
                                                if (result.isSuccess()) {
                                                    Log.d("Hello", "Successfully logged out.");
                                                } else {
                                                    Log.d("Hello", "Failed to log out, error: " + result.getError());
                                                }
                                            });
                                        }

                                        startActivity(new Intent(SplashActivity.this, GetStartedActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                });
                            }
                        });
                    }

                } else {
                    Toast.makeText(SplashActivity.this,
                            "Couldn't connect to the server. Downloads will take place the next time you open the app or by using the refresh option.",
                            Toast.LENGTH_LONG).show();

                    if (realm != null)
                        realm.close();

                    if (user != null) {
                        user.logOutAsync(result1 -> {
                            if (result1.isSuccess()) {
                                Log.d("Hello", "Successfully logged out.");
                            } else {
                                Log.d("Hello", "Failed to log out, error: " + result1.getError());
                            }
                        });
                    }

                    startActivity(new Intent(SplashActivity.this, GetStartedActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });
        }else {
            loadLayout.setVisibility(View.GONE);
            ietLogo.setVisibility(View.VISIBLE);
            tfdLogo.setVisibility(View.VISIBLE);
            madeText.setVisibility(View.VISIBLE);

            if (!sharedPreferences.getBoolean("fcmsubs", false)){
                FirebaseMessaging.getInstance().subscribeToTopic("all").addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sharedPreferences.edit().putBoolean("fcmsubs", false).apply();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sharedPreferences.edit().putBoolean("fcmsubs", true).apply();
                    }
                });
            }

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Intent notifIntent = getIntent();
                /*if (notifIntent != null){
                    if (notifIntent.getStringExtra("refreshNotif") != null
                            && notifIntent.getStringExtra("refreshNotif").equals("true"))
                        intent.putExtra("refreshNotif", true);
                }*/
                if (sharedPreferences.getBoolean("refreshNotif", false)){
                    intent.putExtra("refreshNotif", true);
                }
                startActivity(intent);
                finish();
            }, 1600);
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
                    Log.d("Hello", "Successfully logged out.");
                } else {
                    Log.d("Hello", "Failed to log out, error: " + result.getError());
                }
            });
        }
    }
}