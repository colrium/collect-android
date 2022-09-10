package com.colrium.collect.ui.auth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import com.colrium.collect.App;
import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.Attachment;
import com.colrium.collect.data.local.model.Client;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;

import com.colrium.collect.R;
import com.colrium.collect.data.local.model.User;
import com.colrium.collect.data.remote.Result;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.data.remote.auth.login.LoginResponse;
import com.colrium.collect.data.remote.auth.login.ResponseData;
import com.colrium.collect.databinding.FragmentLoginBinding;
import com.colrium.formbuilder.base.FormError;
import com.colrium.formbuilder.base.FormField;
import com.colrium.formbuilder.base.FormValidationListener;
import com.colrium.formbuilder.validation.ValidationNotEmpty;
import com.colrium.formbuilder.validation.ValidationRegex;
import com.colrium.formbuilder.Form;
import com.colrium.collect.ui.AuthActivity;
import com.colrium.collect.ui.MainActivity;
import com.colrium.collect.utility.AppPreferences;
import retrofit2.Call;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    AppPreferences appPreferences;
    Form form;
    boolean formValid = false;
    App app;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        app = (App)((AuthActivity) getActivity()).getApplication();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
        loginViewModel.setContext(getContext());
        final TextInputEditText usernameEditText = binding.username;
        final TextInputEditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button btnForgotPassword = binding.btnForgotPassword;
        final ProgressBar loadingProgressBar = binding.pbLoading;
        appPreferences = AppPreferences.getInstance(getContext());
        form  = new Form();
        form.addField(new FormField(usernameEditText).addValidation(new ValidationRegex(Patterns.EMAIL_ADDRESS)
                        .setMessage(getString(R.string.invalid_username))))
                .addField(new FormField(passwordEditText).addValidation(new ValidationNotEmpty()
                        .setMessage(getString(R.string.invalid_password))))
                .setListener(new FormValidationListener() {
                    @Override
                    public void onValidationPassed() {
                        formValid = false;
                        loginButton.setEnabled(true);
                    }

                    @Override
                    public void onValidationFailed(List<FormError> errors) {
                        loginButton.setEnabled(false);
                        formValid = false;
                        Log.e(LOG_TAG, "onValidationFailed errors " + errors.toString());
                    }
                });
        loginViewModel.setOnApiCallRequest(new ApiClient.ApiCall.OnApiCallRequestListener<LoginResponse>() {
            @Override
            public void onQueued(Call<LoginResponse> call) {
                loginButton.setText(getString(R.string.action_sign_in_progress));
            }

            @Override
            public void onResponse(Call<LoginResponse> call, ApiClient.ApiCall.Response<LoginResponse> response) {
                LoginResponse loginResponse = response.getData();
                ResponseData data = loginResponse.getData();
                User user = data.getUser();
                Attachment avatar = user != null? user.getAvatar() : null;
                ArrayList<Attachment> documents = user != null? user.getDocuments() : null;
                AccessToken accessToken = data.getAccessToken();
                Client client = accessToken != null? accessToken.getClient() : null;
                SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser = new SessionWithAccessTokenAndUser();

                Session session = new Session();
                session.setAccessToken(accessToken);
                session.setStatus("active");
                session.setUser(user);
                session.setUserId(user.getId());
                session.setAccessTokenId(accessToken.getId());
                session.setId(user.getId());
                session.setUuid(user.getUuid());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        app.getAppDatabase().sessionDao().insert(session);
                        if (user != null) {
                            app.getAppDatabase().userDao().insert(user);
                        }
                        if (avatar != null) {
                            app.getAppDatabase().attachmentDao().insert(avatar);
                        }
                        if (client != null) {
                            app.getAppDatabase().clientDao().insert(client);
                        }

                        app.getAppDatabase().accessTokenDao().insert(accessToken);


                    }
                });
                sessionWithAccessTokenAndUser.session = session;
                sessionWithAccessTokenAndUser.accessToken = accessToken;
                sessionWithAccessTokenAndUser.user = user;
                loginButton.setText(getString(R.string.action_sign_in_success));
                app.getOnAuthListener().onLogin(sessionWithAccessTokenAndUser);
            }

            @Override
            public void onError(Call<LoginResponse> call, ApiClient.ApiCall.Error<LoginResponse> error) {
                loginButton.setText(getString(R.string.action_sign_in_failed));
                showLoginFailed(error.message());
            }
        });

        loginViewModel.getLoginApiCall().observe(getViewLifecycleOwner(), new Observer<ApiClient.ApiCall>() {
                    @Override
                    public void onChanged(@Nullable ApiClient.ApiCall apiCall) {



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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && passwordEditText.isFocusable() && !passwordEditText.isFocused()) {
                    int pos = passwordEditText.getText().length();
                    passwordEditText.setSelection(pos);
                    passwordEditText.requestFocus();
                }
                return false;
            }
        });
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && form.validate()) {

                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        usernameEditText.setText(Constants.DEFAULT_USERNAME);
        passwordEditText.setText(Constants.DEFAULT_PASSWORD);
        if (usernameEditText.isFocusable() && !usernameEditText.isFocused()){

            int pos = usernameEditText.getText().length();
            if (pos > 0){
                form.validate();
            }
            usernameEditText.setSelection(pos);
            usernameEditText.requestFocus();
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navFromLoginToForgotPassword);
            }
        });
    }

    private void updateUiWithUser(String welcome) {
        // TODO : initiate successful logged in experience

        if (getContext() != null && getContext().getApplicationContext() != null) {

            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(String errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}