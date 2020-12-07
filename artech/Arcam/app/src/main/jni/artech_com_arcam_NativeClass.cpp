#include <artech_com_arcam_NativeClass.h>

char* message = "This is message from JNI";

JNIEXPORT jstring JNICALL Java_artech_com_arcam_NativeClass_getMessageFromJNI
  (JNIEnv *env, jclass obj){
    return env->NewStringUTF(message);
  }

