package dev.panel;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dev.panel.service.PanelService;
import dev.panel.utils.L;

public class MainActivity extends AppCompatActivity
{
    private static final int PERMISSION_WRITE_SETTINGS_REQUEST = 1001;
    private static final int PERMISSION_SYSTEM_ALERT_WINDOW_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    //TODO: Permissions not requesting
    private void checkPermissions()
    {
        requestWriteSettingsPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PERMISSION_WRITE_SETTINGS_REQUEST:
                requestSystemAlertWindowPermission();
                break;

            case PERMISSION_SYSTEM_ALERT_WINDOW_REQUEST:
                requestManifestPermissions();
                break;
        }
    }

    private void requestWriteSettingsPermission()
    {
        L.m("permissions", "requesting WRITE_SETTINGS");
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
                            startActivityForResult
                                    (writeSettings, PERMISSION_WRITE_SETTINGS_REQUEST);
                        }
                    })
                    .show();
            }
        }
    }

    private void requestSystemAlertWindowPermission()
    {
        L.m("permissions", "requesting SYSTEM_ALERT_WINDOW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.canDrawOverlays(this))
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
                            Intent systemAlertWindow = new Intent
                                    (Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            systemAlertWindow.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult
                                    (systemAlertWindow, PERMISSION_SYSTEM_ALERT_WINDOW_REQUEST);

                        }
                    })
                    .show();
            }
        }
    }

    private void requestManifestPermissions()
    {
        L.m("permissions", "requesting manifest permissions");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            String[] permissions = new String[]
                    {
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                    };
            ActivityCompat.requestPermissions(this, permissions, 1001);
        }
    }

//    private ServiceConnection serviceConnection = new ServiceConnection()
//    {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
//        {
//            PanelService.ServiceBinder binder = (PanelService.ServiceBinder) iBinder;
//            service = binder.getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {}
//    };

    public void startService(View view)
    {
        final EditText password = new EditText(this);
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setTransformationMethod(new PasswordTransformationMethod());

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Please Enter Password")
                .setCancelable(false)
                .setView(password)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(password.getText().toString().equals("sysalert"))
                {
                    Intent intent = new Intent(MainActivity.this, PanelService.class);
//                            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    startService(intent);
                    dialog.dismiss();
                }
                else
                {
                    password.setText("");
                    Toast.makeText
                            (MainActivity.this, "Wrong password", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}