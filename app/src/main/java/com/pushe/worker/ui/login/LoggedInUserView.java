package com.pushe.worker.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private final String id;
    private final String displayName;

    LoggedInUserView(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    String getUserId() {
        return id;
    }

    String getDisplayName() {
        return displayName;
    }
}