package com.hotcashew.littleme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hotcashew.littleme.R;
import com.hotcashew.littleme.sensor.HeartRateSensor;
import com.hotcashew.littleme.sensor.HeartRateSensorCallback;

import java.text.SimpleDateFormat;

public class MainWearActivity extends Activity implements HeartRateSensorCallback {
    private final SimpleDateFormat readableDateFormat = new SimpleDateFormat("kk:mm:ss:SSS");

    public static final int HEART_RATE_JUMP = 10;
    private TextView sensorAccuracyView;
    private TextView sensorLastUpdateView;
    private TextView heartRateView;


    private final String TAG = MainWearActivity.class.getSimpleName();
    private HeartRateSensor heartRateSensor;

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
        heartRateSensor.debugAddRate(HEART_RATE_JUMP);
    }

    public void downHeartClick(View view) {
        heartRateSensor.debugSubRate(HEART_RATE_JUMP);
    }

    @Override
    public void onSensorChanged(HeartRateSensor.HeartSensorReading reading) {
        HeartRateSensor.HeartSensorReading lastReading = heartRateSensor.getLastReading();
        sensorLastUpdateView.setText(readableDateFormat.format(lastReading.lastTime));
        heartRateView.setText("" + lastReading.lastRate);
        sensorAccuracyView.setText("[acc " + reading.lastAccuracy + "]");
    }
}
