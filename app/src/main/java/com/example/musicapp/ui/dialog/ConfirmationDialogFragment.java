package com.example.musicapp.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.musicapp.R;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String TAG = "ConfirmationDialogFragment";
    private final int mMessageId;
    private final OnDeleteConfirmListener mListener;

    public ConfirmationDialogFragment(int messageId, OnDeleteConfirmListener listener) {
        mMessageId = messageId;
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setIcon(R.drawable.ic_warning);
        String title = getString(R.string.confirm_delete_title);
        builder.setTitle(title);
        builder.setMessage(mMessageId);
        builder.setPositiveButton(R.string.text_yes, (dialog, which) ->
                mListener.onConfirm(true));
        builder.setNegativeButton(R.string.text_no, (dialog, which) ->
                mListener.onConfirm(false));
        return builder.create();
    }

    public interface OnDeleteConfirmListener {
        void onConfirm(boolean isConfirmed);
    }
}
