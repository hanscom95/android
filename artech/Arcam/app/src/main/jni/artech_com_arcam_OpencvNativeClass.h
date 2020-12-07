/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using namespace cv;
using namespace std;

//////// DETECTION CONSTANTS ///////////
#define SCALE 4

/* Header for class artech_com_arcam_OpencvNativeClass */

#ifndef _Included_artech_com_arcam_OpencvNativeClass
#define _Included_artech_com_arcam_OpencvNativeClass
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     artech_com_arcam_OpencvNativeClass
 * Method:    convertGray
 * Signature: (JJ)I
 */
 int toGray(Mat img, Mat& gray);
 int means(Mat img, Mat& kmeansImg);

JNIEXPORT jint JNICALL Java_artech_com_arcam_OpencvNativeClass_convertGray
  (JNIEnv *, jclass, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
