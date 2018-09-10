package com.migafgarcia.taperecorder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.MediaRecorder.MEDIA_ERROR_SERVER_DIED;
import static android.media.MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN;


public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = RecorderActivity.class.getName();
    private final static int STATUS_NOT_RECORDING = 0;
    private final static int STATUS_RECORDING = 1;
    private final static int NOTIFICATION_ID = 666;
    private final static String RECORDING_STATUS = "RECORDING_STATUS";


    @BindView(R.id.status_txtview)
    TextView statusTxtView;
    @BindView(R.id.record_btn)
    Button recordBtn;

    private MediaRecorder mediaRecorder;

    private final static String CHANNEL_ID = "TRNC";

    private NotificationCompat.Builder mBuilder;

    private int currentStatus = STATUS_NOT_RECORDING;
    private NotificationManagerCompat notificationManagerCompat;
    private MediaRecorder.OnInfoListener onInfoListener;
    private MediaRecorder.OnErrorListener onErrorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ButterKnife.bind(this);
        mediaRecorder = new MediaRecorder();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fiber_manual_record_black_24dp)
                .setContentTitle(getString(R.string.status_recording))
                .setContentText(getString(R.string.context_text))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();

        onInfoListener = new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int info, int extra) {
                switch (info) {
                    case MEDIA_RECORDER_INFO_UNKNOWN:
                        Log.d(TAG, "MEDIA_RECORDER_INFO_UNKNOWN");
                        break;
                    case MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                        Log.d(TAG, "MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
                        break;
                    case MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                        Log.d(TAG, "MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED");
                        break;
                }
            }
        };

        onErrorListener = new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mediaRecorder, int error, int extra) {
                switch(error) {
                    case MEDIA_RECORDER_ERROR_UNKNOWN:
                        Log.d(TAG, "MEDIA_RECORDER_ERROR_UNKNOWN");
                        break;
                    case MEDIA_ERROR_SERVER_DIED:
                        Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                        break;
                }
            }
        };

        if(savedInstanceState != null){
            currentStatus = savedInstanceState.getInt(RECORDING_STATUS);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(currentStatus);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECORDING_STATUS, currentStatus);
        super.onSaveInstanceState(outState);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "tape-recorder-channel";
            String description = "asdasdasd";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

    @OnClick(R.id.record_btn)
    public void onViewClicked() {
        if(currentStatus == STATUS_RECORDING) {
            stopRecording();
        }
        else {
            startRecording();
        }
    }

    private String newFile() {
        File trDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Tape Recorder");

        if(!trDir.exists() && !trDir.mkdirs()) {
            Toast.makeText(this, "Error creating Tape Recorder directory", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Error creating Tape Recorder directory:" + trDir.getAbsolutePath());
            // TODO: 10-09-2018 handle this
        }

        File outputFile = new File(trDir.getAbsolutePath(), "tr-" + System.currentTimeMillis() + ".mp3");

        return outputFile.getAbsolutePath();
    }

    public void startRecording() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(newFile());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        mediaRecorder.start();
        updateUI(STATUS_RECORDING);
        currentStatus = STATUS_RECORDING;
        Log.d(TAG, "Recording");
        notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        updateUI(STATUS_NOT_RECORDING);
        currentStatus = STATUS_NOT_RECORDING;
        Log.d(TAG, "Stopped Recording");
        notificationManagerCompat.cancel(NOTIFICATION_ID);

    }


}
