package com.example.womensateyapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class ShakeDetectionService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    public static boolean isAudioPlaying = false;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SensorManager and accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        // Initialize the Vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Register the sensor listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;

        // Detect shaking motion
        if (Math.abs(values[0]) > 20 || Math.abs(values[1]) > 20 || Math.abs(values[2]) > 20) {
            if (isAudioPlaying) {
                stopAlarm();
            } else {
             isAudioPlaying = true;
                activateAlarm();
           }

        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null || mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isAudioPlaying = false;
            mediaPlayer = null;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    private void activateAlarm() {

        isAudioPlaying = true;
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        }
        // Start the alarm sound
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Vibrate the device
        long[] pattern = {0, 1000, 1000};
        vibrator.vibrate(pattern, 0);

        // You can also add other actions such as sending an alert to a contact or calling emergency services.
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the sensor listener
        sensorManager.unregisterListener(this);

        // Release resources
        mediaPlayer.stop();
        mediaPlayer.release();
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
