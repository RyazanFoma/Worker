package com.pushe.worker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pushe.worker.data.LoginDataSource;
import com.pushe.worker.data.Result;
import com.pushe.worker.data.model.LoggedInUser;
import com.pushe.worker.R;

public class LoginViewModel extends ViewModel {

    private final static MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private static LoginDataSource loginDataSource;
    public LoggedInUserView loggedInUserView = null;
    private char[] hashPassword;

    LoginViewModel(LoginDataSource loginDataSource) {
        LoginViewModel.loginDataSource = loginDataSource;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LoginDataSource getLoginDataSource() {
        return loginDataSource;
    }

    public void login(String id) {
        loginDataSource.requestUser(id);
    }
    public void logout() {
        // can be launched in a separate asynchronous job
//        loginDataSource = null;
    }

    protected void loginSourceChanged(Result<?> result) {
        if (result instanceof Result.Success) {
            LoggedInUser data = (LoggedInUser) ((Result.Success<?>) result).getData();
            loggedInUserView = new LoggedInUserView(data.getUserId(), data.getDisplayName());
            hashPassword = data.getHashPassword().toCharArray();
            loginFormState.setValue(new LoginFormState(true));
        } else if (result instanceof Result.Error) {
            loginFormState.setValue(new LoginFormState(result.toString()));
        }
    }

    public void loginPasswordChanged(String password) {
        if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
         return password != null && password.trim().length() == 6;
    }

    /**
     * Compare password hash with password entry
     * @param inputText - password entry
     * @return - comparison result
     */
    public boolean isVerifiedPassword(String inputText) {
        char[] input = inputText.toCharArray();
        if (hashPassword.length != input.length) return false;
        for (int position = 0; position<hashPassword.length; position++ ) {
//            Отключим кодирование на время отладки
//            if ((int) hasp[position] != ((int) input[position]*13-(position+1)*7)%873) return false;
            if ((int) hashPassword[position] != ((int) input[position])) return false;
        }
        return true;
    }

}