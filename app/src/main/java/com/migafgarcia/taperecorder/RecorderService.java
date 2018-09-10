package com.migafgarcia.taperecorder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.media.MediaRecorder.MEDIA_ERROR_SERVER_DIED;
import static android.media.MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN;


public class RecorderService extends Service {
    private final static String TAG = RecorderService.class.getName();
    private final static int NOTIFICATION_ID = 666;
    private final static String RECORDING_STATUS = "RECORDING_STATUS";

    private final IBinder binder = new RecorderServiceBinder();

    private MediaRecorder mediaRecorder;

    private final static String CHANNEL_ID = "TRNC";

    private NotificationCompat.Builder mBuilder;

    private RecorderStatus currentStatus = RecorderStatus.NOT_RECORDING;
    private NotificationManagerCompat notificationManagerCompat;
    private MediaRecorder.OnInfoListener onInfoListener;
    private MediaRecorder.OnErrorListener onErrorListener;


    @Override
    public void onCreate() {
        super.onCreate();
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

    }

    @Override
    public void onDestroy() {
        if(currentStatus == RecorderStatus.RECORDING)
            stopRecording();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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

    private void startRecording() {
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
        currentStatus = RecorderStatus.RECORDING;
        Log.d(TAG, "Recording");
        notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        currentStatus = RecorderStatus.NOT_RECORDING;
        Log.d(TAG, "Stopped Recording");
        notificationManagerCompat.cancel(NOTIFICATION_ID);

    }

    public RecorderStatus getStatus() {
        return currentStatus;
    }

    public void record() {
        if(currentStatus == RecorderStatus.RECORDING) {
            stopRecording();
        }
        else {
            startRecording();
        }
    }

    public class RecorderServiceBinder extends Binder {
        RecorderService getService() {
            return RecorderService.this;
        }
    }

}
