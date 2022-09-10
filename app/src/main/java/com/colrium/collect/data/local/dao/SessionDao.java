package com.colrium.collect.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.data.local.model.SessionWithUser;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Session session);

    @Query("UPDATE sessions SET status='inactive' WHERE status = 'active'")
    void deactivateAll();

    @Transaction
    @Query("SELECT * FROM sessions WHERE status = :status ORDER BY last_activity_timestamp DESC")
    LiveData<List<SessionWithUser>> getByStatus(String status);

    @Transaction
    @Query("SELECT * FROM sessions WHERE id = :id")
    SessionWithAccessTokenAndUser getByIdWithAccessTokenAndUser(String id);

    @Transaction
    @Query("SELECT * FROM sessions WHERE status = 'active' ORDER BY last_activity_timestamp DESC LIMIT 1 OFFSET 0")
    LiveData<SessionWithAccessTokenAndUser> getActiveSessionWithAccessTokenAndUser();

    @Transaction
    @Query("SELECT * FROM sessions WHERE status = :status ORDER BY last_activity_timestamp DESC")
    LiveData<List<SessionWithAccessTokenAndUser>> getByStatusWithAccessTokenAndUser(String status);


    @Query("SELECT * FROM sessions WHERE id = :id")
    SessionWithUser getById(String id);

    @Query("SELECT * FROM sessions ORDER BY last_activity_timestamp DESC")
    LiveData<List<SessionWithUser>> getAll();

    @Query("SELECT * FROM sessions ORDER BY last_activity_timestamp DESC")
    LiveData<List<SessionWithAccessTokenAndUser>> getAllWithAccessTokenAndUser();

    @Query("DELETE FROM sessions WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM sessions")
    void deleteAll();
}
