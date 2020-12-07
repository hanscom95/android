LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:\opencv-3.3.0-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := artech_com_arcam_autoScoringNativeClass.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyOpencvLibs
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_SRC_FILES := artech_com_arcam_NativeClass.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyLibs
include $(BUILD_SHARED_LIBRARY)