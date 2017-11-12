package dev.panel.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Dialoger
{
    public static AlertDialog createAlertDialog
            (
                Context context,
                String title,
                String message,
                String positiveButtonText,
                DialogInterface.OnClickListener positiveButtonListener
            )
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(android.R.string.cancel, null);

        return dialogBuilder.create();
    }
}
