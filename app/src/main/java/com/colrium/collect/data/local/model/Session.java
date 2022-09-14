package com.colrium.collect.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.UUID;
@Entity(tableName = Session.TABLE_NAME)
public class Session {
    public static final String TABLE_NAME = "sessions";

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("uuid")
    @ColumnInfo(name = "uuid")
    private String uuid;


    @SerializedName("access_token")
    @Ignore
    private AccessToken accessToken;

    @SerializedName("access_token_id")
    @ColumnInfo(name = "access_token_id")
    private String accessTokenId;

    @SerializedName(value="user", alternate={"profile"})
    @Ignore
    private User user;

    @SerializedName(value="user_id")
    @ColumnInfo(name = "user_id")
    private String userId;

    @SerializedName("status")
    @ColumnInfo(name = "status")
    private String status = "inactive";

    @SerializedName("last_activity_timestamp")
    @ColumnInfo(name = "last_activity_timestamp")
    private Long lastActivityTimestamp = new Date().getTime();

    public Session () {
        this.id = new ObjectId().toString();
        this.uuid = UUID.randomUUID().toString();
    }

    public Session(String id) {
        this.id = id;
        this.uuid = UUID.randomUUID().toString();
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;

    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        this.accessTokenId = accessToken != null? accessToken.getId() : null;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null? user.getId() : null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public void setLastActivityTimestamp(Long lastActivityTimestamp) {
        this.lastActivityTimestamp = lastActivityTimestamp;
    }

    public void setLastActivityTimestamp(Date lastActivityTimestamp) {
        this.lastActivityTimestamp = lastActivityTimestamp.getTime();
    }
}
