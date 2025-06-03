package com.example.musicapp.ui;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.ItemSongBinding;
import com.example.musicapp.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private final List<Song> mSongs = new ArrayList<>();
    private final OnSongItemClickListener mSongItemClickListener;
    private final OnSongItemMenuClickListener mSongItemMenuClickListener;

    public SongListAdapter(OnSongItemClickListener songItemClickListener,
                           OnSongItemMenuClickListener songItemMenuClickListener) {
        this.mSongItemClickListener = songItemClickListener;
        this.mSongItemMenuClickListener = songItemMenuClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSongBinding binding = ItemSongBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mSongItemClickListener, mSongItemMenuClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.ViewHolder holder, int position) {
        holder.bind(mSongs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void updateSongs(List<Song> songs) {
        if (songs != null) {
            int oldSize = mSongs.size();
            mSongs.clear();
            mSongs.addAll(songs);
            if (oldSize > mSongs.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeChanged(0, mSongs.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding mBinding;
        private final OnSongItemClickListener mSongItemClickListener;
        private final OnSongItemMenuClickListener mSongItemMenuClickListener;

        public ViewHolder(ItemSongBinding binding,
                          OnSongItemClickListener songItemClickListener,
                          OnSongItemMenuClickListener songItemMenuClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.mSongItemClickListener = songItemClickListener;
            this.mSongItemMenuClickListener = songItemMenuClickListener;
        }

        public void bind(Song song, int position) {
            mBinding.textItemSongTitle.setText(song.getTitle());
            mBinding.textItemSongArtist.setText(song.getArtistName());
            Glide.with(mBinding.getRoot().getContext())
                    .load(song.getImageUrl())
                    .error(R.drawable.ic_music_note)
                    .into(mBinding.imgItemSongAvatar);

            mBinding.getRoot().setOnClickListener(view -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Boolean isGranted = PermissionUtils.getPermissionGranted().getValue();
                    if (isGranted == null || !isGranted) {
                        PermissionUtils.setPermissionAsked(true);
                    }
                }
                mSongItemClickListener.onSongItemClick(song, position);
            });

            mBinding.btnItemSongOption.setOnClickListener(
                    view -> mSongItemMenuClickListener.onSongMenuItemClick(song));
        }
    }

    public interface OnSongItemClickListener {
        void onSongItemClick(Song song, int position);
    }

    public interface OnSongItemMenuClickListener {
        void onSongMenuItemClick(Song song);
    }
}
