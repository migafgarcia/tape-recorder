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
        holder.duration.setText(Long.toString(recording.getDuration()) + " ms");
        holder.size.setText(humanReadableByteCount(recording.getSize(), true));

    }

    /**
     * https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param bytes
     * @param si
     * @return
     */
    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
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
