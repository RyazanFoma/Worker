package com.pushe.worker.ui.login;

/**
 * A singleton holding class that stores the state of the activity form.
 */
class LoginState {

    enum ActivityState { BARCODE, PASSWORD }

    private static final LoginState LOGIN_STATE = new LoginState();
    private ActivityState activityState;

    private LoginState() {
        activityState = ActivityState.BARCODE;
    }

    static LoginState getInstance() {
        return LOGIN_STATE;
    }

    ActivityState getActivityState() {
        return activityState;
    }

    void setActivityState(ActivityState activityState) {
        this.activityState = activityState;
    }
}
