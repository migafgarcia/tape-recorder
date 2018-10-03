package com.migafgarcia.taperecorder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.migafgarcia.taperecorder.models.Recording;

@Database(entities = {Recording.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecordingDao recordingDao();

}
