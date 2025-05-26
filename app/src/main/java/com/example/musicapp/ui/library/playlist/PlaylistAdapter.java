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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final List<Playlist> playlists = new ArrayList<>();
    private final OnPlaylistClickListener onPlaylistClickListener;
    private final OnPlaylistOptionMenuClickListener onPlaylistOptionMenuClickListener;

    public PlaylistAdapter(OnPlaylistClickListener clickListener,
                           OnPlaylistOptionMenuClickListener optionMenuClickListener) {
        this.onPlaylistClickListener = clickListener;
        this.onPlaylistOptionMenuClickListener = optionMenuClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void updatePlaylists(List<Playlist> newPlaylists) {
        if (newPlaylists != null) {
            int oldSize = playlists.size();
            playlists.clear();
            playlists.addAll(newPlaylists);
            if (oldSize > playlists.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeChanged(0, playlists.size());
        }
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaylistBinding binding;
//        private final onPlaylistItemClickListener mItemClickListener;
//        private final onPlaylistOptionMenuClickListener mOptionMenuClickListener;

        PlaylistViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            mItemClickListener = itemClickListener;
//            mOptionMenuClickListener = optionMenuClickListener;
        }

        public void bind(Playlist playlist) {
            binding.textItemPlaylistName.setText(playlist.getName());
            binding.textItemPlaylistCount.setText(
                    binding.getRoot().getContext().getString(
                            R.string.text_songs,
                            playlist.getSongs() != null ? playlist.getSongs().size() : 0
                    )
            );
            Glide.with(binding.getRoot().getContext())
                    .load(playlist.getSongs() != null && !playlist.getSongs().isEmpty() ?
                            playlist.getSongs().get(0).getImageUrl() : R.drawable.ic_music_note)
                    .error(R.drawable.ic_music_note)
                    .into(binding.imgItemPlaylistAvatar);

            binding.getRoot().setOnClickListener(v -> onPlaylistClickListener.onPlaylistClick(playlist));
            binding.btnItemPlaylistOption.setOnClickListener(v -> onPlaylistOptionMenuClickListener.onPlaylistOptionMenuClick(playlist));
        }

    }

    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }

    public interface OnPlaylistOptionMenuClickListener {
        void onPlaylistOptionMenuClick(Playlist playlist);
    }
}
