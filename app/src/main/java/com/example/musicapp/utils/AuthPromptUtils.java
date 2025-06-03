package com.example.musicapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.musicapp.R;
import com.example.musicapp.ui.auth.AuthActivity;

public class AuthPromptUtils {
    public static void showLoginRequiredDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.text_title_login_required)
                .setMessage(R.string.text_login_required)
                .setPositiveButton(R.string.text_login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
