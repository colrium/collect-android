package com.colrium.collect.fragments.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.colrium.collect.databinding.FragmentForgotPasswordBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import com.colrium.collect.R;
import com.colrium.formbuilder.Form;
import com.colrium.formbuilder.base.FormError;
import com.colrium.formbuilder.base.FormField;
import com.colrium.formbuilder.base.FormValidationListener;
import com.colrium.formbuilder.base.ValidationMode;
import com.colrium.formbuilder.validation.ValidationRegex;

public class ForgotPasswordFragment  extends Fragment {

    private ForgotPasswordViewModel mViewModel;
    private FragmentForgotPasswordBinding binding;
    Form form;
    String email;
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        form  = new Form(ValidationMode.ON_CONTENT_CHANGE);
        form.addField(new FormField(binding.email).addValidation(new ValidationRegex(Patterns.EMAIL_ADDRESS)
                .setMessage(getString(R.string.invalid_username))));
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        final TextInputEditText emailEditText = binding.email;
        final Button btnRecover = binding.btnRecover;
        final Button btnLogin = binding.btnLogin;
        form.setListener(new FormValidationListener() {
            @Override
            public void onValidationPassed() {
                btnRecover.setEnabled(true);
            }

            @Override
            public void onValidationFailed(List<FormError> errors) {
                btnRecover.setEnabled(false);
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = emailEditText.getText().toString();
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    form.submit();
                }
                return false;
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navFromForgotPasswordToLogin);
            }
        });
    }


}
