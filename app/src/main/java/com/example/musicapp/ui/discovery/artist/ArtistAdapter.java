package com.example.musicapp.ui.discovery.artist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.artist.Artist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.ItemArtistBinding;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private final OnArtistClickListener mListener;
    private final List<Artist> mArtist = new ArrayList<>();
    private final List<Song> mSongs = new ArrayList<>();

    public ArtistAdapter(OnArtistClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemArtistBinding binding = ItemArtistBinding.inflate(inflater, parent, false);
        return new ArtistViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = mArtist.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        return mArtist.size();
    }

    public void updateArtist(List<Artist> artists) {
        int oldSize = mArtist.size();
        mArtist.clear();
        mArtist.addAll(artists);
        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, mArtist.size());
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final ItemArtistBinding mBinding;
        private final OnArtistClickListener mListener;

        public ArtistViewHolder(@NonNull ItemArtistBinding binding, OnArtistClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bind(@NonNull Artist artist) {
            Glide.with(mBinding.getRoot())
                    .load(artist.getAvatar())
                    .error(R.drawable.ic_view_artist)
                    .circleCrop()
                    .into(mBinding.imgItemArtistAvatar);
            mBinding.textItemArtistName.setText(artist.getName());
            mBinding.textItemArtistSong.setText(mBinding.getRoot().getContext().
                    getString(R.string.text_number_song, artist.getSongCount()));
            mBinding.getRoot().setOnClickListener(view -> mListener.onClick(artist));
        }
    }

    public interface OnArtistClickListener {
        void onClick(Artist artist);
    }
}
