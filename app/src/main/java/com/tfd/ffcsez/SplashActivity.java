package com.tfd.ffcsez;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.tfd.ffcsez.database.ExecutorClass;
import com.tfd.ffcsez.database.FacultyData;
import com.tfd.ffcsez.database.FacultyDatabase;
import com.tfd.ffcsez.models.CourseData;

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

    @BindView(R.id.loadAnimation) LottieAnimationView loadAnimation;
    @BindView(R.id.loadTextView) TextView loadText;
    @BindView(R.id.loadLayout) LinearLayout loadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        FacultyDatabase database = FacultyDatabase.getInstance(getApplicationContext());
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.tfd.ffcsez", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getBoolean("firstTime", true)) {
                    Log.d("Hello", "firstTime");
                    Realm.init(SplashActivity.this);
                    app = new App(new AppConfiguration.Builder("ffcsapp-mwjba").build());

                    loadLayout.setVisibility(View.VISIBLE);
                    loadAnimation.playAnimation();
                    loadText.setText("Setting up for first time use...");

                    Credentials credentials = Credentials.anonymous();
                    app.loginAsync(credentials, result -> {
                        if (result.isSuccess()) {
                            Log.d("Hello", "Successfully authenticated anonymously.");

                            runOnUiThread(() -> {
                                Log.d("Hello", "afterlogin");
                                user = app.currentUser();
                                Log.d("Hello", user.toString());

                                if (user != null) {
                                    SyncConfiguration config = new SyncConfiguration.Builder(user, "Open")
                                            .waitForInitialRemoteData()
                                            .build();
                                    Log.d("Hello", "config");

                                    Realm.getInstanceAsync(config, new Realm.Callback() {
                                        @Override
                                        public void onSuccess(Realm realm) {
                                            Log.d("Hello", "Realm created");
                                            SplashActivity.this.realm = realm;

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
                                                    sharedPreferences.edit().putBoolean("firstTime", false).apply();
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
                                            Log.d("Hello", "Failed to create Realm" + exception.getMessage());
                                            Toast.makeText(SplashActivity.this,
                                                    "Couldn't login securely to the server. " + exception.getMessage(),
                                                    Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            });

                        } else {
                            Toast.makeText(SplashActivity.this,
                                    "Couldn't connect to the server. Downloads will take place the next time you open the app or by using the refresh option.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }else {
                    loadLayout.setVisibility(View.GONE);

                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }, 1000);
                }
            }
        }, 1100);
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