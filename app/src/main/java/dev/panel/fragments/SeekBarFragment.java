package dev.panel.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import dev.panel.R;
import dev.panel.utils.L;

@SuppressWarnings("RedundantCast")
    public class SeekBarFragment extends Fragment implements SeekBar.OnSeekBarChangeListener
    {
        private ContentResolver contentResolver;
        private AudioManager systemAudio = null;
        private AudioManager mediaAudio = null;

        public SeekBarFragment() {}

        @Nullable
        @Override
        public View onCreateView
            (
                LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState
            )
        {
            contentResolver = getContext().getContentResolver();
            systemAudio = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            mediaAudio = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

            View view = inflater.inflate(R.layout.fragment_seekbars, container, false);

            //TODO: Update when brightness is changed by other apps
            SeekBar brightnessBar =  (SeekBar) view.findViewById(R.id.brightness_bar);
            brightnessBar.setMax(255);
            brightnessBar.setProgress(getScreenBrightness());
            brightnessBar.setOnSeekBarChangeListener(this);

            SeekBar systemVolumeBar = (SeekBar) view.findViewById(R.id.system_volume_bar);
            systemVolumeBar.setMax(systemAudio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
            systemVolumeBar.setProgress(systemAudio.getStreamVolume(AudioManager.STREAM_SYSTEM));
            systemVolumeBar.setOnSeekBarChangeListener(this);

            SeekBar mediaVolumeBar = (SeekBar) view.findViewById(R.id.media_volume_bar);
            mediaVolumeBar.setMax(mediaAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            mediaVolumeBar.setProgress(mediaAudio.getStreamVolume(AudioManager.STREAM_MUSIC));
            mediaVolumeBar.setOnSeekBarChangeListener(this);

            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
        }

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
                    systemAudio.setStreamVolume(AudioManager.STREAM_SYSTEM, i, 0);
                    break;

                case R.id.media_volume_bar:
                    mediaAudio.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
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
