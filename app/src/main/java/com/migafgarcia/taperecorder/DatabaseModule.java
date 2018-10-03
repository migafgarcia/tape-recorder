package com.migafgarcia.taperecorder;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.migafgarcia.taperecorder.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase appDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "tape-recorder-db").build();
    }
}
