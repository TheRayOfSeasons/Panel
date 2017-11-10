package dev.panel;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import dev.panel.utils.L;

public class MainActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener
{
    private int brightness;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();
        window = getWindow();

        try
        {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }

        SeekBar brightnessBar = (SeekBar) findViewById(R.id.brightness_bar);
        brightnessBar.setMax(100);
//        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(this);

        SeekBar systemVolumeBar = (SeekBar) findViewById(R.id.system_volume_bar);
        systemVolumeBar.setMax(100);
        systemVolumeBar.setOnSeekBarChangeListener(this);

        SeekBar mediaVolumeBar = (SeekBar) findViewById(R.id.media_volume_bar);
        mediaVolumeBar.setMax(100);
        mediaVolumeBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        switch (seekBar.getId())
        {
            case R.id.brightness_bar:
                L.m("b", i);

                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.screenBrightness = i;
                window.setAttributes(layoutParams);
//                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, i);
                break;

            case R.id.system_volume_bar:
                L.m("sv", i);
                break;

            case R.id.media_volume_bar:
                L.m("mv", i);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}
