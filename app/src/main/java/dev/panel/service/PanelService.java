package dev.panel.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

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
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBinder onBind(Intent intent)
    {
        L.m("service", "started and bound");

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

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(new Button(this), layoutParams);

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
}
