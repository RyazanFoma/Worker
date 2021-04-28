package com.pushe.worker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * A wrapper class that contains the ViewModel for the LoginActivity
 * and stores the state of the activity.
 */
public class StateViewModel extends ViewModel {

    private final MutableLiveData<LoginState> mutableLiveData = new MutableLiveData<>();
    private final static LoginState state = LoginState.getInstance();

    LiveData<LoginState> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setStateToBarCode() {
        setState(LoginState.ActivityState.BARCODE);
    }

    public void setStateToPassword() {
        setState(LoginState.ActivityState.PASSWORD);
    }

    private void setState(LoginState.ActivityState value) {
        state.setActivityState(value);
        mutableLiveData.setValue(state);
    }
}
