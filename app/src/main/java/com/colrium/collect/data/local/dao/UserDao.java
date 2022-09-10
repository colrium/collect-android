package com.colrium.collect.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.colrium.collect.data.local.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users WHERE users.id LIKE :id")
    User getById(String id);

    @Query("DELETE FROM users WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAll();

    @Query("DELETE FROM users")
    void deleteAll();
}
