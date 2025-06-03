package com.example.musicapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.ui.dialog.SongOptionMenuDialogFragment;
import com.example.musicapp.ui.dialog.optionmenu.OptionMenuViewModel;
import com.example.musicapp.ui.library.playlist.PlaylistViewModel;
import com.example.musicapp.ui.playing.NowPlayingActivity;
import com.example.musicapp.ui.playing.NowPlayingViewModel;
import com.example.musicapp.utils.PermissionUtils;
import com.example.musicapp.utils.SharedDataUtils;
import com.example.musicapp.utils.TokenManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AppBaseFragment extends Fragment {
    private NowPlayingViewModel mNowPlayingViewModel;
    private PlaylistViewModel mPlaylistViewModel;

    @Inject
    public TokenManager tokenManager;

    @Inject
    public NowPlayingViewModel.Factory nowPlayingFactory;

    @Inject
    public PlaylistViewModel.Factory playlistFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNowPlayingViewModel = new ViewModelProvider(requireActivity(), nowPlayingFactory).get(NowPlayingViewModel.class);
        mPlaylistViewModel = new ViewModelProvider(requireActivity(), playlistFactory).get(PlaylistViewModel.class);
    }

    protected void showAndPlay(Song song, int index, String playlistName) {
        int userId = tokenManager.getUserId();
        int songId = song.getId();

        mNowPlayingViewModel.increaseListeningCount(userId, songId);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            doNavigate(index, playlistName);
        } else {
            Boolean isPermissionGranted = PermissionUtils.getPermissionGranted().getValue();
            if (isPermissionGranted != null && isPermissionGranted) {
                doNavigate(index, playlistName);
            } else if (!PermissionUtils.isRegistered) {
                PermissionUtils.getPermissionGranted().observe(requireActivity(), isGranted -> {
                    if (isGranted) {
                        doNavigate(index, playlistName);
                    }
                });
                PermissionUtils.isRegistered = true;
            }
        }
    }

    protected void showOptionMenu(Song song) {
        OptionMenuViewModel optionMenuViewModel =
                new ViewModelProvider(requireActivity()).get(OptionMenuViewModel.class);
        optionMenuViewModel.setSong(song);
        SongOptionMenuDialogFragment dialog = SongOptionMenuDialogFragment.newInstance();
        dialog.show(requireActivity().getSupportFragmentManager(), SongOptionMenuDialogFragment.TAG);
    }

    private void doNavigate(int index, String playlistName) {
        SharedDataUtils.setCurrentPlaylist(playlistName);
        SharedDataUtils.setIndexToPlay(index);

        Intent intent = new Intent(requireContext(), NowPlayingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeCustomAnimation(requireContext(), R.anim.slide_up, R.anim.fade_out);
        requireContext().startActivity(intent, options.toBundle());
    }

    protected void showPlaylistOptionMenu(Playlist playlist) {
        String[] options = {getString(R.string.action_delete_playlist),
                getString(R.string.action_rename_playlist)};
        new AlertDialog.Builder(requireContext())
                .setTitle(playlist.getName())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) { // Xóa Playlist
                        showDeletePlaylistDialog(requireContext(), playlist);
                    } else if (which == 1) { // Đổi tên Playlist
                        showRenamePlaylistDialog(requireContext(), playlist);
                    }
                })
                .show();
    }

    private void showDeletePlaylistDialog(Context context, Playlist playlist) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_confirm_delete)
                .setMessage(R.string.text_are_you_sure_delete_playlist)
                .setPositiveButton(R.string.action_delete, (d, w) -> {
                    mPlaylistViewModel.deletePlaylist(playlist.getId());
                    Toast.makeText(context, R.string.text_delete_playlist_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showRenamePlaylistDialog(Context context, Playlist playlist) {
        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(playlist.getName());
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_rename_playlist)
                .setView(editText)
                .setPositiveButton(R.string.action_save, (d, w) -> {
                    String newName = editText.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        mPlaylistViewModel.renamePlaylist(playlist.getId(), newName);
                        Toast.makeText(context, R.string.text_rename_playlist_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.text_name_invalid, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }
}
