package dev.panel;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dev.panel.service.PanelService;
import dev.panel.utils.Dialoger;
import dev.panel.utils.L;

public class MainActivity extends AppCompatActivity
{
    private static final int MANIFEST_PERMISSIONS_REQUEST = 1001;
    private static final int PERMISSION_WRITE_SETTINGS_REQUEST = 1002;
    private static final int PERMISSION_SYSTEM_ALERT_WINDOW_REQUEST = 1003;

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
        requestManifestPermissions();
    }

    private void requestWriteSettingsPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.System.canWrite(this))
            {
                L.m("permissions", "requesting WRITE_SETTINGS");

                Dialoger.createAlertDialog
                        (
                            this,
                            "Allow Modify Settings",
                            "Please allow the app to modify system settings.",
                            "Okay",
                            new DialogInterface.OnClickListener()
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
                            }
                        ).show();
            }
        }
    }

    private void requestSystemAlertWindowPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.canDrawOverlays(this))
            {
                L.m("permissions", "requesting SYSTEM_ALERT_WINDOW");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            L.m("permissions", "requesting manifest permissions");

            String[] permissions = new String[]
                {
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                };

            int[] permissionsCheck = new int[permissions.length];
            for(int i = 0; i < permissions.length; i++)
                permissionsCheck[i] = ContextCompat.checkSelfPermission(this, permissions[i]);

            boolean accessWifiState = permissionsCheck[0] == PackageManager.PERMISSION_GRANTED;
            boolean changeWifiState = permissionsCheck[1] == PackageManager.PERMISSION_GRANTED;

            if(!accessWifiState && !changeWifiState)
                ActivityCompat.requestPermissions(this, permissions, MANIFEST_PERMISSIONS_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PERMISSION_WRITE_SETTINGS_REQUEST:
                L.m("permissions", "WRITE_SETTINGS request finished");
                requestSystemAlertWindowPermission();
                break;

            case PERMISSION_SYSTEM_ALERT_WINDOW_REQUEST:
                L.m("permissions", "SYSTEM_ALERT_WINDOW request finished");
                requestManifestPermissions();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == MANIFEST_PERMISSIONS_REQUEST)
        {
            L.m("permissions", "manifest permissions request finished");
            if(grantResults.length > 0)
            {
                boolean accessWifiState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean changeWifiState = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(accessWifiState && changeWifiState)
                {
                    L.m("permissions", "manifest permissions granted");
                    requestWriteSettingsPermission();
                }
                else
                {
                    L.m("permissions", "manifest permissions denied")
                    ;new AlertDialog.Builder(this)
                        .setTitle("Permissions Required")
                        .setMessage("This needs some permissions to work properly. Please grant " +
                                "the permissions requested.")
                        .setCancelable(false)
                        .setPositiveButton("Okay",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        requestManifestPermissions();
                                    }
                                })
                        .show();
                }
            }
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

        final AlertDialog dialog = Dialoger.createAlertDialog
                (
                    this,
                    "(FOR DEV ONLY) Please Enter Password",
                    "",
                    "Confirm",
                    new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {}
                    }
                );

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(password.getText().toString().equals("sysalert"))
                {
                    dialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, PanelService.class);
//                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    startService(intent);
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