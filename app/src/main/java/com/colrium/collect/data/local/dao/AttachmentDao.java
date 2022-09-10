package com.colrium.collect.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.colrium.collect.data.local.model.Attachment;
import com.colrium.collect.data.local.model.User;
@Dao
public interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Attachment attachment);

    @Query("SELECT * FROM attachments WHERE id LIKE :id")
    Attachment getById(String id);

    @Query("DELETE FROM attachments WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM attachments")
    LiveData<List<Attachment>> getAll();

    @Query("DELETE FROM attachments")
    void deleteAll();
}
