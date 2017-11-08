package dev.panel;

import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
        brightnessBar.setMax(255);
        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

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
