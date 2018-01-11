package dev.panel.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.View;

public class Toggles
{
    private Context context;
    public Toggles (Context x)
    {
        context = x;

        wifiManager = (WifiManager) x
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        wifiIsEnabled = wifiManager.isWifiEnabled();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        audioManager = (AudioManager) x.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(audioManager.getRingerMode());
    }

    private WifiManager wifiManager;
    private boolean wifiIsEnabled;

    private final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;

    AudioManager audioManager;
    private enum AudioModes
    {
        NORMAL,
        SILENT,
        VIBRATE
    };
    AudioModes audioModes;

    //TODO Sync TogglesFragment OnCreate Data
    public void flashlight()
    {}

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
        wifiIsEnabled = !wifiIsEnabled;
        L.m("toggle", wifiIsEnabled? "wifi enabled" : "wifi disabled");
        wifiManager.setWifiEnabled(wifiIsEnabled);
    }

    public void bluetooth ()
    {
        L.m("toggle", "bluetooth");
        if (!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
        }
        else
        {
            mBluetoothAdapter.disable();
        }
    }

    public void rotation ()
    {
        L.m("toggle", "rotation");

        if  (android.provider.Settings.System.getInt(context.getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
        {
            android.provider.Settings.System.putInt(context.getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
        }
        else
        {
            android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        }
    }


}
