cmake_minimum_required(VERSION 3.4.1)


set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -UNDEBUG")


add_library(hello-jni SHARED
            hello-jni.c
            motionDetection.c
            motionFramework.c
            peakCounter.c)

# Include libraries needed for hello-jni lib
target_link_libraries(hello-jni
                      android
                      log)
