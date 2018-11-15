//
// Created by mgarcia on 15-11-2018.
//

#ifndef TAPE_RECORDER_RECORDINGENGINE_H
#define TAPE_RECORDER_RECORDINGENGINE_H
#include <jni.h>
#include <oboe/Oboe.h>

class RecordingEngine : public oboe::AudioStreamCallback {
public:
    RecordingEngine();
    ~RecordingEngine();
    void setDeviceId(int32_t deviceId);
    void startRecording();
    void stopRecording();

    oboe::DataCallbackResult onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames);

    void onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error);
    void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error);

    bool setAudioApi(oboe::AudioApi);
    bool isAAudioSupported(void);


private:
    bool is_recording = false;
    int32_t mRecordingDeviceId = oboe::kUnspecified;
    oboe::AudioFormat mFormat = oboe::AudioFormat::I16;
    int32_t mSampleRate = oboe::kUnspecified;
    int32_t mInputChannelCount = oboe::ChannelCount::Stereo;
    oboe::AudioStream *recording_stream = nullptr;
    oboe::AudioApi mAudioApi = oboe::AudioApi::AAudio;
    void warnIfNotLowLatency(oboe::AudioStream *stream);
};


#endif //TAPE_RECORDER_RECORDINGENGINE_H
