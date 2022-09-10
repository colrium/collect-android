package com.colrium.collect.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.colrium.collect.data.local.model.Client;

@Dao
public interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Client client);

    @Query("SELECT * FROM clients WHERE clients.id LIKE :id")
    Client getById(String id);

    @Query("DELETE FROM clients WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM clients")
    LiveData<List<Client>> getAll();

    @Query("DELETE FROM clients")
    void deleteAll();
}
