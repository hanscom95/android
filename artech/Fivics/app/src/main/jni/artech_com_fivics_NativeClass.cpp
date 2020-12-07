#include <artech_com_fivics_NativeClass.h>

char* message = "This is message from JNI";

JNIEXPORT jstring JNICALL Java_artech_com_fivics_NativeClass_getMessageFromJNI
  (JNIEnv *env, jclass obj){
    return env->NewStringUTF(message);
  }

