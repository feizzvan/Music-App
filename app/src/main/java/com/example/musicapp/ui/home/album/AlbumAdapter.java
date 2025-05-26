package com.example.musicapp.ui.home.album;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.databinding.ItemAlbumBinding;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private final List<Album> mAlbums = new ArrayList<>();
    private final AlbumClickListener mListener;

    public AlbumAdapter(AlbumClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAlbumBinding binding = ItemAlbumBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        holder.bind(mAlbums.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public void updateAlbums(List<Album> albums) {
        if (albums != null) {
            int oldSize = mAlbums.size();
            mAlbums.clear();
            mAlbums.addAll(albums);
            if (oldSize > mAlbums.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeInserted(0, mAlbums.size());
        }
    }

    //ViewHolder để quản lý từng item Albums
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumBinding mBinding;
        private final AlbumClickListener mListener;

        public ViewHolder(ItemAlbumBinding binding, AlbumClickListener listener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.mListener = listener;
        }

        public void bind(Album album) {
            Glide.with(itemView.getContext())
                    .load(album.getCoverImageUrl())
                    .error(R.drawable.ic_music_note)
                    .into(mBinding.imgItemAlbumAvatar);
            mBinding.textItemAlumTitle.setText(album.getTitle());
            mBinding.getRoot().setOnClickListener(v -> mListener.onAlbumClick(album));
        }
    }

    public interface AlbumClickListener {
        void onAlbumClick(Album album);
    }
}
