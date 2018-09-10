package com.migafgarcia.taperecorder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * mgarcia
 * 10-09-2018
 * DCC/FCUP
 */
public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingViewHolder>{


    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecordingViewHolder extends RecyclerView.ViewHolder{

        TextView title, size, duration;
        Button play;

        public RecordingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
