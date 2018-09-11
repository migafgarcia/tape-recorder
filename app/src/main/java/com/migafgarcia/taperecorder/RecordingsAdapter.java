package com.migafgarcia.taperecorder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.migafgarcia.taperecorder.models.Recording;

import java.util.ArrayList;
import java.util.List;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingViewHolder>{

    private ArrayList<Recording> recordings = new ArrayList<>();

    public void update(List<Recording> newRecordings) {
        recordings.clear();
        recordings.addAll(newRecordings);
        notifyDataSetChanged();
    }

    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recording_item, parent, false);
        return new RecordingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {
        Recording recording = recordings.get(position);

        holder.title.setText(recording.getTitle());

    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public class RecordingViewHolder extends RecyclerView.ViewHolder{

        TextView title, size, duration;
        Button play;

        public RecordingViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title_txtview);
            this.size = itemView.findViewById(R.id.size_txtview);
            this.duration = itemView.findViewById(R.id.duration_txtview);
            this.play = itemView.findViewById(R.id.play_btn);
        }
    }
}