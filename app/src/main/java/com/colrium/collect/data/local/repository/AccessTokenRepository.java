package com.colrium.collect.data.local.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.colrium.collect.data.local.dao.AccessTokenDao;
import com.colrium.collect.data.local.dao.AccessTokenDao;
import com.colrium.collect.data.local.dao.AccessTokenDao;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.AccessToken;

public class AccessTokenRepository {
    private MediatorLiveData<List<AccessToken>> observableEntries;
    private final AccessTokenDao dao;
    private final AppDatabase appDatabase;
    private static AccessTokenRepository sInstance;

    private AccessTokenRepository(final AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.dao = appDatabase.accessTokenDao();
        observableEntries = new MediatorLiveData<>();

        observableEntries.addSource(dao.getAll(),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        observableEntries.postValue(entries);
                    }
                });
    }

    public static AccessTokenRepository getInstance(final AppDatabase appDatabase) {
        if(sInstance == null) {
            sInstance = new AccessTokenRepository(appDatabase);
        }
        return sInstance;
    }

    public MediatorLiveData<List<AccessToken>> getObservableEntries() {
        return observableEntries;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void insert(AccessToken entry) {
        dao.insert(entry);
    }

    public void delete(String id) {
        appDatabase.sessionDao().delete(id);
    }

    public AccessToken getById(String id) {
        return dao.getById(id);
    }

    public LiveData<List<AccessToken>> getAll() {
        return dao.getAll();
    }
}
