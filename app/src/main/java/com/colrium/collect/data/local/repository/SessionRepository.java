package com.colrium.collect.data.local.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.colrium.collect.data.local.dao.SessionDao;
import com.colrium.collect.data.local.dao.SessionDao;
import com.colrium.collect.data.local.dao.SessionDao;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.data.local.model.SessionWithUser;

public class SessionRepository {
    private static final String LOG_TAG = SessionRepository.class.getSimpleName();
    private MediatorLiveData<List<SessionWithUser>> observableEntries;
    private final SessionDao dao;
    private final AppDatabase appDatabase;
    private static SessionRepository sInstance;

    private SessionRepository(final AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.dao = appDatabase.sessionDao();
        observableEntries = new MediatorLiveData<>();

        observableEntries.addSource(dao.getAll(),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        observableEntries.postValue(entries);
                    }
                });
    }

    public static SessionRepository getInstance(final AppDatabase appDatabase) {
        if(sInstance == null) {
            sInstance = new SessionRepository(appDatabase);
        }
        return sInstance;
    }

    public MediatorLiveData<List<SessionWithUser>> getObservableEntries() {
        return observableEntries;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void insert(Session entry) {
        dao.insert(entry);
    }

    public void delete(String id) {
        appDatabase.sessionDao().delete(id);
    }

    public SessionWithUser getById(String id) {
        return dao.getById(id);
    }

    public LiveData<List<SessionWithUser>> getAll() {
        return dao.getAll();
    }

    public LiveData<List<SessionWithUser>> getByStatus(String status) {
        return appDatabase.sessionDao().getByStatus(status);
    }

    public LiveData<List<SessionWithAccessTokenAndUser>> getAllWithAccessTokenAndUser() {
        MediatorLiveData<List<SessionWithAccessTokenAndUser>> liveData = new MediatorLiveData<>();
        liveData.addSource(appDatabase.sessionDao().getAllWithAccessTokenAndUser(),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        liveData.postValue(entries);
                    }
                });
        return liveData;
    }

    public LiveData<List<SessionWithAccessTokenAndUser>> getByStatusWithAccessTokenAndUser(String status) {
        MediatorLiveData<List<SessionWithAccessTokenAndUser>> liveData = new MediatorLiveData<>();
        liveData.addSource(appDatabase.sessionDao().getByStatusWithAccessTokenAndUser(status),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        liveData.postValue(entries);
                    }
                });
        return liveData;
    }
    public LiveData<SessionWithAccessTokenAndUser> getCurrentWithAccessTokenAndUser() {
        MediatorLiveData<SessionWithAccessTokenAndUser> liveData = new MediatorLiveData<>();
        liveData.addSource(appDatabase.sessionDao().getByStatusWithAccessTokenAndUser("active"), entries -> {
            if (appDatabase.getDatabaseCreated().getValue() != null && entries != null && !entries.isEmpty()) {
                liveData.postValue(entries.get(0));
            }
        });
        return liveData;
    }
}
