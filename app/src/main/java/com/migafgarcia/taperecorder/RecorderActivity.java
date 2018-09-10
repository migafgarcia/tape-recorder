package com.migafgarcia.taperecorder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();

    @BindView(R.id.status_txtview)
    TextView statusTxtView;
    @BindView(R.id.record_btn)
    Button recordBtn;

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
            recordBtn.setText(R.string.action_start);
            statusTxtView.setText(R.string.status_stopped);
        }
        else if(status == RecorderStatus.RECORDING){
            recordBtn.setText(R.string.action_stop);
            statusTxtView.setText(R.string.status_recording);
        }
        else {
            Toast.makeText(this, "Unknown status", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.record_btn)
    public void onViewClicked() {
        mService.record();
        updateUI();
    }

}
