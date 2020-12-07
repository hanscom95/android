/**
  ******************************************************************************
  * @file    peakCounter.h
  * @author  Michael Vogl
  * @version -
  * @date    27-October-2015
  * @brief   TBD
  ******************************************************************************
  * 
  ******************************************************************************
  */


#ifndef __PEAKCOUNTER_H__
#define __PEAKCOUNTER_H__

#include "sglib.h"
//#include "sesp_api.h" /* malloc and free */
#include "Parameters.h"
#include "math.h"
#include "string.h"
#include "stdlib.h"

//typedef unsigned char bool;


typedef struct peak 
{
	float acc;
	int sampleNumber;
	char isTruePeak;
	struct peak *ptr_to_next;
	struct peak *ptr_to_previous;
} peak;



int runPeakDetection(float[][3], int);
int discoverAllPeaks(int, int, int, int, float, float[][NO_OF_AXIS], float[]);
void calculateAccelBpf(float[][NO_OF_AXIS],float[][NO_OF_AXIS],float[],float[]);


#endif	
