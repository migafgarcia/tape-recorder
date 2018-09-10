package com.migafgarcia.taperecorder;

import android.media.MediaRecorder;

import dagger.Module;
import dagger.Provides;

@Module
public class RecorderModule {

    @Provides
    public MediaRecorder mediaRecorder() {
        return new MediaRecorder();
    }

}
