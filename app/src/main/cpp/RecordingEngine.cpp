//
// Created by mgarcia on 15-11-2018.
//

#include "RecordingEngine.h"
#include "../../../../oboe/src/common/OboeDebug.h"
#include <unistd.h>
#include <sys/socket.h>
#include <linux/in.h>
#include <netinet/in.h>
#include <endian.h>
#include <arpa/inet.h>


RecordingEngine::RecordingEngine() {


}

RecordingEngine::~RecordingEngine() {

}

void RecordingEngine::setDeviceId(int32_t deviceId) {

}

void RecordingEngine::startRecording() {

    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    if(sockfd == 0) {
        LOGE("Socket creation failed");
        return;
    }

    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_port = htons(6666);

    // Convert IPv4 and IPv6 addresses from text to binary form
    if(inet_pton(AF_INET, "192.168.1.110", &address.sin_addr)<=0)
    {
        LOGE("Invalid address/ Address not supported ");
        return;
    }

    if (connect(sockfd, (struct sockaddr *)&address, sizeof(address)) < 0)
    {
        LOGE("\nConnection Failed \n");
        return ;
    }

    oboe::AudioStreamBuilder builder;

    builder.setCallback(this)
            ->setDeviceId(mRecordingDeviceId)
            ->setDirection(oboe::Direction::Input)
            ->setChannelCount(mInputChannelCount)
            ->setFormat(mFormat)
            ->setSampleRate(sample_rate)
            ->setContentType(oboe::ContentType::Speech)
            ->setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency);

    oboe::Result result = builder.openStream(&recording_stream);
    if (result == oboe::Result::OK && recording_stream) {

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

    //TODO(migafgarcia): remove this from here
    if(send(sockfd, audioData, static_cast<size_t>(oboeStream->getBytesPerFrame() * numFrames), 0) == -1) {
        LOGE("Error sending data, closing stream");
        //TODO(migafgarcia): close stream
    }
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
    oboe::AudioStreamBuilder builder;
    return builder.isAAudioSupported();
}


void RecordingEngine::warnIfNotLowLatency(oboe::AudioStream *stream) {
    if (stream->getPerformanceMode() != oboe::PerformanceMode::LowLatency) {
        LOGW(
                "Stream is NOT low latency."
                "Check your requested format, sample rate and channel count");
    }
}


