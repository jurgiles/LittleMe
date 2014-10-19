package com.hotcashew.littleme;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.common.primitives.Floats;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainWearActivity extends Activity implements SensorEventListener2{

    private TextView sensorAccuracyView;
    private TextView sensorLastUpdateView;
    private TextView heartRateView;

    private SensorManager sensorManager;
    private final String TAG = MainWearActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                sensorAccuracyView = (TextView) stub.findViewById(R.id.sensor_accuracy);
                heartRateView = (TextView) stub.findViewById(R.id.sensor_heart_rate);
                sensorLastUpdateView = (TextView) stub.findViewById(R.id.sensor_last_update);
            }
        });

        sensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
    }


    @Override
    protected void onStart() {
        super.onStart();

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(TAG, "flush completed");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String heartReadings = TextUtils.join(", ", Floats.asList(event.values));

        SimpleDateFormat readableDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        sensorLastUpdateView.setText(readableDateFormat.format(new Date()));
        heartRateView.setText(heartReadings);

        Log.d(TAG, "Heart readings: " + heartReadings);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sensorAccuracyView.setText("Sensor accuracy changed to " + accuracy);

        Log.d(TAG, "Sensor accuracy changed to " + accuracy);
    }


}
