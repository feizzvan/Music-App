package com.example.musicapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.data.model.auth.AuthenticationResponse;
import com.example.musicapp.data.model.auth.LoginRequest;
import com.example.musicapp.data.source.remote.AppService;
import com.example.musicapp.data.source.remote.RetrofitHelper;
import com.example.musicapp.databinding.FragmentLoginBinding;
import com.example.musicapp.utils.TokenManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding mBinding;

    private final AppService appService = RetrofitHelper.getInstance();

    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
    }

    private void setupView() {
        showProgressBar(false);
        mBinding.btnLogin.setOnClickListener(view -> handleLogin());
        mBinding.actionMoveSignUp.setOnClickListener(view -> navigateToRegister());
        mBinding.actionForgotPassword.setOnClickListener(view -> navigateToForgotPassword());
    }

    public void handleLogin() {
        String email = Objects.requireNonNull(mBinding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mBinding.edtPassword.getText()).toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), R.string.text_please_fill_in_all_information, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), R.string.text_invalid_email_format, Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressBar(true);

        LoginRequest request = new LoginRequest(email, password);

        appService.login(request).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthenticationResponse> call,
                                   @NonNull Response<AuthenticationResponse> response) {
                if (!isAdded()) return;

                showProgressBar(false);

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();

                    // Lưu token vào SharedPreferences
                    SharedPreferences preferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
                    preferences.edit().putString("access_token", token).apply();
                    preferences.edit().putInt("user_id", response.body().getUserId()).apply();

                    TokenManager tokenManager = new TokenManager(requireContext());
                    tokenManager.saveUserId(response.body().getUserId());

                    Toast.makeText(requireContext(), R.string.text_login_successful, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();

                } else {
                    Toast.makeText(requireContext(), R.string.text_please_check_your_login_information_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthenticationResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showProgressBar(false);
                Toast.makeText(requireContext(), R.string.text_network_error + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar(boolean show) {
        mBinding.progressLogin.setVisibility(show ? View.VISIBLE : View.GONE);
        mBinding.btnLogin.setEnabled(!show);
        mBinding.edtEmail.setEnabled(!show);
        mBinding.edtPassword.setEnabled(!show);
    }

    private void navigateToRegister() {
        NavDirections directions = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
        NavHostFragment.findNavController(LoginFragment.this).navigate(directions);
    }

    private void navigateToForgotPassword() {
        NavDirections directions = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment();
        NavHostFragment.findNavController(LoginFragment.this).navigate(directions);
    }
}

