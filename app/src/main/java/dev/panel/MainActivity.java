package dev.panel;

import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity
{

    private int brightness;
    private ContentResolver contentResolver;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();
        window = getWindow();

        try
        {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }

        final SeekBar brightnessBar = (SeekBar) findViewById(R.id.brightness_bar);
        brightnessBar.setMax(100);
//        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = i;
                getWindow().setAttributes(layoutParams);
                Log.wtf("Tag", "WTF");
//                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        final SeekBar systemVolumeBar = (SeekBar) findViewById(R.id.system_volume_bar);
        systemVolumeBar.setMax(100);
        systemVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                Log.d("System Volume", String.valueOf("System Volume: " + i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        final SeekBar mediaVolumeBar = (SeekBar) findViewById(R.id.media_volume_bar);
        mediaVolumeBar.setMax(100);
        mediaVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                Log.d("Media Volume", String.valueOf("Media Volume: " + i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        Log.d ("Brightness", String.valueOf(brightness));
    }
}
