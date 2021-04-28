package com.pushe.worker.ui.login;

import com.pushe.worker.data.Result;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {

    @Nullable
    private final String restApiError;
    @Nullable
    private final Integer passwordError;
    private final boolean isDataValid;

    LoginFormState(@Nullable String restApiError) {
        this.restApiError = restApiError;
        this.passwordError = null;
        this.isDataValid = false;
    }

    LoginFormState(@Nullable Integer passwordError) {
        this.restApiError = null;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.restApiError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    String getRestApiError() {
        return restApiError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}