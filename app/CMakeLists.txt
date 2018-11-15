
cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp/)

add_library( # Specifies the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp
        src/main/cpp/RecordingEngine.cpp
        src/main/cpp/RecordingEngine.h


        )


set(OBOE_DIR ../oboe)

add_subdirectory(${OBOE_DIR} ./oboe)

include_directories(${OBOE_DIR}/include)

target_link_libraries(native-lib log oboe)
