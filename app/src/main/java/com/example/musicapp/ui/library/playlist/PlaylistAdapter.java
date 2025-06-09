package com.example.musicapp.ui.library.playlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.databinding.ItemPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private final List<Playlist> mPlaylists = new ArrayList<>();
    private final OnPlaylistItemClickListener mItemClickListener;
    private final OnPlaylistOptionMenuClickListener mMenuClickListener;

    public PlaylistAdapter(OnPlaylistItemClickListener itemClickListener,
                           OnPlaylistOptionMenuClickListener menuClickListener) {
        mItemClickListener = itemClickListener;
        mMenuClickListener = menuClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding, mItemClickListener, mMenuClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mPlaylists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlaylists.size();
    }

    public void updatePlaylists(List<Playlist> newPlaylists) {
        if (newPlaylists != null) {
            int oldSize = mPlaylists.size();
            mPlaylists.clear();
            mPlaylists.addAll(newPlaylists);
            notifyDataSetChanged();
        }
    }

    public void addPlaylist(Playlist playlist) {
        mPlaylists.add(playlist);
        notifyItemInserted(mPlaylists.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaylistBinding mBinding;
        private final OnPlaylistItemClickListener mItemClickListener;
        private final OnPlaylistOptionMenuClickListener mMenuClickListener;

        public ViewHolder(@NonNull ItemPlaylistBinding binding,
                          OnPlaylistItemClickListener itemClickListener,
                          OnPlaylistOptionMenuClickListener menuClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            mItemClickListener = itemClickListener;
            mMenuClickListener = menuClickListener;
        }

        public void bind(Playlist playlist) {
            mBinding.textItemPlaylistName.setText(playlist.getName());
            mBinding.textItemPlaylistCount.setText(
                    mBinding.getRoot().getContext().getString(
                            R.string.text_songs,
                            playlist.getSongs() != null ? playlist.getSongs().size() : 0
                    )
            );
            Glide.with(mBinding.getRoot().getContext())
                    .load(playlist.getSongs() != null && !playlist.getSongs().isEmpty() ?
                            playlist.getSongs().get(0).getImageUrl() : R.drawable.ic_music_note)
                    .error(R.drawable.ic_music_note)
                    .into(mBinding.imgItemPlaylistAvatar);

            mBinding.getRoot().setOnClickListener(v -> mItemClickListener.onClick(playlist));
            mBinding.btnItemPlaylistOption
                    .setOnClickListener(v -> mMenuClickListener.onClick(playlist));
        }
    }

    public interface OnPlaylistItemClickListener {
        void onClick(Playlist playlist);
    }

    public interface OnPlaylistOptionMenuClickListener {
        void onClick(Playlist playlist);
    }
}
