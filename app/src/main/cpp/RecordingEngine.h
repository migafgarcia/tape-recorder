//
// Created by mgarcia on 15-11-2018.
//

#ifndef TAPE_RECORDER_RECORDINGENGINE_H
#define TAPE_RECORDER_RECORDINGENGINE_H
#include <jni.h>
#include <oboe/Oboe.h>
#include <future>
#include <vector>
#include <string>
#include <thread>

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

    oboe::AudioStream *recording_stream = nullptr;

    int32_t mRecordingDeviceId = oboe::kUnspecified;
    oboe::AudioFormat mFormat = oboe::AudioFormat::Float;
    int32_t mInputChannelCount = oboe::ChannelCount::Stereo;
    int32_t sample_rate = 44100;

    int32_t sockfd;

    void warnIfNotLowLatency(oboe::AudioStream *stream);

};


#endif //TAPE_RECORDER_RECORDINGENGINE_H
