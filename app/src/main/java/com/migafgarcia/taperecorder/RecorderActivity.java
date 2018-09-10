package com.migafgarcia.taperecorder;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();

    @BindView(R.id.record_btn)
    FloatingActionButton recordBtn;

    @BindView(R.id.recordings_rv)
    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private RecorderService mService;
    private boolean mBound = false;


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

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
//        adapter = new MyAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);

        // TODO: 10-09-2018

        


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RecorderService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void updateUI() {
        RecorderStatus status = mService.getStatus();
        if(status == RecorderStatus.NOT_RECORDING) {
            recordBtn.setImageResource(R.drawable.ic_mic_black_24dp);
        }
        else if(status == RecorderStatus.RECORDING){
            recordBtn.setImageResource(R.drawable.ic_mic_off_black_24dp);
        }
    }

    @OnClick(R.id.record_btn)
    public void onViewClicked() {
        mService.record();
        updateUI();
    }

}
