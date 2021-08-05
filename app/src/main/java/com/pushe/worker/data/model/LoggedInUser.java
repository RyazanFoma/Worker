package com.pushe.worker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    @SerializedName("Код")
    private String userId;
    @SerializedName("Наименование")
    private String displayName;
    @SerializedName("Пароль")
    private String hashPassword;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getHashPassword() {
        return hashPassword;
    }
    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword; }
}