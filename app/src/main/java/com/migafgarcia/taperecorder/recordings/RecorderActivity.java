package com.migafgarcia.taperecorder.recordings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.migafgarcia.taperecorder.R;
import com.migafgarcia.taperecorder.TapeRecorderApp;
import com.migafgarcia.taperecorder.database.AppDatabase;
import com.migafgarcia.taperecorder.models.Recording;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();

    @BindView(R.id.record_btn)
    FloatingActionButton recordBtn;

    @BindView(R.id.recordings_rv)
    RecyclerView recyclerView;

    private AppDatabase db;

    private RecordingsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private RecorderService mService;
    private boolean mBound = false;

    private MediaPlayer mediaPlayer;
    private Recording currentlyPlaying;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            RecorderService.RecorderServiceBinder binder = (RecorderService.RecorderServiceBinder) service;
            mService = binder.getService();
            mBound = true;
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ButterKnife.bind(this);

        Intent intent = new Intent(this, RecorderService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        db = ((TapeRecorderApp) getApplication()).appDatabase;

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new RecordingsAdapter(recording -> {
            if(recording == currentlyPlaying) {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
            else {
                currentlyPlaying = recording;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(recording.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        getRecordings();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer = new MediaPlayer();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
    }

    private void updateUI() {
        RecorderStatus status = mService.getStatus();
        if (status == RecorderStatus.NOT_RECORDING) {
            recordBtn.setImageResource(R.drawable.ic_mic_black_24dp);
        } else if (status == RecorderStatus.RECORDING) {
            recordBtn.setImageResource(R.drawable.ic_mic_off_black_24dp);
        }
        getRecordings();
    }

    @OnClick(R.id.record_btn)
    public void onViewClicked() {
        mService.record();
        updateUI();
    }

    private void getRecordings() {
        Log.d(TAG, "MAIN: " + Thread.currentThread().getName());
        Disposable disposable = db.recordingDao()
                .getAllFlowable()
                .map(new Function<List<Recording>, List<Recording>>() {
                    @Override
                    public List<Recording> apply(List<Recording> recordings) throws Exception {

                        ArrayList<Recording> result = new ArrayList<>(recordings);

                        for (Recording recording : recordings) {
                            File file = new File(recording.getPath());
                            if(!file.exists())
                                result.remove(recording);

                        }
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordings -> {
                    Log.d(TAG, Arrays.asList(recordings).toString());
                    adapter.update(recordings);
                });

    }

}
