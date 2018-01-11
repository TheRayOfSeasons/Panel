package dev.panel.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.panel.R;
import dev.panel.utils.L;

@SuppressWarnings("RedundantCast")
public class TogglesFragment extends Fragment implements View.OnClickListener
{
    private WifiManager wifiManager;
    private boolean wifiIsEnabled;

    public TogglesFragment() {}

    @Nullable
    @Override
    public View onCreateView
        (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
        )
    {
        wifiManager = (WifiManager) getActivity()
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        wifiIsEnabled = wifiManager.isWifiEnabled();

        View view = inflater.inflate(R.layout.fragment_toggles, container, false);

        //TODO: Change button ID and object names
        ImageView flashlight = (ImageView) view.findViewById(R.id.toggle_flashlight);
        ImageView volumeModes = (ImageView) view.findViewById(R.id.toggle_volume_modes);
        ImageView wifi = (ImageView) view.findViewById(R.id.toggle_wifi);
        ImageView bluetooth = (ImageView) view.findViewById(R.id.toggle_bluetooth);
        ImageView rotation = (ImageView) view.findViewById(R.id.toggle_rotation);

        ImageView[] toggles = new ImageView[]
                { flashlight, volumeModes, wifi, bluetooth, rotation };
        for (ImageView toggle : toggles)
            toggle.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.toggle_flashlight:
                L.m("toggle", "flashlight");
                break;

            case R.id.toggle_volume_modes:
                L.m("toggle", "volume modes");
                break;

            case R.id.toggle_wifi:
                wifiIsEnabled = !wifiIsEnabled;
                L.m("toggle", wifiIsEnabled? "wifi enabled" : "wifi disabled");
                wifiManager.setWifiEnabled(wifiIsEnabled);
                break;

            case R.id.toggle_bluetooth:
                L.m("toggle", "bluetooth");
                break;

            case R.id.toggle_rotation:
                L.m("toggle", "rotation");
                break;
        }
    }
}
