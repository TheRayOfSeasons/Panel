package dev.panel;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dev.panel.service.PanelService;

public class MainActivity extends AppCompatActivity
{
    private PanelService service;

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
                                Intent writeSettings = new Intent
                                        (Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                writeSettings.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(writeSettings);
                            }
                        });
            }

            String[] permissions = new String[]
            {
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
            };
            ActivityCompat.requestPermissions(this, permissions, 1001);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            PanelService.ServiceBinder binder = (PanelService.ServiceBinder) iBinder;
            service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    public void startService(View view)
    {
        Intent intent = new Intent(this, PanelService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
}