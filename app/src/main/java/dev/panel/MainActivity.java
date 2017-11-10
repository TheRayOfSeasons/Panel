package dev.panel;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import dev.panel.utils.L;

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener
{
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        init();
    }

    private void checkPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.System.canWrite(this))
            {
                Intent writeSettings = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                writeSettings.setData(Uri.parse("package:" + getPackageName()));
                startActivity(writeSettings);
            }
        }
    }

    private void init()
    {
        contentResolver = getContentResolver();

        //Update when brightness is changed by other apps
        SeekBar brightnessBar =  (SeekBar) findViewById(R.id.brightness_bar);
        brightnessBar.setMax(255);
        brightnessBar.setProgress(getScreenBrightness());
        brightnessBar.setOnSeekBarChangeListener(this);

        SeekBar systemVolumeBar = (SeekBar) findViewById(R.id.system_volume_bar);
        systemVolumeBar.setMax(100);
        systemVolumeBar.setOnSeekBarChangeListener(this);

        SeekBar mediaVolumeBar = (SeekBar) findViewById(R.id.media_volume_bar);
        mediaVolumeBar.setMax(100);
        mediaVolumeBar.setOnSeekBarChangeListener(this);
    }

//    @Override
//    public void onRequestPermissionsResult
//        (
//            int requestCode,
//            @NonNull String[] permissions,
//            @NonNull int[] grantResults
//        )
//    {
//        if(requestCode == PERMISSION_REQUEST_CODE)
//        {
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                init();
//            else
//                new AlertDialog.Builder(this)
//                .setTitle("Permission Request")
//                .setMessage("This app needs the permission requested to function. Please grant " +
//                            "the permission requested.")
//                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i)
//                    {
//                        checkPermissions();
//                    }
//                })
//                .show();
//        }
//    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        switch (seekBar.getId())
        {
            case R.id.brightness_bar:
                L.m("b", i);

                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, i);
                break;

            case R.id.system_volume_bar:
                L.m("sv", i);
                break;

            case R.id.media_volume_bar:
                L.m("mv", i);
                break;
        }
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override public void onStopTrackingTouch(SeekBar seekBar) {}

    private int getScreenBrightness()
    {
        int brightness = 0;
        try
        {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }

        return brightness;
    }
}
