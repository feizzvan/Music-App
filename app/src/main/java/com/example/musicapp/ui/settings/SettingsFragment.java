package com.example.musicapp.ui.settings;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.musicapp.R;
import com.example.musicapp.data.model.auth.LogoutRequest;
import com.example.musicapp.data.model.auth.UpdatePasswordRequest;
import com.example.musicapp.data.repository.auth.AuthRepositoryImpl;
import com.example.musicapp.databinding.DialogChangePasswordBinding;
import com.example.musicapp.ui.auth.AuthActivity;
import com.example.musicapp.utils.AppUtils;
import com.example.musicapp.utils.TokenManager;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_PREF_DARK_MODE = "dark_mode";
    public static final String KEY_PREF_LANGUAGE = "language";
    public static final String KEY_PREF_CHANGE_PASSWORD = "change_password";
    public static final String KEY_PREF_LOGOUT = "logout";
    private DialogChangePasswordBinding mBinding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public TokenManager tokenManager;

    @Inject
    public AuthRepositoryImpl authRepository;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPreferences();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mDisposable.clear();
    }

    private void setupPreferences() {
        SwitchPreferenceCompat darkModePref = findPreference(KEY_PREF_DARK_MODE);
        if (darkModePref != null) {
            darkModePref.setOnPreferenceChangeListener((preference, newValue) -> {
                changeTheme(preference, newValue);
                return true;
            });
        }

        ListPreference languagePref = findPreference(KEY_PREF_LANGUAGE);
        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                changeLanguage(preference, newValue);
                return true;
            });
        }

        Preference changePasswordPref = findPreference(KEY_PREF_CHANGE_PASSWORD);
        if (changePasswordPref != null) {
            changePasswordPref.setVisible(tokenManager.getToken() != null);
            changePasswordPref.setVisible(tokenManager.getToken() != null);
            changePasswordPref.setOnPreferenceClickListener(preference -> {
                showChangePasswordDialog();
                return true;
            });
        }

        Preference logoutPref = findPreference(KEY_PREF_LOGOUT);
        if (logoutPref != null) {
            logoutPref.setVisible(tokenManager.getToken() != null);
            logoutPref.setOnPreferenceClickListener(preference -> {
                performLogout();
                return true;
            });
        }
    }

    private void changeTheme(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = preference.getSharedPreferences();
        boolean oldDarkMode = false;
        if (sharedPreferences != null) {
            oldDarkMode = sharedPreferences.getBoolean(KEY_PREF_DARK_MODE, false);
        }
        boolean newDarkMode = Boolean.parseBoolean(newValue.toString());
        if (newDarkMode != oldDarkMode) {
            AppUtils.sIsConfigChanged = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                UiModeManager uiModeManager = requireActivity().getSystemService(UiModeManager.class);
                int uiMode = newDarkMode ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO;
                uiModeManager.setApplicationNightMode(uiMode);
            } else {
                int mode = newDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);
            }
        }
    }

    private void changeLanguage(Preference preference, Object newValue) {
        String defaultLanguage = Locale.getDefault().getLanguage();
        String oldLanguage = defaultLanguage;
        String newLanguage = newValue.toString();
        SharedPreferences sharedPreferences = preference.getSharedPreferences();
        if (sharedPreferences != null) {
            oldLanguage = sharedPreferences.getString(KEY_PREF_LANGUAGE, defaultLanguage);
        }
        if (oldLanguage.compareTo(newLanguage) != 0) {
            AppUtils.sIsConfigChanged = true;
            LocaleListCompat localeListCompat = LocaleListCompat.forLanguageTags(newLanguage);
            AppCompatDelegate.setApplicationLocales(localeListCompat);
        }
    }

    private void performLogout() {
        int userId = tokenManager.getUserId();
        LogoutRequest request = new LogoutRequest(userId);

        mDisposable.add(authRepository.logout(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            tokenManager.clearToken();
                            startActivity(new Intent(getContext(), AuthActivity.class));
                            requireActivity().finishAffinity();
                        }, throwable -> Toast.makeText(getContext(), "Logout failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                )
        );
    }

    private void showChangePasswordDialog() {
        mBinding = DialogChangePasswordBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.title_change_password)
                .setView(mBinding.getRoot())
                .setPositiveButton(R.string.action_save, null) // set null để tự xử lý validate
                .setNegativeButton(R.string.action_cancel, null)
                .create();

        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String oldPassword = Objects.requireNonNull(mBinding.edtOldPassword.getText()).toString();
                String newPassword = Objects.requireNonNull(mBinding.edtNewPassword.getText()).toString();
                String confirmPassword = Objects.requireNonNull(mBinding.edtConfirmPassword.getText()).toString();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(requireContext(), R.string.text_please_fill_in_all_information, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(newPassword)) {
                    Toast.makeText(requireContext(), R.string.text_invalid_password_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(requireContext(), R.string.text_confirmation_password_does_not_match, Toast.LENGTH_SHORT).show();
                    return;
                }

                performChangePassword(oldPassword, newPassword, dialog);
            });
        });

        dialog.setOnDismissListener(d -> mBinding = null);
        dialog.show();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        return password.matches(passwordPattern);
    }

    private void performChangePassword(String oldPassword, String newPassword, AlertDialog dialog) {
        int userId = tokenManager.getUserId();
        UpdatePasswordRequest request = new UpdatePasswordRequest(userId, oldPassword, newPassword);
        String token = "Bearer " + tokenManager.getToken();

        mDisposable.add(authRepository.updatePassword(token, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Toast.makeText(requireContext(), R.string.change_password_successfully, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        },
                        throwable -> Toast.makeText(requireContext(), R.string.change_password_failed, Toast.LENGTH_SHORT).show()
                )
        );
    }
}