package com.example.musicapp.ui.dialog.playlist;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.musicapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class PlaylistCreationDialog extends DialogFragment {
    public static final String TAG = "PlaylistCreationDialog";
    private final PlaylistDialogListener mListener;

    public PlaylistCreationDialog(PlaylistDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View rootView = layoutInflater.inflate(R.layout.dialog_playlist_creation, null);
        TextInputEditText editText = rootView.findViewById(R.id.edit_playlist_name);

        builder.setView(rootView)
                .setTitle(getString(R.string.title_create_playlist))
                .setPositiveButton(getString(R.string.action_create), (dialogInterface, id) -> {
                    if (editText != null && editText.getText() != null) {
                        String playlistName = editText.getText().toString().trim();
                        if (!playlistName.isEmpty()) {
                            mListener.onCreateDialog(playlistName);
                        } else {
                            String message = getString(R.string.error_empty_playlist_name);
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), (dialog, id) -> dismiss());

        // Ng dùng click vào ô thì sẽ không thay đổi vị trí hoặc kích thước của cửa sổ
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                requireActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_NOTHING);
            }
        });
        return builder.create();
    }

    // Truyền tên playlist từ dialog cho về activity hoặc fragment gọi dialog này
    public interface PlaylistDialogListener {
        void onCreateDialog(String playlistName);
    }
}
