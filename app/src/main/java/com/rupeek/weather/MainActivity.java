package com.rupeek.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.rupeek.weather.databinding.ActivityMainBinding;

import static com.rupeek.weather.utils.ToastUtils.showToastLong;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_FOREGROUND = 100;
    private static final long SPLASHTIME = 2000;
    private ActivityMainBinding activityMainBinding;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        checkForegroundLocationPermissionAndRequest();
    }

    private void checkForegroundLocationPermissionAndRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean hasLocationPermission = (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOREGROUND);
            } else {
                onForegroundPermissionGranted(true);
            }
        } else {
            onForegroundPermissionGranted(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[]
                                                   grantResults) {
        if (requestCode == REQUEST_CODE_FOREGROUND) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onForegroundPermissionGranted(true);
            } else {
                onForegroundPermissionGranted(false);
            }
        }
    }

    private void onForegroundPermissionGranted(boolean isGrant) {
        if (!isGrant) {
            showToastLong(MainActivity.this, "Location Permission is required");
            //or showSnackbar
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkBackgroundLocationPermissionAndRequest();
        } else {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }, SPLASHTIME);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkBackgroundLocationPermissionAndRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean hasLocationPermission = (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasLocationPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_FOREGROUND);
            } else {
                onBackgroundPermissionGranted(true);
            }
        } else {
            onBackgroundPermissionGranted(true);
        }
    }

    private void onBackgroundPermissionGranted(boolean isGrant) {
        if (!isGrant) {
            showToastLong(MainActivity.this, "Permissions not granted");
            //or showSnackbar
            finish();
            return;
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, SPLASHTIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}