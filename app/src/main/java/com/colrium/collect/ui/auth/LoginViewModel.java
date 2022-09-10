package com.colrium.collect.ui.auth;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colrium.collect.R;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.data.remote.api.interfaces.AuthInterface;
import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.model.Attachment;
import com.colrium.collect.data.local.model.Client;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.data.remote.Result;
import com.colrium.collect.data.remote.auth.login.LoginResponse;
import com.colrium.collect.data.remote.auth.login.ResponseData;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.User;
import com.colrium.collect.data.local.repository.SessionRepository;
import com.colrium.collect.utility.AppPreferences;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private static final String LOG_TAG = LoginViewModel.class.getSimpleName();
    private MediatorLiveData<FormState> formState = new MediatorLiveData<>();
    private MediatorLiveData<ApiClient.ApiCall<LoginResponse>> loginApiCall = new MediatorLiveData<>();
    private SessionRepository sessionRepository;
    private MediatorLiveData<List<SessionWithAccessTokenAndUser>> sessions= new MediatorLiveData<>();
    private MediatorLiveData<List<SessionWithAccessTokenAndUser>> activeSessions= new MediatorLiveData<>();
    private MediatorLiveData<SessionWithAccessTokenAndUser> session = new MediatorLiveData<>();
    private AppDatabase appDatabase;
    private Context context;
    private AppPreferences appPreferences;

    public LoginViewModel() {
        formState.setValue(new FormState());
        loginApiCall.setValue(new ApiClient.ApiCall<>());
    }

    public LoginViewModel setContext(Context context){
        this.context = context;
        this.appPreferences = AppPreferences.getInstance(context);
        this.appDatabase = AppDatabase.getInstance(this.context);
        this.sessionRepository = SessionRepository.getInstance(appDatabase);

        sessions.addSource(sessionRepository.getAllWithAccessTokenAndUser(), entries -> {
            sessions.postValue(entries);
        });
        activeSessions.addSource(sessionRepository.getByStatusWithAccessTokenAndUser("active"), entries -> {
            activeSessions.postValue(entries);
        });
        session.addSource(sessionRepository.getCurrentWithAccessTokenAndUser(), entry -> {
            session.postValue(entry);
        });

        return this;
    }

    public LiveData<FormState> getFormState() {
        return formState;
    }
    public LiveData<ApiClient.ApiCall<LoginResponse>> getLoginApiCall() {
        return loginApiCall;
    }


    public LiveData<List<SessionWithAccessTokenAndUser>> getAllSessions() {
        return activeSessions;
    }
    public void setOnApiCallRequest(ApiClient.ApiCall.OnApiCallRequestListener onApiCallRequestListener) {
        loginApiCall.postValue(loginApiCall.getValue().setOnApiCallRequest(onApiCallRequestListener));
    }

    public LiveData<SessionWithAccessTokenAndUser> getActiveSession() {
        return session;
    }


    public void login(String username, String password) {
        formState.postValue(formState.getValue().setUsername(username).setPassword(password).clearError("request").setSubmitting(true));
        Map<String, Object> valuesMap = formState.getValue().getValues();
        loginApiCall.postValue(loginApiCall.getValue()
                .endpoint(ApiClient
                        .retrofit()
                        .create(AuthInterface.class)
                        .login(ApiClient.ApiCall.parseBody(valuesMap))
                )

                .enqueue());

    }

    public void loginDataChanged(String username, String password) {
        formState.postValue(formState.getValue().setUsername(username).setPassword(password));
    }



    public static final class FormState {
        Map<String, Object> values = new ArrayMap<>();
        private Result result;
        private boolean isSubmitting;
        private boolean isSubmitted;
        private Map errors = new HashMap();

        public FormState() {
            //initialize values
            values.put("grant_type", Constants.DEFAULT_GRANT_TYPE);
            values.put("client_id", Constants.CLIENT_ID);
            values.put("client_secret", Constants.CLIENT_SECRET);
            values.put("username", "");
            values.put("password", "");
        }



        // A placeholder username validation check
        private boolean isUserNameValid(String username) {
            if (username == null) {
                return false;
            }
            if (username.contains("@")) {
                return Patterns.EMAIL_ADDRESS.matcher(username).matches();
            } else {
                return !username.trim().isEmpty();
            }
        }

        // A placeholder password validation check
        private boolean isPasswordValid(String password) {
            return password != null && password.trim().length() > 0;
        }

        public boolean isValid() {
            return !isSubmitting && (errors == null || (!errors.containsKey("username") && !errors.containsKey("password")));
        }

        public boolean hasErrors() {
            return errors != null && !errors.isEmpty() ;
        }

        public FormState setUsername(@Nullable String username) {
            values.put("username", username);
            if (!isUserNameValid(username)) {
                errors.put("username", R.string.invalid_username);
            }
            else {
                errors.remove("username");
            }
            return this;
        }

        public FormState setPassword(@Nullable String password) {
            values.put("password", password);
            if (!isPasswordValid(password)) {
                errors.put("password", R.string.invalid_password);
            }
            else {
                errors.remove("password");
            }
            return this;
        }


        public boolean isSubmitting() {
            return isSubmitting;
        }

        public FormState setSubmitting(boolean submitting) {
            isSubmitting = submitting;
            return this;
        }
        public FormState setSubmitted(boolean submitted) {
            isSubmitting = submitted;
            return this;
        }

        public void submit() {

        }

        public boolean isSubmitted() {
            return isSubmitted;
        }

        public Map<String, Object> getValues() {
            return values;
        }

        public Map getErrors() {
            return errors;
        }
        public FormState setError(String name, String error) {
            errors.put(name, error);
            return this;
        }
        public FormState clearError(String name) {
            errors.remove(name);
            return this;
        }
        public FormState clearErrors() {
            errors.clear();
            return this;
        }
    }
}