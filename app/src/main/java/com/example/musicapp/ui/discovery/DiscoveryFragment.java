package com.example.musicapp.ui.discovery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.databinding.FragmentDiscoveryBinding;
import com.example.musicapp.ui.searching.SearchingFragmentDirections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoveryFragment extends Fragment {
    private FragmentDiscoveryBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
    }

    private void setupView() {
        mBinding.btnDiscoverySearch.setOnClickListener(view -> {
            NavDirections directions = SearchingFragmentDirections.actionGlobalFrSearching();
            NavHostFragment.findNavController(this).navigate(directions);
        });
    }
}