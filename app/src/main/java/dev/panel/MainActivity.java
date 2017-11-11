package dev.panel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    /*









            PA THE BEST








    */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    private void checkPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.System.canWrite(this))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Allow Modify Settings")
                        .setMessage("Please allow the app to modify system settings.")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Intent writeSettings = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                writeSettings.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(writeSettings);
                            }
                        });
            }
        }
    }
}