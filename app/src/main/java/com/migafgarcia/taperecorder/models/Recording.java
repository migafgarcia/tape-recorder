package com.migafgarcia.taperecorder.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "recordings")
public class Recording {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String title;
    private String path;
    private long size;
    private long duration;
    private long timestamp;

    public Recording(int uid, String title, String path, long size, long duration, long timestamp) {
        this.uid = uid;
        this.title = title;
        this.path = path;
        this.size = size;
        this.duration = duration;
        this.timestamp = timestamp;
    }

    private Recording(Builder builder) {
        title = builder.title;
        path = builder.path;
        size = builder.size;
        duration = builder.duration;
        timestamp = builder.timestamp;
    }

    public int getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getDuration() {
        return duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String title;
        private String path;
        private long size;
        private long duration;
        private long timestamp;

        private Builder() {
        }

        public Builder setTitle(String val) {
            title = val;
            return this;
        }

        public Builder setPath(String val) {
            path = val;
            return this;
        }

        public Builder setSize(long val) {
            size = val;
            return this;
        }

        public Builder setDuration(long val) {
            duration = val;
            return this;
        }

        public Builder setTimestamp(long val) {
            timestamp = val;
            return this;
        }

        public Recording build() {
            return new Recording(this);
        }
    }

    @Override
    public String toString() {
        return "Recording{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", timestamp=" + timestamp +
                '}';
    }
}
