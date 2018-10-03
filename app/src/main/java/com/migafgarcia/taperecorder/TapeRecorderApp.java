package com.migafgarcia.taperecorder;


import android.app.Application;

import com.migafgarcia.taperecorder.database.AppDatabase;

import javax.inject.Inject;

public class TapeRecorderApp extends Application {

    @Inject
    public AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();

        appComponent.inject(this);

    }
}
