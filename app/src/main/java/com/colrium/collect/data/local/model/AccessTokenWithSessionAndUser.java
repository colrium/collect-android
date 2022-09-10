package com.colrium.collect.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class AccessTokenWithSessionAndUser {
    @Embedded
    public AccessToken accessToken;
    @Relation(
            entity = Session.class,
            parentColumn = "id",
            entityColumn = "access_token_id"
    )
    public SessionWithUser session;
}
