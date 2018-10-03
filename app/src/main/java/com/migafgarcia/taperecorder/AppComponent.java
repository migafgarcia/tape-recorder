package com.migafgarcia.taperecorder;

import android.content.Context;

import com.migafgarcia.taperecorder.recordings.RecorderActivity;
import com.migafgarcia.taperecorder.recordings.RecorderService;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {DatabaseModule.class, ContextModule.class})
@Singleton
public interface AppComponent {

    Context context();

    void inject(RecorderActivity recorderActivity);
    void inject(RecorderService recorderService);
    void inject(TapeRecorderApp tapeRecorderApp);
}
