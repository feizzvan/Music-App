package com.example.musicapp.ui.dialog.optionmenu;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.databinding.ItemOptionMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class OptionMenuAdapter extends RecyclerView.Adapter<OptionMenuAdapter.ViewHolder> {

    private final List<OptionMenuItem> mOptionMenuItems = new ArrayList<>();
    private final OnMenuOptionItemClickListener mListener;

    public OptionMenuAdapter(OnMenuOptionItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public OptionMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemOptionMenuBinding binding = ItemOptionMenuBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionMenuAdapter.ViewHolder holder, int position) {
        holder.bind(mOptionMenuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mOptionMenuItems.size();
    }

    public void updateMenuOptionItems(List<OptionMenuItem> optionMenuItems) {
        mOptionMenuItems.clear();
        mOptionMenuItems.addAll(optionMenuItems);
        notifyItemRangeChanged(0, mOptionMenuItems.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOptionMenuBinding mBinding;
        private final OnMenuOptionItemClickListener mListener;

        public ViewHolder(ItemOptionMenuBinding binding, OnMenuOptionItemClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bind(OptionMenuItem optionMenuItem) {
            mBinding.textMenuOptionItemTitle.setText(optionMenuItem.getMenuItemTitle());
            Glide.with(mBinding.getRoot())
                    .load(optionMenuItem.getIconId())
                    .into(mBinding.imgMenuOptionItemIcon);
            mBinding.getRoot()
                    .setOnClickListener(view -> mListener.onMenuOptionItemClick(optionMenuItem));
        }
    }

    public interface OnMenuOptionItemClickListener {
        void onMenuOptionItemClick(OptionMenuItem optionMenuItem);
    }
}
