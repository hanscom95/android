/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
  ******************************************************************************
  * @file    hello-jni.c
  * @author  Taehoon Moon
  * @version -
  * @date    2016/01/21
  * @brief   Functions making use of the motion framework.
  ******************************************************************************
  * 
  ******************************************************************************
  */
#include <string.h>
#include <jni.h>
#include <android/sensor.h>
#include <android/looper.h>
#include <android/log.h>
#include <stdio.h>
#include "semoApi.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "HelloJni", __VA_ARGS__))
#define LOOPER_ID 1
#define SAMP_PER_SEC 100

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   hello-jni/app/src/main/java/com/example/hellojni/StepActivity.java
 */

ASensorEventQueue* sensorEventQueue;
ASensorManager* sensorManager;

const ASensor* accSensor;

float accelX = 0.;
float accelY = 0.;
float accelZ = 0.;

int peaksOutput;
int peakMotionOutput;



static int get_sensor_events(int fd, int events, void* data);


JNIEXPORT jstring JNICALL

Java_com_standingegg_acc_StepActivity_stringFromJNI( JNIEnv* env,
                                                      jobject thiz )
{
    LOGI("stringFromJNI");
    return (*env)->NewStringUTF(env,"Hello from JNI !");
}


void
Java_com_standingegg_acc_StepActivity_sensorValue( JNIEnv* env, jobject thiz ) {
    void* sensor_data = malloc(1000);

    LOGI("sensorValue() - ALooper_forThread()");

    ALooper* looper = ALooper_forThread();

    if(looper == NULL)
    {
        looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
    }

    sensorManager = ASensorManager_getInstance();

    accSensor = ASensorManager_getDefaultSensor(sensorManager, ASENSOR_TYPE_ACCELEROMETER);


    sensorEventQueue = ASensorManager_createEventQueue(sensorManager, looper, 3, get_sensor_events, sensor_data);

    ASensorEventQueue_enableSensor(sensorEventQueue, accSensor);

    //Sampling rate: 100Hz
    int a = ASensor_getMinDelay(accSensor);
    LOGI("min-delay: %d",a);

    a = ASensorEventQueue_setEventRate(sensorEventQueue, accSensor, 20000);

    LOGI("sensorValue() - START");
}



static int get_sensor_events(int fd, int events, void* data) {
    ASensorEvent event;
    //ASensorEventQueue* sensorEventQueue;
    while (ASensorEventQueue_getEvents(sensorEventQueue, &event, 1) > 0) {
        if(event.type == ASENSOR_TYPE_ACCELEROMETER) {
//            LOGI("Acc(x,y,z,t): %f %f %f", event.acceleration.x, event.acceleration.y, event.acceleration.z);
            accelX = event.acceleration.x/ASENSOR_STANDARD_GRAVITY;
            accelY = event.acceleration.y/ASENSOR_STANDARD_GRAVITY;
            accelZ = event.acceleration.z/ASENSOR_STANDARD_GRAVITY;

            int motion = detect_motion(accelX, accelY, accelZ, (float)0, (float)0, (float)0, (float)0, (float)0, (float)0);
            detect_peaks(accelX, accelY, accelZ, &peaksOutput, &peakMotionOutput);
        }

    }
    //should return 1 to continue receiving callbacks, or 0 to unregister
    return 1;
}

void Java_com_standingegg_acc_StepActivity_stopSensor(JNIEnv *env, jobject instance) {
    ASensorEventQueue_disableSensor(sensorEventQueue, accSensor);
    sensorEventQueue = ASensorManager_destroyEventQueue(sensorManager, sensorEventQueue);
}


JNIEXPORT jstring JNICALL

Java_com_standingegg_acc_StepActivity_accCallback( JNIEnv* env,
                                                    jobject thiz ) {
    char myCharPointer[100];
    sprintf(myCharPointer, "%d,%d", peaksOutput,peakMotionOutput);
    LOGI("peaksOutput : %d  peakMotionOutput : %d " , peaksOutput,peakMotionOutput);
    return (*env)->NewStringUTF(env, myCharPointer);
}