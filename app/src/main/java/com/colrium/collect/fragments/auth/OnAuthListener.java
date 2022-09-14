package com.colrium.collect.fragments.auth;

import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;

public interface OnAuthListener {
    void onLogin(SessionWithAccessTokenAndUser session);
    void onLogout();
}
