package dev.panel.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import dev.panel.utils.L;

public class PanelService extends Service
{
    private IBinder binder = new ServiceBinder();

    public class ServiceBinder extends Binder
    {
        public PanelService getService()
        {
            return PanelService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        addFloatingView();
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBinder onBind(Intent intent)
    {
        L.m("service", "started and bound");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        L.m("service", "unbound");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy()
    {
        L.m("service", "destroyed");
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n")
    private void addFloatingView()
    {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams
                (
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        PixelFormat.TRANSLUCENT
                );

        Button button = new Button(this);
        button.setText("Service");
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PanelService.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                stopSelf();
                return true;
            }
        });

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(button, layoutParams);
    }
}