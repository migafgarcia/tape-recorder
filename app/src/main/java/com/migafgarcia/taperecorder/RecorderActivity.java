package com.migafgarcia.taperecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.migafgarcia.taperecorder.recorder.RecorderIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.ACTION_STATUS_REQUEST;
import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.ACTION_STATUS_RESPONSE;
import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.ACTION_TOGGLE;
import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.PARAM_STATUS;
import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.STATUS_NOT_RECORDING;
import static com.migafgarcia.taperecorder.recorder.RecorderIntentService.STATUS_RECORDING;

public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();

    private RecorderActivityReceiver receiver;

    @BindView(R.id.status_txtview)
    TextView statusTxtView;
    @BindView(R.id.record_btn)
    Button recordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ButterKnife.bind(this);
    }

    private void updateUI(int status) {
        if(status == STATUS_NOT_RECORDING) {
            recordBtn.setText(R.string.action_start);
            statusTxtView.setText(R.string.status_stopped);
        }
        else if(status == STATUS_RECORDING){
            recordBtn.setText(R.string.action_stop);
            statusTxtView.setText(R.string.status_recording);
        }
        else {
            Toast.makeText(this, "Unknown status", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, RecorderIntentService.class));

        receiver = new RecorderActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATUS_RESPONSE);
        registerReceiver(receiver, intentFilter);

        Intent i = new Intent();
        i.setAction(ACTION_STATUS_REQUEST);
        sendBroadcast(i);
    }

    @OnClick(R.id.record_btn)
    public void onViewClicked() {
        Intent i = new Intent();
        i.setAction(ACTION_TOGGLE);
        sendBroadcast(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private class RecorderActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_STATUS_RESPONSE)){
                int status = intent.getIntExtra(PARAM_STATUS, STATUS_NOT_RECORDING);
                updateUI(status);
            }
        }
    }

}
