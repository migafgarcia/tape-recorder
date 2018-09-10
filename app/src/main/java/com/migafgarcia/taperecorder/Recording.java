package com.migafgarcia.taperecorder;

public class Recording {

    private String title, path;
    private long size, duration;

    private Recording(Builder builder) {
        title = builder.title;
        path = builder.path;
        size = builder.size;
        duration = builder.duration;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String title;
        private String path;
        private long size;
        private long duration;

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

        public Recording build() {
            return new Recording(this);
        }
    }
}
