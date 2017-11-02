package com.migafgarcia.taperecorder.recorder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.migafgarcia.taperecorder.R;
import com.migafgarcia.taperecorder.RecorderActivity;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecorderIntentService extends IntentService {

    private static final String TAG = RecorderIntentService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 666;

    private RecorderBroadcastReceiver receiver;

    private NotificationManager notificationManager;
    private MediaRecorder mediaRecorder;
    private int status;

    public final static String ACTION_TOGGLE = "ACTION_TOGGLE";
    public final static String ACTION_STATUS_REQUEST = "ACTION_STATUS_REQUEST";
    public final static String ACTION_STATUS_RESPONSE = "ACTION_STATUS_RESPONSE";

    public final static String PARAM_STATUS = "PARAM_STATUS";

    public final static int STATUS_NOT_RECORDING = 0;
    public final static int STATUS_RECORDING = 1;

    private RecorderThread recorderThread;

    public RecorderIntentService() {
        super("RecorderIntentService");
    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mediaRecorder = new MediaRecorder();
        status = STATUS_NOT_RECORDING;
        receiver = new RecorderBroadcastReceiver();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TOGGLE);
        registerReceiver(receiver, intentFilter);
        broadcastStatus();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onHandleIntent");
        }
    }

    private Notification buildNotification() {
        Notification.Builder builder = new Notification.Builder(RecorderIntentService.this);
        builder.setAutoCancel(false);
        builder.setTicker("Recording...");
        builder.setContentTitle("Title");
        builder.setContentText("Context text");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(this, RecorderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");
        return builder.build();
    }

    public void startRecording() {
        recorderThread = new RecorderThread();
        recorderThread.start();
        status = STATUS_RECORDING;
        Log.d(TAG, "Recording");
        notificationManager.notify(NOTIFICATION_ID, buildNotification());
        broadcastStatus();
    }

    public void stopRecording() {

        recorderThread.interrupt();
        status = STATUS_NOT_RECORDING;
        Log.d(TAG, "Stopped Recording");
        notificationManager.cancel(NOTIFICATION_ID);
        broadcastStatus();
    }

    public class RecorderBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(ACTION_TOGGLE)) {
                Log.d(TAG, "ACTION_TOGGLE");
                if(status == STATUS_RECORDING)
                    stopRecording();
                else
                    startRecording();
            }
            else if(action.equals(ACTION_STATUS_REQUEST)) {
                Log.d(TAG, "ACTION_STATUS_REQUEST");
                broadcastStatus();
            }
        }
    }

    private void broadcastStatus() {
        Intent i = new Intent();
        i.setAction(ACTION_STATUS_RESPONSE);
        i.putExtra(PARAM_STATUS, status);
        sendBroadcast(i);
    }



    private class RecorderThread extends Thread {

        @Override
        public void run() {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            mediaRecorder.start();

            while(true) {
                if (Thread.interrupted()) {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Sleep failed");
                }
            }

        }


    }
}
