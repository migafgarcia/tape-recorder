//
// Created by mgarcia on 15-11-2018.
//

#include "RecordingEngine.h"
#include "../../../../oboe/src/common/OboeDebug.h"


RecordingEngine::RecordingEngine() {

}

RecordingEngine::~RecordingEngine() {

}

void RecordingEngine::setDeviceId(int32_t deviceId) {

}

void RecordingEngine::startRecording() {
    // To create a stream we use a stream builder. This allows us to specify all
    // the parameters for the stream prior to opening it
    oboe::AudioStreamBuilder builder;

    builder.setCallback(this)
            ->setDeviceId(mRecordingDeviceId)
            ->setDirection(oboe::Direction::Input)
            ->setSampleRate(mSampleRate)
            ->setChannelCount(mInputChannelCount)
            ->setAudioApi(mAudioApi)
            ->setFormat(mFormat)
            ->setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency);

    // Now that the parameters are set up we can open the stream
    oboe::Result result = builder.openStream(&recording_stream);
    if (result == oboe::Result::OK && recording_stream) {
        assert(recording_stream->getChannelCount() == mInputChannelCount);
//        assert(recording_stream->getSampleRate() == mSampleRate);
//        assert(recording_stream->getFormat() == mFormat);

        warnIfNotLowLatency(recording_stream);
    } else {
        LOGE("Failed to create recording stream. Error: %s",
             oboe::convertToText(result));
        return;
    }

    result = recording_stream->requestStart();
    if (result != oboe::Result::OK) {
        LOGE("Error starting stream. %s", oboe::convertToText(result));
    }
}

void RecordingEngine::stopRecording() {
    if(recording_stream) {
        recording_stream->close();
    }
}

oboe::DataCallbackResult
RecordingEngine::onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {
    LOGD("Read: %d", numFrames);
    return oboe::DataCallbackResult::Continue;
}

void RecordingEngine::onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error) {

}

void RecordingEngine::onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) {

}

bool RecordingEngine::setAudioApi(oboe::AudioApi) {
    return false;
}

bool RecordingEngine::isAAudioSupported(void) {
    return false;
}

/**
 * Warn in logcat if non-low latency stream is created
 * @param stream: newly created stream
 *
 */
void RecordingEngine::warnIfNotLowLatency(oboe::AudioStream *stream) {
    if (stream->getPerformanceMode() != oboe::PerformanceMode::LowLatency) {
        LOGW(
                "Stream is NOT low latency."
                "Check your requested format, sample rate and channel count");
    }
}
