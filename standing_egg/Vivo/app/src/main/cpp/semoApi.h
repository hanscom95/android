/**
  ******************************************************************************
  * @file    semoAPI.h
  * @author  Thierry Durand
  * @version -
  * @date    2016/01/21
  * @brief   Standing Egg Motion Detection Library (SEMO)
  ******************************************************************************
	**/
	
#ifndef SEMO_API_H
#define SEMO_API_H
	
	/*-------------------------------------------------------------------------------------------------*\
 |    I N C L U D E   F I L E S
\*-------------------------------------------------------------------------------------------------*/
#include "motionFramework.h"

	
	

/*-------------------------------------------------------------------------------------------------*\
 |    P U B L I C   F U N C T I O N   D E C L A R A T I O N S
\*-------------------------------------------------------------------------------------------------*/
/****************************************************************************************************
 * @fn      detect_motion
 * @brief detect motion
 * @param x	Updated accelerometer value on x axis
 * @param y Updated accelerometer value on y axis
 * @param z Updated accelerometer value on z axis
 * @return current motion
 ***************************************************************************************************/
int detect_motion(float x, float y, float z, float, float, float, float, float, float);
void detect_peaks(float x, float y, float z, int* peaksOutput, int* peakMotionOutput);
void changeMotionFrameworkMode(MotionFrameworkMode newMode);
void sensor_test(float x, float y, float z, int* peaksOutput, int* peakMotionOutput);

#endif /* SEMO_API_H */
/*-------------------------------------------------------------------------------------------------*\
 |    E N D   O F   F I L E
\*-------------------------------------------------------------------------------------------------*/
