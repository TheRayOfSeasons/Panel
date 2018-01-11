package dev.panel.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import dev.panel.R;
import dev.panel.utils.L;

public class PanelService extends Service implements View.OnTouchListener
{
    private View panel;
    //    private IBinder binder = new ServiceBinder();
//
//    public class ServiceBinder extends Binder
//    {
//        public PanelService getService()
//        {
//            return PanelService.this;
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        L.m("service", "started");
        addFloatingView();
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBinder onBind(Intent intent)
    {
//        L.m("service", "started and bound");
//        return binder;
        return null;
    }

    @Override
    public void onDestroy()
    {
        L.m("service", "destroyed");
        super.onDestroy();
    }

    //TODO: Handle panel exceeding given height (300dp)

    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n,InflateParams")
    private void addFloatingView()
    {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        @SuppressLint("InlinedApi")
        int typeLayoutParam = (android.os.Build.VERSION.SDK_INT >= 26)?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
            WindowManager.LayoutParams.TYPE_PHONE;
        float heightDP = TypedValue.applyDimension
            (TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams
            (
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) heightDP,
                typeLayoutParam,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            );
        layoutParams.gravity = Gravity.BOTTOM;

        LayoutInflater inflater =
            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        panel = inflater.inflate(R.layout.test, null, false);
        panel.setOnTouchListener(this);
        panel.setTranslationY(heightDP - (heightDP/8));
        windowManager.addView(panel, layoutParams);

//        Button button = new Button(this);
//        button.setText("Service");
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Toast.makeText(PanelService.this, "REEEEEEEEEE", Toast.LENGTH_SHORT).show();
//            }
//        });
//        windowManager.addView(button, layoutParams);

        L.m("service", "added floating view");
    }

    private float offset, y, downY, lastMoveY;
    private boolean opened;

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
//                offset = event.getY();
                downY = view.getY() - event.getRawY();
//                y = downY + offset;
                lastMoveY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                opened = event.getRawY() < lastMoveY;
                y = event.getRawY() + downY;
                moveSlidingLayout(y);
                L.m("Action", "Move");
                lastMoveY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if(downY != event.getRawY())
                {
                    if(opened) L.m("Action", "Open");
                    else L.m("Action", "Close");
                }

                break;
        }
        return true;
    }

    public void moveSlidingLayout(float amount)
    {
//        if(amount < 0)
//            amount = 0;

        panel.animate()
            .y(amount)
            .setDuration(0)
            .start();


    }
}
