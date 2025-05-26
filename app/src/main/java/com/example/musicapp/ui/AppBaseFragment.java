package com.example.musicapp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.ui.dialog.SongOptionMenuDialogFragment;
import com.example.musicapp.ui.dialog.optionmenu.OptionMenuViewModel;
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

    @Inject
    public TokenManager tokenManager;

    @Inject
    public NowPlayingViewModel.Factory nowPlayingFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNowPlayingViewModel = new ViewModelProvider(requireActivity(), nowPlayingFactory).get(NowPlayingViewModel.class);
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
}
