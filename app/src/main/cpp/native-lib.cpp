
#include <jni.h>
#include "RecordingEngine.h"
#include "../../../../oboe/src/common/OboeDebug.h"

static RecordingEngine *engine = nullptr;


extern "C"
JNIEXPORT bool JNICALL
Java_com_migafgarcia_taperecorder_AudioEngine_createRecordingEngine( JNIEnv* env, jobject ) {
    if (engine == nullptr) {
        engine = new RecordingEngine();
    }

    return (engine != nullptr);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_migafgarcia_taperecorder_AudioEngine_destroyRecordingEngine( JNIEnv* env, jobject ) {
    delete engine;
    engine = nullptr;
}




extern "C"
JNIEXPORT void JNICALL
Java_com_migafgarcia_taperecorder_AudioEngine_startRecording( JNIEnv* env, jobject ) {
    if (engine == nullptr) {
        LOGE(
                "Engine is null, you must call createEngine before calling this "
                "method");
        return;
    }

    engine->startRecording();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_migafgarcia_taperecorder_AudioEngine_stopRecording( JNIEnv* env, jobject ) {
    if (engine == nullptr) {
        LOGE(
                "Engine is null, you must call createEngine before calling this "
                "method");
        return;
    }

    engine->stopRecording();
}
