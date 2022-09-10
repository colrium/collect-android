package com.colrium.collect.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class SessionWithAccessTokenAndUser {
    @Embedded
    public Session session;
    @Relation(
            entity = User.class,
            parentColumn = "user_id",
            entityColumn = "id"
    )
    public User user;
    @Relation(
            entity = AccessToken.class,
            parentColumn = "access_token_id",
            entityColumn = "id"
    )
    public AccessToken accessToken;
}
