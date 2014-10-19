package com.hotcashew.littleme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hotcashew.littleme.R;
import com.hotcashew.littleme.sensor.HeartRateSensor;
import com.hotcashew.littleme.sensor.HeartRateSensorCallback;

import java.text.SimpleDateFormat;

public class MainWearActivity extends Activity implements HeartRateSensorCallback {
    public static final int HEART_RATE_CHANGE = 10;

    private final SimpleDateFormat readableDateFormat = new SimpleDateFormat("kk:mm:ss:SSS");

    private TextView sensorAccuracyView;
    private TextView sensorLastUpdateView;
    private TextView heartRateView;

    private final String TAG = MainWearActivity.class.getSimpleName();
    private HeartRateSensor heartRateSensor;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        Log.d(TAG, "onCreate");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                sensorAccuracyView = (TextView) stub.findViewById(R.id.sensor_accuracy);
                heartRateView = (TextView) stub.findViewById(R.id.sensor_heart_rate);
                sensorLastUpdateView = (TextView) stub.findViewById(R.id.sensor_last_update);
                progressBar = (ProgressBar) stub.findViewById(R.id.progress_bar);
            }
        });

        heartRateSensor = new HeartRateSensor(this, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        heartRateSensor.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        heartRateSensor.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void upHeartClick(View view) {
        heartRateSensor.debugAddRate(HEART_RATE_CHANGE);
    }

    public void downHeartClick(View view) {
        heartRateSensor.debugSubRate(HEART_RATE_CHANGE);
    }

    @Override
    public void onSensorChanged(HeartRateSensor.HeartSensorReading reading) {
        sensorLastUpdateView.setText(readableDateFormat.format(reading.lastTime));
        heartRateView.setText("" + reading.lastRate);
        sensorAccuracyView.setText("[acc " + reading.lastAccuracy + "]");

        if(reading.lastRate > 100){
            progressBar.incrementProgressBy(1);

            if(progressBar.getProgress() >= 100){
                Toast.makeText(this, "You do it!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void stopClick(View view) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void startClick(View view) {
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
    }
}
