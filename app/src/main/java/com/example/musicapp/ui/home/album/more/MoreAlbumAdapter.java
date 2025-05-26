package com.example.musicapp.ui.home.album.more;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.databinding.ItemMoreAlbumBinding;
import com.example.musicapp.ui.home.album.AlbumAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoreAlbumAdapter extends RecyclerView.Adapter<MoreAlbumAdapter.ViewHolder> {

    private final List<Album> mAlbums = new ArrayList<>();
    private final AlbumAdapter.AlbumClickListener mListener;

    public MoreAlbumAdapter(AlbumAdapter.AlbumClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MoreAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoreAlbumBinding binding =
                ItemMoreAlbumBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreAlbumAdapter.ViewHolder holder, int position) {
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
            notifyItemRangeChanged(0, mAlbums.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMoreAlbumBinding mBinding;
        public AlbumAdapter.AlbumClickListener albumClickListener;

        public ViewHolder(@NonNull ItemMoreAlbumBinding binding,
                          AlbumAdapter.AlbumClickListener listener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.albumClickListener = listener;
        }

        public void bind(Album album) {
            mBinding.textItemAlbum.setText(album.getTitle());
            Glide.with(itemView.getContext())
                    .load(album.getCoverImageUrl())
                    .error(R.drawable.ic_album)
                    .into(mBinding.imgItemAlbumDetail);
            mBinding.getRoot().setOnClickListener(v -> albumClickListener.onAlbumClick(album));
        }
    }
}
