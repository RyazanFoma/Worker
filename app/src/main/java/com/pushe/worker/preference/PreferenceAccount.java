package com.pushe.worker.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pushe.worker.R;

/**
 * Singleton class for getting the path, account and password for access to 1C from preferences
 */
public class PreferenceAccount {

    /**
     * Https path to 1C
     */
    public String path;

    /**
     * Account for connecting to 1C
     */
    public String account;

    /**
     * Password for connecting to 1C
     */
    public String password;

    private static final String PATH = "erp_path";
    private static final String ACCOUNT = "erp_user";
    private static final String PASSWORD = "erp_password";

    /**
     * Constructor
     * @param context - application context
     */
    public PreferenceAccount(Context context) {
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(context);
        path = pr.getString(PATH, context.getString(R.string.pref_default_1c_path));
        account = pr.getString(ACCOUNT, context.getString(R.string.pref_default_1c_user));
        password = pr.getString(PASSWORD, context.getString(R.string.pref_default_1c_password));
    }
}
