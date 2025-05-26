package com.example.musicapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.R;
import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.ForgotPasswordRequest;
import com.example.musicapp.data.source.remote.AppService;
import com.example.musicapp.data.source.remote.RetrofitHelper;
import com.example.musicapp.databinding.FragmentForgotPasswordBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding mBinding;
    private final AppService appService = RetrofitHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
    }

    private void setupView() {
        mBinding.btnBack.setOnClickListener(view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mBinding.actionBackToLogin.setOnClickListener(view -> navigateToLogin());
        mBinding.btnResetPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void navigateToLogin() {
        NavDirections directions = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment();
        NavHostFragment.findNavController(ForgotPasswordFragment.this).navigate(directions);
    }

    private void handleForgotPassword() {
        String email = Objects.requireNonNull(mBinding.edtEmail.getText()).toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), R.string.text_please_enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
//        showProgressBar(true);

        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        appService.forgotPassword(request).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthenticationResponse> call, @NonNull Response<AuthenticationResponse> response) {
//                showProgressBar(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), R.string.text_check_email_reset_password, Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(getContext(), R.string.text_email_does_not_exist_or_is_not_registered, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthenticationResponse> call, @NonNull Throwable t) {
//                showProgressBar(false);
                Toast.makeText(getContext(), "Lỗi mạng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}