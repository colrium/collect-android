package com.colrium.collect.data.local.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.colrium.collect.data.local.dao.AttachmentDao;
import com.colrium.collect.data.local.dao.AttachmentDao;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.Attachment;
import com.colrium.collect.data.local.model.Attachment;

public class AttachmentRepository {
    private MediatorLiveData<List<Attachment>> observableEntries;
    private final AttachmentDao dao;
    private final AppDatabase appDatabase;
    private static AttachmentRepository sInstance;

    private AttachmentRepository(final AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.dao = appDatabase.attachmentDao();
        observableEntries = new MediatorLiveData<>();

        observableEntries.addSource(dao.getAll(),
                entries -> {
                    if (appDatabase.getDatabaseCreated().getValue() != null) {
                        observableEntries.postValue(entries);
                    }
                });
    }

    public static AttachmentRepository getInstance(final AppDatabase appDatabase) {
        if(sInstance == null) {
            sInstance = new AttachmentRepository(appDatabase);
        }
        return sInstance;
    }

    public MediatorLiveData<List<Attachment>> getObservableEntries() {
        return observableEntries;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void insert(Attachment entry) {
        dao.insert(entry);
    }

    public void delete(String id) {
        appDatabase.sessionDao().delete(id);
    }

    public Attachment getById(String id) {
        return dao.getById(id);
    }

    public LiveData<List<Attachment>> getAll() {
        return dao.getAll();
    }
}
