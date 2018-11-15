package com.migafgarcia.taperecorder


object AudioEngine {

    init {
        System.loadLibrary("native-lib")
    }

    external fun createRecordingEngine(): Boolean
    external fun destroyRecordingEngine()


    external fun startRecording()
    external fun stopRecording()
}