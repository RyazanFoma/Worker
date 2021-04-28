package com.pushe.worker.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    private static PreferenceAccount ourInstance;
    private static final String PATH = "erp_path";
    private static final String ACCOUNT = "erp_user";
    private static final String PASSWORD = "erp_password";

    /**
     * Getting a instance of user preferences
     * @param context - application context
     * @return - user preference's
     */
    public static PreferenceAccount getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new PreferenceAccount(context);
        }
        return ourInstance;
    }

    /**
     * Constructor
     * @param context - application context
     */
    private PreferenceAccount(Context context) {
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(context);
        path = pr.getString(PATH, null);
        account = pr.getString(ACCOUNT, null);
        password = pr.getString(PASSWORD, null);
    }
}
