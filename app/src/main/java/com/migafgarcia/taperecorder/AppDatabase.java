package com.migafgarcia.taperecorder;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.migafgarcia.taperecorder.models.Recording;
import com.migafgarcia.taperecorder.models.RecordingDao;

@Database(entities = {Recording.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecordingDao recordingDao();

}
