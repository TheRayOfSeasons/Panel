package dev.panel.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Toggles
{
    private Context context;
    @SuppressLint("NewApi")
    public Toggles (Context context)
    {
        this.context = context;

        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        cameraHasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        //TODO Implement Permission to MainActivity
        isFlashEnabled = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        wifiManager = (WifiManager) context
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        wifiIsEnabled = wifiManager.isWifiEnabled();
        airplaneModeEnabled = Settings.Global.getInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        autoRotateEnabled = android.provider.Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(audioManager.getRingerMode());
    }

    private WifiManager wifiManager;
    private boolean wifiIsEnabled,
                    airplaneModeEnabled,
                    autoRotateEnabled,
                    isFlashEnabled;

    BluetoothAdapter bluetoothAdapter;

    AudioManager audioManager;
    private enum AudioModes
    {
        NORMAL,
        SILENT,
        VIBRATE
    };
    AudioModes audioModes;

    //flashlight related fields
    CameraManager cameraManager;
    private boolean flashLightStatus = false;
    private static final int CAMERA_REQUEST = 50;
    private final boolean cameraHasFlash;

    @SuppressLint("NewApi")
    private void turnOnFlashlight()
    {
        try
        {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
        } catch (@SuppressLint("NewApi") CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void turnOffFlashlight()
    {
        try
        {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
        } catch (@SuppressLint("NewApi") CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void flashlight()
    {
        L.m("toggle", "flashlight");

        //TODO Implement this permission request in MainActivity (Part 2)
        ActivityCompat.requestPermissions((Activity) context,
                new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        if (cameraHasFlash)
        {
            if (flashLightStatus)
                turnOffFlashlight();
            else
                turnOnFlashlight();
        }
        else
        {
            Toast.makeText(context, "Flashlight not available on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public void airplaneMode ()
    {
        L.m("toggle", "airplane mode");

        int enabler;
        airplaneModeEnabled = !airplaneModeEnabled;
        if (airplaneModeEnabled)
            enabler = 1;
        else
            enabler = 0;

        Settings.Global.putInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, enabler);
    }

    public void volumeModes ()
    {
        L.m("toggle", "volume modes");

        switch (audioManager.getRingerMode())
        {
            case AudioManager.RINGER_MODE_NORMAL:
                audioModes = AudioModes.NORMAL;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                audioModes = AudioModes.SILENT;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioModes = AudioModes.VIBRATE;
                break;
        }

        switch (audioModes)
        {
            case NORMAL:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case SILENT:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }
    }

    public void wifi ()
    {
        L.m("toggle", wifiIsEnabled? "wifi enabled" : "wifi disabled");

        wifiIsEnabled = !wifiIsEnabled;
        wifiManager.setWifiEnabled(wifiIsEnabled);
    }

    public void bluetooth ()
    {
        L.m("toggle", "bluetooth");

        if (!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
        else
            bluetoothAdapter.disable();
    }

    public void rotation ()
    {
        L.m("toggle", "rotation");

        int enabler;
        autoRotateEnabled = !autoRotateEnabled;
        if (autoRotateEnabled)
            enabler = 1;
        else
            enabler = 0;

        android.provider.Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, enabler);
    }

}
