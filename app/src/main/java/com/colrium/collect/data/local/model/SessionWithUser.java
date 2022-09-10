package com.colrium.collect.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class SessionWithUser {
    @Embedded
    public Session session;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "id"
    )
    public User user;
}
