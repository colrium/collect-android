package com.colrium.collect.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.AccessTokenWithSessionAndUser;

@Dao
public interface AccessTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccessToken accessToken);
    @Update
    void update(AccessToken accessToken);
    @Query("SELECT * FROM access_tokens WHERE id = :id")
    AccessToken getById(String id);

    @Query("SELECT * FROM access_tokens")
    LiveData<List<AccessToken>> getAll();

    @Query("DELETE FROM access_tokens WHERE id = :id")
    void delete(String id);

    @Delete
    void delete(AccessToken accessToken);

    @Query("DELETE FROM access_tokens")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM access_tokens")
    LiveData<List<AccessTokenWithSessionAndUser>> getAllWithSessionAndUser();
}
