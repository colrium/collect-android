package com.colrium.collect.fragments.auth;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordViewModel extends ViewModel {
    private static final String LOG_TAG = ForgotPasswordViewModel.class.getSimpleName();
    private ForgotPasswordFormState formState = new ForgotPasswordFormState();
    private Context context;
    public ForgotPasswordViewModel() {
    }

    public ForgotPasswordViewModel setContext(Context context){
        this.context = context;

        return this;
    }
    public static final class ForgotPasswordFormState {
        private boolean isSubmitting;
        private boolean isSubmitted;
        private Map errors = new HashMap();
        private MutableLiveData<ForgotPasswordFormState> observerble = new MediatorLiveData<>();

        public ForgotPasswordFormState() {
            observerble.setValue(this);
        }

        public boolean isValid() {
            return !isSubmitting && (errors == null || !errors.containsKey("username"));
        }

        public boolean hasErrors() {
            return errors != null && !errors.isEmpty() ;
        }

        public boolean isSubmitting() {
            return isSubmitting;
        }

        public ForgotPasswordFormState setSubmitting(boolean submitting) {
            isSubmitting = submitting;
            observerble.setValue(this);
            return this;
        }

        public boolean isSubmitted() {
            return isSubmitted;
        }

        public ForgotPasswordFormState setSubmitted(boolean submitted) {
            isSubmitted = submitted;
            observerble.setValue(this);
            return this;
        }

        public Map getErrors() {
            return errors;
        }

        public ForgotPasswordFormState setErrors(Map errors) {
            this.errors = errors;
            observerble.setValue(this);
            return this;
        }

        public LiveData<ForgotPasswordFormState> getObserverble() {
            return observerble;
        }
    }
}