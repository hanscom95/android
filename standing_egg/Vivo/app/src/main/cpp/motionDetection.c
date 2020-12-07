
/**
  ******************************************************************************
  * @file    motionDetection.c
  * @author  Thierry Durand
  * @version -
  * @date    2016/01/21
  * @brief   Functions making use of the motion framework.
  ******************************************************************************
  * 
  ******************************************************************************
  */

/*-------------------------------------------------------------------------------------------------*\
 |    I N C L U D E   F I L E S
\*-------------------------------------------------------------------------------------------------*/
#include "motionFramework.h"
#include "Parameters.h"
#include "peakCounter.h"
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "HelloJni", __VA_ARGS__))

/*-------------------------------------------------------------------------------------------------*\
 |    P U B L I C   V A R I A B L E S   D E F I N I T I O N S
\*-------------------------------------------------------------------------------------------------*/
//MotionDetection variables
int prevMotionBuffer[4] = {0};

//PeakDetection variables
extern int prevMotionBuffer[4];
int peakSampleCnt = 0;
int peaks = 0;
int peakMotion = 0;
float receiveBufferPeak[SAMPLES_PEAK_COUNTER][3];
#if (USE_GESTURE_BUTTON == 1)
sesp_bool_t prevSwitchState;
sesp_bool_t newSwitchState;
sesp_bool_t runMotion;
#endif
#if (USE_MOUSE_DRAWING_BUTTON == 1)
sesp_bool_t mouseClickState;
#endif

/*-------------------------------------------------------------------------------------------------*\
 |    P U B L I C     F U N C T I O N S
\*-------------------------------------------------------------------------------------------------*/
/****************************************************************************************************
 * @fn      changeMotionFrameworkMode
 * @brief changes the Mode of the Motion Framework
 * @param newMode The new mode that the Motion Framework will be switched to
 * @return void
 ***************************************************************************************************/
#if (USE_KNN == 1) && (USE_SVM == 0)
void changeMotionFrameworkMode(MotionFrameworkMode newMode)
{
	changeMode(newMode);
}
#else
void changeMotionFrameworkMode(MotionFrameworkMode newMode)
{
	// Not available yet with SVM
}
#endif

/****************************************************************************************************
 * @fn      detect_motion
 * @brief detect motion
 * @param x	Updated accelerometer value on x axis
 * @param y Updated accelerometer value on y axis
 * @param z Updated accelerometer value on z axis
 * @return current motion
 ***************************************************************************************************/
int detect_motion(float accelX, float accelY, float accelZ, float gyroX, float gyroY, float gyroZ, float roll, float pitch, float yaw)
{
	static float receiveBuffer[SAMPLES_SEC][9];
	static int motionSampleCnt = 0;
	static int motion = 99;
	int m = 0;
	
#if (USE_GESTURE == 1) && (USE_EXERCISE == 0)	&& (USE_GESTURE_BUTTON == 0)// gesture mode
	
	motion = runMotionDetection(receiveBuffer);
	
	for(m=1;m<SAMPLES_SEC;m++)
	{
		receiveBuffer[m-1][0] = receiveBuffer[m][0];
		receiveBuffer[m-1][1] = receiveBuffer[m][1];
		receiveBuffer[m-1][2] = receiveBuffer[m][2];
	}
	
	receiveBuffer[SAMPLES_SEC-1][0] = accelX;
	receiveBuffer[SAMPLES_SEC-1][1] = accelY;
	receiveBuffer[SAMPLES_SEC-1][2] = accelZ;
	motionSampleCnt++;

#elif (USE_GESTURE == 1) && (USE_EXERCISE == 0)	&&	(USE_GESTURE_BUTTON == 1)// gesture mode with button
	
	prevSwitchState = newSwitchState;
	newSwitchState = sespButtonIsTriggered(BUTTON_1);
	
	#if (USE_MOUSE_DRAWING_BUTTON == 1)
	mouseClickState = sespButtonIsTriggered(SWITCH_1);
	if(mouseClickState)
	{
		//Gesture 7 is defined as mouse click
		//If drawing is performed gesture input is not allowed
		return 7;
	}	
	#endif
	
	receiveBuffer[SAMPLES_SEC-1][0] = accelX;
	receiveBuffer[SAMPLES_SEC-1][1] = accelY;
	receiveBuffer[SAMPLES_SEC-1][2] = accelZ;
	
	for(m=1;m<SAMPLES_SEC;m++)
	{
		receiveBuffer[m-1][0] = receiveBuffer[m][0];
		receiveBuffer[m-1][1] = receiveBuffer[m][1];
		receiveBuffer[m-1][2] = receiveBuffer[m][2];
	}	
		
	if(!prevSwitchState && newSwitchState)
	{
		runMotion = true;
	}		
	if(runMotion)
	{		
		motion = runMotionDetection(receiveBuffer);	
		if(motion != 0)
		{
			runMotion = false;
		}
	}
	
	
#elif (USE_GESTURE == 0) && (USE_EXERCISE == 1)		// exercise mode
	
	if (motionSampleCnt == SAMPLES_SEC)
		{
			motionSampleCnt = SAMPLES_SEC - SLIDING_WINDOW_SIZE;
			
			motion = runMotionDetection(receiveBuffer);
			prevMotionBuffer[0] = prevMotionBuffer[1];  
			prevMotionBuffer[1] = prevMotionBuffer[2]; 
			prevMotionBuffer[2] = prevMotionBuffer[3]; 		
			prevMotionBuffer[3] = motion;
			
			//Only move sensor data if there is a sliding window set
			if (SAMPLES_SEC - SLIDING_WINDOW_SIZE != 0)
				{			
					for (m = 0; m < SAMPLES_SEC - SLIDING_WINDOW_SIZE; m++)
					{
						receiveBuffer[m][0] = receiveBuffer[m + SLIDING_WINDOW_SIZE][0];
						receiveBuffer[m][1] = receiveBuffer[m + SLIDING_WINDOW_SIZE][1];
						receiveBuffer[m][2] = receiveBuffer[m + SLIDING_WINDOW_SIZE][2];
						receiveBuffer[m][3] = receiveBuffer[m + SLIDING_WINDOW_SIZE][3];
						receiveBuffer[m][4] = receiveBuffer[m + SLIDING_WINDOW_SIZE][4];
						receiveBuffer[m][5] = receiveBuffer[m + SLIDING_WINDOW_SIZE][5];
						receiveBuffer[m][6] = receiveBuffer[m + SLIDING_WINDOW_SIZE][6];
						receiveBuffer[m][7] = receiveBuffer[m + SLIDING_WINDOW_SIZE][7];
						receiveBuffer[m][8] = receiveBuffer[m + SLIDING_WINDOW_SIZE][8];
					}
				}
		}
		
		else
			{
				receiveBuffer[motionSampleCnt][0] = accelX;
				receiveBuffer[motionSampleCnt][1] = accelY;
				receiveBuffer[motionSampleCnt][2] = accelZ;
				receiveBuffer[motionSampleCnt][3] = gyroX;
				receiveBuffer[motionSampleCnt][4] = gyroY;
				receiveBuffer[motionSampleCnt][5] = gyroZ;
				receiveBuffer[motionSampleCnt][6] = roll;
				receiveBuffer[motionSampleCnt][7] = pitch;
				receiveBuffer[motionSampleCnt][8] = yaw;
				motionSampleCnt++;
			}
			
#endif
			return motion;
}



/*****************************************************************************************************
 * @fn      detect_peaks
 * @brief detect peaks
 * @param x	Updated accelerometer value on x axis
 * @param y Updated accelerometer value on y axis
 * @param z Updated accelerometer value on z axis
 * @param peaksOutput Pointer on number of peaks output
 * @param peakMotionOutput Pointer on type of motion output
 * @return current motion
 ***************************************************************************************************/
void detect_peaks(float x, float y, float z, int* peaksOutput, int* peakMotionOutput)
{
//	LOGI("detect_peaks(x,y,z,step,motion): %f %f %f", x, y, z);
	if (peakSampleCnt == SAMPLES_PEAK_COUNTER)
  {
		int i = 3;
		int classCount[NO_OF_CLASSES] = {0};

		while (i >= 0)
		{
			int temp = prevMotionBuffer[i];
			classCount[temp]++;
			i--;
		}

		if(prevMotionBuffer[3] == STANDING && prevMotionBuffer[2] != STANDING)
		{
			peaks = peaks + runPeakDetection(receiveBufferPeak, prevMotionBuffer[2]);
			peakMotion = prevMotionBuffer[2];
		}

		else if(prevMotionBuffer[3] == STANDING && prevMotionBuffer[2] == STANDING && prevMotionBuffer[1] != STANDING)
		{
			peaks = peaks + runPeakDetection(receiveBufferPeak, prevMotionBuffer[1]);
			peakMotion = prevMotionBuffer[1];
		}

		else if(prevMotionBuffer[3] == STANDING && prevMotionBuffer[2] == STANDING && prevMotionBuffer[1] == STANDING&& prevMotionBuffer[0] != STANDING)
		{
			peaks = peaks + runPeakDetection(receiveBufferPeak, prevMotionBuffer[0]);
			peakMotion = prevMotionBuffer[0];
		}
		else if(prevMotionBuffer[3] != STANDING && prevMotionBuffer[2] ==  prevMotionBuffer[1] && prevMotionBuffer[0] ==  prevMotionBuffer[1])
		{
			peaks = peaks + runPeakDetection(receiveBufferPeak, prevMotionBuffer[2]);
			peakMotion = prevMotionBuffer[2];
		}

		else
		{
			peaks = peaks + runPeakDetection(receiveBufferPeak, prevMotionBuffer[3]);
			peakMotion = prevMotionBuffer[3];
		}

		peakSampleCnt = 0;
	}
	
	else
  {
    receiveBufferPeak[peakSampleCnt][0] = x;
    receiveBufferPeak[peakSampleCnt][1] = y;
    receiveBufferPeak[peakSampleCnt][2] = z;
    peakSampleCnt++;
  }

//	LOGI("detect_peaks(peaks, peakMotion): %d %d", peaks, peakMotion);
	*peaksOutput = peaks;
	*peakMotionOutput = peakMotion;
	
}




