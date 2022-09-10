package com.colrium.collect.data.local.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.converter.Converters;
import com.colrium.collect.data.local.dao.AccessTokenDao;
import com.colrium.collect.data.local.dao.AttachmentDao;
import com.colrium.collect.data.local.dao.ClientDao;
import com.colrium.collect.data.local.dao.SessionDao;
import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.Attachment;
import com.colrium.collect.data.local.model.Client;
import com.colrium.collect.data.local.model.Session;
import com.colrium.collect.data.local.model.User;
import com.colrium.collect.data.local.dao.UserDao;

@Database(
        version = 4,
        entities = {AccessToken.class, Attachment.class, Client.class, Session.class, User.class},
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    private static AppDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, AppDatabase.DATABASE_NAME)
//                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
                sInstance.updateDatabaseCreated(context.getApplicationContext());
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


    public static void destroyInstance() {
        sInstance = null;
    }
    public abstract AccessTokenDao accessTokenDao();
    public abstract AttachmentDao attachmentDao();
    public abstract ClientDao clientDao();
    public abstract UserDao userDao();
    public abstract SessionDao sessionDao();
}
