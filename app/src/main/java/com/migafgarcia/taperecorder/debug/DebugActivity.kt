package com.migafgarcia.taperecorder.debug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.migafgarcia.taperecorder.AudioEngine.createRecordingEngine
import com.migafgarcia.taperecorder.AudioEngine.destroyRecordingEngine
import com.migafgarcia.taperecorder.AudioEngine.startRecording
import com.migafgarcia.taperecorder.AudioEngine.stopRecording
import com.migafgarcia.taperecorder.R


class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
    }

    override fun onStart() {
        super.onStart()
        createRecordingEngine()
        startRecording()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        destroyRecordingEngine()
    }
}
