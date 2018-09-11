package com.migafgarcia.taperecorder.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface RecordingDao {

    @Query("SELECT * FROM recordings")
    Maybe<List<Recording>> getAll();

    @Insert
    void insertAll(Recording... recordings);

    @Delete
    void delete(Recording recording);

    @Update
    void updateUsers(Recording... recordings);

}
