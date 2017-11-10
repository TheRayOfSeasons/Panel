package dev.panel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.panel.R;

public class ButtonCaseFragment extends Fragment
{
    public ButtonCaseFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View fragment = inflater.inflate(R.layout.fragment_buttoncase, container, false);

        return fragment;
    }
}
