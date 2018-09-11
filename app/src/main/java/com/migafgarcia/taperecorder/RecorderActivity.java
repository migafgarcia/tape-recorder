package com.migafgarcia.taperecorder;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.migafgarcia.taperecorder.models.Recording;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();

    @BindView(R.id.record_btn)
    FloatingActionButton recordBtn;

    @BindView(R.id.recordings_rv)
    RecyclerView recyclerView;

    private RecordingsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private RecorderService mService;
    private boolean mBound = false;

    private AppDatabase db;


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

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tape-recorder-db").build();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecordingsAdapter();
        recyclerView.setAdapter(adapter);

        getRecordings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RecorderService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
        db.recordingDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordings -> {
                    Log.d(TAG, Arrays.asList(recordings).toString());
                    adapter.update(recordings);
                });
    }

}
