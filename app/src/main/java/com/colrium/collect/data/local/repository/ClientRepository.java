package com.colrium.collect.data.local.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.colrium.collect.data.local.dao.ClientDao;
import com.colrium.collect.data.local.dao.ClientDao;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.Client;
import com.colrium.collect.data.local.model.Client;

public class ClientRepository {
    private MediatorLiveData<List<Client>> observableEntries;
    private final ClientDao dao;
    private final AppDatabase appDatabase;
    private static ClientRepository sInstance;

    private ClientRepository(final AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.dao = appDatabase.clientDao();
        observableEntries = new MediatorLiveData<>();

        observableEntries.addSource(dao.getAll(),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        observableEntries.postValue(entries);
                    }
                });
    }

    public static ClientRepository getInstance(final AppDatabase appDatabase) {
        if(sInstance == null) {
            sInstance = new ClientRepository(appDatabase);
        }
        return sInstance;
    }

    public MediatorLiveData<List<Client>> getObservableEntries() {
        return observableEntries;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void insert(Client entry) {
        dao.insert(entry);
    }

    public void delete(String id) {
        appDatabase.sessionDao().delete(id);
    }

    public Client getById(String id) {
        return dao.getById(id);
    }

    public LiveData<List<Client>> getAll() {
        return dao.getAll();
    }


}
