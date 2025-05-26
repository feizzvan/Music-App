package com.example.musicapp.ui.searching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.databinding.FragmentHistorySearchedKeyBinding;
import com.example.musicapp.databinding.ItemSearchedKeyBinding;
import com.example.musicapp.ui.dialog.ConfirmationDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HistorySearchedKeyFragment extends Fragment {
    private FragmentHistorySearchedKeyBinding mBinding;
    private SearchingViewModel mSearchingViewModel;
    private HistorySearchedKeyAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    SearchingViewModel.Factory mSearchingFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHistorySearchedKeyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
        setupViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupView() {
        mAdapter = new HistorySearchedKeyAdapter(key -> mSearchingViewModel.setSelectedKey(key));
        mBinding.rvHistorySearchedKey.setAdapter(mAdapter);
        mBinding.actionClearAllHistorySearchedKey.setOnClickListener(v -> {
            int messageId = R.string.message_confirm_clear_history_searched_key;
            ConfirmationDialogFragment dialog = new ConfirmationDialogFragment(messageId, isConfirmed -> {
                if (isConfirmed) {
                    mDisposable.add(mSearchingViewModel.clearAllKeys()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());
                }
            });

            dialog.show(requireActivity().getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
        });
    }

    private void setupViewModel() {
        mSearchingViewModel = new ViewModelProvider(requireActivity(), mSearchingFactory)
                .get(SearchingViewModel.class);
        mDisposable.add(mSearchingViewModel.getAllKeys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keys -> {
                    if (keys != null) {
                        mAdapter.updateKeys(keys);
                    }
                })
        );
    }

    static class HistorySearchedKeyAdapter extends RecyclerView.Adapter<HistorySearchedKeyAdapter.ViewHolder> {
        private final onItemClickListener mListener;
        private final List<HistorySearchedKey> mKeys = new ArrayList<>();

        public HistorySearchedKeyAdapter(onItemClickListener listener) {
            mListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemSearchedKeyBinding binding = ItemSearchedKeyBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding, mListener);
        }

        @Override
        public void onBindViewHolder(@NonNull HistorySearchedKeyAdapter.ViewHolder holder, int position) {
            holder.bind(mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mKeys.size();
        }

        public void updateKeys(List<HistorySearchedKey> keys) {
            int oldSize = mKeys.size();
            mKeys.clear();
            notifyItemRangeRemoved(0, oldSize);
            mKeys.addAll(keys);
            notifyItemRangeInserted(0, keys.size());
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemSearchedKeyBinding mBinding;
            private final onItemClickListener mListener;

            public ViewHolder(@NonNull ItemSearchedKeyBinding binding, onItemClickListener listener) {
                super(binding.getRoot());
                mBinding = binding;
                mListener = listener;
            }

            public void bind(HistorySearchedKey key) {
                mBinding.textSearchedKey.setText(key.getKey());
                mBinding.getRoot().setOnClickListener(v -> mListener.onClick(key.getKey()));
            }
        }
    }

    interface onItemClickListener {
        void onClick(String key);
    }
}