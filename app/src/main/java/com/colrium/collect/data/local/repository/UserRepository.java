package com.colrium.collect.data.local.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.colrium.collect.data.local.dao.UserDao;
import com.colrium.collect.data.local.dao.UserDao;
import com.colrium.collect.data.local.dao.UserDao;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.User;
import com.colrium.collect.data.local.model.User;

public class UserRepository {
    private MediatorLiveData<List<User>> observableEntries;
    private final UserDao dao;
    private final AppDatabase appDatabase;
    private static UserRepository sInstance;

    private UserRepository(final AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.dao = appDatabase.userDao();
        observableEntries = new MediatorLiveData<>();

        observableEntries.addSource(dao.getAll(),
                entries -> {

                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        observableEntries.postValue(entries);
                    }
                });
    }

    public static UserRepository getInstance(final AppDatabase appDatabase) {
        if(sInstance == null) {
            sInstance = new UserRepository(appDatabase);
        }
        return sInstance;
    }

    public MediatorLiveData<List<User>> getObservableEntries() {
        return observableEntries;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void insert(User entry) {
        dao.insert(entry);
    }

    public void delete(String id) {
        appDatabase.sessionDao().delete(id);
    }

    public User getById(String id) {
        return dao.getById(id);
    }

    public LiveData<List<User>> getAll() {
        return dao.getAll();
    }
}
