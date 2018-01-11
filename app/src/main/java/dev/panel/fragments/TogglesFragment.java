package dev.panel.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.panel.R;
import dev.panel.utils.L;
import dev.panel.utils.Toggles;

@SuppressWarnings("RedundantCast")
public class TogglesFragment extends Fragment implements View.OnClickListener
{
    Toggles toggles;

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
        toggles = new Toggles (getContext());
        View view = inflater.inflate(R.layout.fragment_toggles, container, false);

        //TODO: Change button ID and object names
        ImageView flashlight = (ImageView) view.findViewById(R.id.toggle_flashlight);
        ImageView volumeModes = (ImageView) view.findViewById(R.id.toggle_volume_modes);
        ImageView wifi = (ImageView) view.findViewById(R.id.toggle_wifi);
        ImageView bluetooth = (ImageView) view.findViewById(R.id.toggle_bluetooth);
        ImageView rotation = (ImageView) view.findViewById(R.id.toggle_rotation);

        ImageView[] tog = new ImageView[]
                { flashlight, volumeModes, wifi, bluetooth, rotation };
        for (ImageView toggle : tog)
            toggle.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.toggle_flashlight:
                toggles.flashlight();
                break;

            case R.id.toggle_volume_modes:
                toggles.volumeModes();
                break;

            case R.id.toggle_wifi:
                toggles.wifi();
                break;

            case R.id.toggle_bluetooth:
                toggles.bluetooth();
                break;

            case R.id.toggle_rotation:
                toggles.rotation();
                break;
        }
    }
}
