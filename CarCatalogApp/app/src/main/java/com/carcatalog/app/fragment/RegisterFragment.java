package com.carcatalog.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.carcatalog.app.UserPrefs;
import com.carcatalog.app.databinding.FragmentRegisterBinding;

/**
 * REGISTER FRAGMENT
 * Collects username + password + confirmation and saves to SharedPreferences.
 * Replaces RegisterActivity.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCreateAccount.setOnClickListener(v -> attemptRegister());

        // "Back to Sign In" link → pop this fragment off the back stack
        binding.tvLoginLink.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    private void attemptRegister() {
        binding.tilRegUsername.setError(null);
        binding.tilRegPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        String username = binding.etRegUsername.getText() == null ? ""
                : binding.etRegUsername.getText().toString().trim();
        String password = binding.etRegPassword.getText() == null ? ""
                : binding.etRegPassword.getText().toString();
        String confirm = binding.etConfirmPassword.getText() == null ? ""
                : binding.etConfirmPassword.getText().toString();

        if (username.length() < 3) {
            binding.tilRegUsername.setError("Username must be at least 3 characters");
            binding.tilRegUsername.requestFocus();
        } else if (username.contains(" ")) {
            binding.tilRegUsername.setError("Username cannot contain spaces");
            binding.tilRegUsername.requestFocus();
        } else if (password.length() < 4) {
            binding.tilRegPassword.setError("Password must be at least 4 characters");
            binding.tilRegPassword.requestFocus();
        } else if (!password.equals(confirm)) {
            binding.tilConfirmPassword.setError("Passwords do not match");
            binding.tilConfirmPassword.requestFocus();
        } else {
            boolean success = UserPrefs.register(requireContext(), username, password);
            if (success) {
                Toast.makeText(requireContext(),
                        "Account created! You can now sign in.",
                        Toast.LENGTH_LONG).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                binding.tilRegUsername.setError("Username already taken — choose another");
                binding.tilRegUsername.requestFocus();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
