package com.mind.sample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mind.libs.MindScore;
import com.mind.libs.MindScoreView;
import com.mind.libs.R;

public class SampleActivity extends AppCompatActivity {

    MindScoreView mvSafeScore;
    public static final int REQUEST_LOCATION = 1;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        if(!isVerifyLocationPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            requestLocationFinePermissions(this);
        }

        mvSafeScore = findViewById(R.id.safe_score);
        MindScore miNd = new MindScore(mvSafeScore, this);


        miNd.clearScore();

        miNd.stopDrive();

        miNd.startDrive();

        miNd.getScore();


    }

    public static boolean isVerifyLocationPermission(Activity activity, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(activity, permission);
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationFinePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
    }

}
