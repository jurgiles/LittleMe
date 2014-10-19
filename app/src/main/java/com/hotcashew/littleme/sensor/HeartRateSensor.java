package com.hotcashew.littleme.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Date;

public class HeartRateSensor implements SensorEventListener2 {
    private final SensorManager sensorManager;
    private final String TAG = HeartRateSensor.class.getSimpleName();
    private final HeartRateSensorCallback callbacks;

    private int additionalHeartRate = 0;
    private float lastRate = 0.0f;
    private long lastRateTime = 0;
    private long lastAccuracy = 0;

    public HeartRateSensor(Context context, HeartRateSensorCallback callbacks) {
        this.callbacks = callbacks;
        sensorManager = ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE));
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(TAG, "flush completed");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lastRate = event.values[0];
        lastRateTime = new Date().getTime();

        callbacks.onSensorChanged(new HeartSensorReading(lastRate, (long) lastRate, lastAccuracy));

        Log.d(TAG, String.format("Heart readings: %s, additional: %s, total: %s", lastRate, additionalHeartRate, lastRate + additionalHeartRate));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        lastAccuracy = accuracy;
        Log.d(TAG, "Sensor accuracy changed to " + accuracy);
    }


    public void onStart() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onStop() {
        sensorManager.unregisterListener(this);
    }

    public HeartSensorReading getLastReading() {
        return new HeartSensorReading(lastRate, lastRateTime, lastAccuracy);
    }

    public void debugAddRate(int additionalRate) {
        additionalHeartRate += additionalRate;
    }

    public void debugSubRate(int subtractRate) {
        additionalHeartRate -= subtractRate;
    }

    public static class HeartSensorReading {
        HeartSensorReading(float lastRate, long lastTime, long lastAccuracy) {
            this.lastRate = lastRate;
            this.lastTime = lastTime;
            this.lastAccuracy = lastAccuracy;
        }

        public float lastRate;
        public long lastTime;
        public long lastAccuracy;
    }
}
