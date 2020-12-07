
/**
  ******************************************************************************
  * @file    parameters.h
  * @author  Taehoon Moon
  * @version -
  * @date    07-January-2016
  * @brief   Contains all parameters for the motion and peak counting algorithms.
  ******************************************************************************
  * 
  ******************************************************************************
  */
#ifndef __PARAMETERS_H__
#define __PARAMETERS_H__

//#include "sespDefinitions.h" // Configure the usage of USE_KNN  or USE_SVM

// --------------------------------------
// General
// --------------------------------------
#define TRUE_ 1
#define FALSE_ 0
#define NULL 0

// --------------------------------------
// Motion Types 
// --------------------------------------
#define STANDING 0
#define	SLOW_WALKING 1
#define	WALKING  2
#define	RUNNING  3
#define	SITTING  4
#define JUMPING  5
#define PUSH_UP  6
#define SIT_UP  7
#define BUTTERFLY  8
#define BICEPS_CURL  9
#define SHOULDER_PRESS  10
#define CRUNCH  11
#define UPRIGHT_ROW  12
#define TOE_RAISE  13
#define PEC_FLY  14
#define CHEST_PRESS  15
#define INVALID  99


// --------------------------------------
// Sampling parameters
// --------------------------------------

// 
//Frequency(ms) = 1000 / SAMPLES_SEC
#if (USE_GESTURE == 1) && (USE_EXERCISE == 0)		// gesture mode
#define SAMPLES_SEC 50
#define SLIDING_WINDOW_SIZE 50
#elif (USE_GESTURE == 0) && (USE_EXERCISE == 1)		// exercise mode
#define SAMPLES_SEC 50
#define SLIDING_WINDOW_SIZE 50
#else // No mode
#define SAMPLES_SEC 1
#define SLIDING_WINDOW_SIZE 1
#endif
#define SAMPLES_PEAK_COUNTER 150//120//240
#define NO_OF_AXIS 3


// --------------------------------------
// Parameters for the KNN classifier
// --------------------------------------
	
#if (USE_GESTURE == 1) && (USE_EXERCISE == 0)		// gesture mode
//Amount of Train data
#define NO_OF_TRAINDATA 72
//Number of features
#define NO_OF_ATT 10
#elif (USE_GESTURE == 0) && (USE_EXERCISE == 1)		// exercise mode
//Amount of Train data
#define NO_OF_TRAINDATA 1737//1379//1156//625//268//284
//Number of features
#define NO_OF_ATT 21
#endif
//Number of classes (Motions)
#define NO_OF_CLASSES 20
#define K 11
#define LEARNING_RATE 0.005
#define LINE_MAX 10000


// --------------------------------------
// Peak detection related parameters
// --------------------------------------
#define MAGNITUDE_MODE 0
#define AXIS_MODE 1
#define MOTION_STATE_UPWARDS 0
#define MOTION_STATE_DOWNWARDS 1


//Motion Frequency Parameters
#define MIN_PERIOD_CURLS 75
#define MIN_PERIOD_WALKING 15
#define MIN_PERIOD_RUNNING 7
#define MIN_PERIOD_SHOULDERPRESS 125
#define MIN_PERIOD_UPRIGHTROW 150
#define MIN_PERIOD_TOERAISE 100

//Cuttof amplitude (Removing noise)
#define CUTOFF_CURLS 0.2
#define CUTOFF_WALKING 1.04
#define CUTOFF_RUNNING 1.3
#define CUTOFF_SHOULDERPRESS 1
#define CUTOFF_UPRIGHTROW -0.5
#define CUTOFF_TOERAISE -1

#endif
