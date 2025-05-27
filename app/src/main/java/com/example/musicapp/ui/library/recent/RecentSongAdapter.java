package com.example.musicapp.ui.library.recent;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.databinding.ItemSongBinding;
import com.example.musicapp.ui.SongListAdapter;
import com.example.musicapp.utils.AppUtils;
import com.example.musicapp.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class RecentSongAdapter extends RecyclerView.Adapter<RecentSongAdapter.ViewHolder> {
    private final List<Song> mSongs = new ArrayList<>();
    private final SongListAdapter.OnSongItemClickListener mSongItemClickListener;
    private final SongListAdapter.OnSongItemMenuClickListener mSongItemMenuClickListener;

    public RecentSongAdapter(SongListAdapter.OnSongItemClickListener mSongItemClickListener, SongListAdapter.OnSongItemMenuClickListener mSongItemMenuClickListener) {
        this.mSongItemClickListener = mSongItemClickListener;
        this.mSongItemMenuClickListener = mSongItemMenuClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSongBinding binding = ItemSongBinding.inflate(layoutInflater, parent, false);

        // Tuỳ chỉnh kích thước của layout hiển thị bài hát
        ConstraintLayout constraintLayout = binding.getRoot();
        ViewTreeObserver vto = constraintLayout.getViewTreeObserver(); //Cung cấp cơ chế để theo dõi sự thay đổi của layout
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { //Lắng nghe sự kiện khi layout đã vẽ xong
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this); //Xoá lắng nghe sau lần đầu thực thi
                int width = constraintLayout.getMeasuredWidth();
                int height = constraintLayout.getMeasuredHeight();
                //Chuyển đổi từ dpi sang px bằng cách nhân với tỉ lệ dpi
                int deltaX = (int) (48 * AppUtils.X_DPI / 160); //1 dp = 1 pixel trên màn hình có mật độ 160 dpi
                binding.layoutItemSong.getLayoutParams().width = width - deltaX;
                binding.layoutItemSong.getLayoutParams().height = height;
            }
        });

        return new ViewHolder(binding, mSongItemClickListener, mSongItemMenuClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSongAdapter.ViewHolder holder, int position) {
        holder.bind(mSongs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding mBinding;
        private final SongListAdapter.OnSongItemClickListener mSongItemClickListener;
        private final SongListAdapter.OnSongItemMenuClickListener mSongItemMenuClickListener;

        public ViewHolder(ItemSongBinding binding,
                          SongListAdapter.OnSongItemClickListener songItemClickListener,
                          SongListAdapter.OnSongItemMenuClickListener songItemMenuClickListener) {
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
                Boolean isGranted = PermissionUtils.getPermissionGranted().getValue();
                if (isGranted == null || !isGranted) {
                    PermissionUtils.setPermissionAsked(true);
                }
                mSongItemClickListener.onSongItemClick(song, position);
            });

            mBinding.getRoot().setOnClickListener(view -> mSongItemClickListener.onSongItemClick(song, position));

            mBinding.btnItemSongOption
                    .setOnClickListener(
                            view -> mSongItemMenuClickListener.onSongMenuItemClick(song));
        }
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
}
