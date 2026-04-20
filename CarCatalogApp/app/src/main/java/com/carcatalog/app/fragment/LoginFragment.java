package com.carcatalog.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.carcatalog.app.MainActivity;
import com.carcatalog.app.UserPrefs;
import com.carcatalog.app.databinding.FragmentLoginBinding;

/**
 * LOGIN FRAGMENT
 * Validates credentials stored in SharedPreferences.
 * On success, calls MainActivity.showMainUI().
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> handleLogin());

        binding.btnRegister.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(((View) binding.getRoot().getParent()).getId(),
                                new RegisterFragment())
                        .addToBackStack(null)
                        .commit()
        );

        binding.etPassword.setOnEditorActionListener((v, actionId, event) -> {
            handleLogin();
            return true;
        });
    }

    private void handleLogin() {
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        String username = binding.etEmail.getText() == null ? ""
                : binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText() == null ? ""
                : binding.etPassword.getText().toString();

        if (username.isEmpty()) {
            binding.tilEmail.setError("Username is required");
            binding.tilEmail.requestFocus();
        } else if (password.isEmpty()) {
            binding.tilPassword.setError("Password is required");
            binding.tilPassword.requestFocus();
        } else {
            if (UserPrefs.login(requireContext(), username, password)) {
                ((MainActivity) requireActivity()).showMainUI();
            } else {
                Toast.makeText(requireContext(),
                        "Invalid username or password", Toast.LENGTH_SHORT).show();
                binding.tilPassword.setError(" ");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
