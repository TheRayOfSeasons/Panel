package dev.panel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dev.panel.R;
import dev.panel.utils.L;

@SuppressWarnings("RedundantCast")
public class ButtonCaseFragment extends Fragment implements View.OnClickListener
{
    public ButtonCaseFragment() {}

    @Nullable
    @Override
    public View onCreateView
        (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
        )
    {
        View view = inflater.inflate(R.layout.fragment_buttoncase, container, false);

        //TODO: Change button ID and object names
        Button button1 = (Button) view.findViewById(R.id.button_1);
        Button button2 = (Button) view.findViewById(R.id.button_2);
        Button button3 = (Button) view.findViewById(R.id.button_3);
        Button button4 = (Button) view.findViewById(R.id.button_4);
        Button button5 = (Button) view.findViewById(R.id.button_5);

        Button[] buttons = new Button[] { button1, button2, button3, button4, button5 };
        for (Button button: buttons)
            button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_1:
                L.m("Button", "1");
                break;

            case R.id.button_2:
                L.m("Button", "2");
                break;

            case R.id.button_3:
                L.m("Button", "3");
                break;

            case R.id.button_4:
                L.m("Button", "4");
                break;

            case R.id.button_5:
                L.m("Button", "5");
                break;
        }
    }
}
