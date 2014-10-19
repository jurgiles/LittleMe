package com.hotcashew.littleme;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainWearActivity extends Activity implements SensorEventListener2 {

    private TextView sensorAccuracyView;
    private TextView sensorLastUpdateView;
    private TextView heartRateView;

    private int additionalHeartRate = 0;

    private SensorManager sensorManager;
    private final String TAG = MainWearActivity.class.getSimpleName();

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

        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(TAG, "flush completed");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartReading = event.values[0] + additionalHeartRate;

        SimpleDateFormat readableDateFormat = new SimpleDateFormat("k:m:s:SSS");

        sensorLastUpdateView.setText(readableDateFormat.format(new Date().getTime()));
        heartRateView.setText("" + heartReading);

        Log.d(TAG, "Heart readings: " + heartReading);
        Log.d(TAG, "Additional heart rate: " + additionalHeartRate);
        Log.d(TAG, "Total test heart rate: " + (heartReading + additionalHeartRate));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sensorAccuracyView.setText("[acc " + accuracy + "]");

        Log.d(TAG, "Sensor accuracy changed to " + accuracy);
    }

    public void upHeartClick(View view) {
        additionalHeartRate += 10;
    }

    public void downHeartClick(View view) {
        additionalHeartRate -=10;
    }
}
