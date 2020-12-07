/**
  ******************************************************************************
  * @file    motionFramework.h
  * @author  Jaiyoun Song
  * @version -
  * @date    21-March-2016
  * @brief   The motionFramework provides functions to create features from
						 the sensor data. This features are provided to an SVM
						 algorithm to perform motion detection.
  ******************************************************************************
  * 
  ******************************************************************************
  */

#ifndef __MOTIONFRAMEWORK_H__
#define __MOTIONFRAMEWORK_H__

#define USE_KNN			1
#define USE_SVM			0
#define USE_EXERCISE		1
#define USE_GESTURE		0

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <jni.h>
#include "featureData.h"
#include "Parameters.h"
#include "peakCounter.h"
//#include "sesp_api.h"
//#include "serpCommands.h"
//#include "sespDefinitions.h" // Configure the usage of USE_KNN  or USE_SVM

	//Main function to call, returns the current motion
	//return id:
	//STANDING - 0
	//SLOW_WALKING - 1
	//WALKING - 2
	//RUNNING - 3
	//SITTING - 4
	//JUMPING - 5
	//PUSH_UP - 6
	//SIT_UP - 7
	//BUTTERFLY - 8
	//ALTERNATED_BICEPS_CURL - 9
	//SHOULDER_PRESS - 10
	//CRUNCH - 11
	//UPRIGHT_ROW - 12
	//TOE_RAISE - 13
	//PEC_FLY - 14
	//CHEST_PRESS - 15

#if (USE_KNN == 1) && (USE_SVM == 0)		// KNN mode
typedef struct trainFeatureData {
	float featureValue[NO_OF_ATT];
	float distance;
} featureDataTrain;

typedef struct testFeatureData {
	float featureValue[NO_OF_ATT];
	float distance;
	jboolean used[NO_OF_ATT];
} featureDataTest;

typedef struct trainDataIndexer{
	float index;
	float distance;
} trainDataIndexer;

typedef struct normalizingData {
	float deviation;
	float mean;
	float max;
	float min;
} normalizingData;



typedef enum
{
    DAILY_LIFE = 0,
    WORKOUT = 1, 
	LEGACY = 2,
} MotionFrameworkMode;


	int runMotionDetection(float[][9]);

	void normalizeFeatures(float[]);
	void normalizeTrainData(void);

	int checkIfSensorOn(float input[]);

	void changeMode(MotionFrameworkMode newMode);
	
	//Sorting function
	void sortAll(float[],float[],float[],float[],float[],float[],float[],float[],float[],float[],float[]);

	//Calculate acceleration related features
	void calculateMagnitudeAccel(float[], float[][9]);
	void calculateMagnitudeGyro(float[], float[][9]);
	void calculateMagnitudeEuler(float[], float[][9]);

	void calculateMagnitudeAccelHoriz(float[], float[][3]);
	void calculateMagnitudeAccelLpf(float[], float[]);
	void calculateMagnitudeAccelBpf(float[], float[]);
	
	float calculateMax(float []);
	float calculateMin(float []);
	float calculateIQR(float []);
	float calculateStdDev(float []);

	// --------- CLASSIFIER RELATED ----------------------- //
	///Finds K nearest neighbors and predicts class according to algorithm used.
	int runKNN(featureDataTest);

	//Reads the training data into the list.
	void readTrainData(void);

	//Utility function to read line by line from a file.
	int GetLine(char *line, int max, FILE *fp);

#elif (USE_KNN == 0) && (USE_SVM == 1)		// SVM mode
typedef struct normalizingData {
	float deviation;
	float mean;
} normalizingData;

	int runMotionDetection(float[][9]);

	void normalizeFeatures(float[]);
	void normalizeTrainData(void);
	
	//Sorting function
	void sortAll(float[],float[],float[],float[],float[],float[]);

	//Calculate acceleration related features


/****************************************************************************************************
 * @fn      calculateXXX
 * @brief calculate motion features
 * @param [] akwejrlkawjefl
 * @return motion features
 ***************************************************************************************************/

	
	void calculateMagnitudeAccelTotal(float[], float[][3]);
	void calculateMagnitudeAccelHoriz(float[], float[][3]);
	void calculateMagnitudeAccelLpf(float[], float[]);
	void calculateMagnitudeAccelBpf(float[], float[]);

	float calculateMin(float []);

	float calculateMaxMagnitudeAccel(float []);
	float calculateMinMagnitudeAccel(float []);
	float calculateStdDevMagnitudeAccel(float []);
	float calculateMeanMagnitudeAccel(float []);
	float calculateIQRMagnitudeAccel(float []);
	
	float calculateMaxAccelX(float []);
	float calculateMinAccelX(float []);
	float calculateStdDevAccelX(float []);
	float calculateIQRAccelX(float []);
	
	float calculateMaxAccelY(float []);
	float calculateMinAccelY(float []);
	float calculateStdDevAccelY(float []);
	float calculateIQRAccelY(float []);

	float calculateMaxAccelZ(float []);
	float calculateMinAccelZ(float []);
	float calculateStdDevAccelZ(float []);
	float calculateIQRAccelZ(float []);

	float calculateMaxMagnitudeAccelLpf(float []);
	float calculateStdDevMagnitudeAccelLpf(float []);
	float calculateIQRMagnitudeAccelLpf(float []);

	float calculateMaxMagnitudeAccelBpf(float []);
	float calculateStdDevMagnitudeAccelBpf(float []);
	float calculateIQRMagnitudeAccelBpf(float []);


	//Reads the training data into the list.
	void readTrainData(void);
	void readTrainDataSVM(void);
	

static char cmd[200];
static char* trainedModelParsing;
static char* tempParse;
static char* readLineTemp;

typedef unsigned char bool;
#ifndef true
	#define true 1
#endif
#ifndef false
	#define false 0
#endif

enum { C_SVC, NU_SVC, ONE_CLASS, EPSILON_SVR, NU_SVR };	/* svm_type */
enum { LINEAR, POLY, RBF, SIGMOID, PRECOMPUTED }; /* kernel_type */

typedef struct svm_parameter
{
	int svm_type;
	int kernel_type;
	int degree;	/* for poly */
	double gamma;	/* for poly/rbf/sigmoid */
	double coef0;	/* for poly/sigmoid */

					/* these are for training only */
	double cache_size; /* in MB */
	double eps;	/* stopping criteria */
	double C;	/* for C_SVC, EPSILON_SVR and NU_SVR */
	int nr_weight;		/* for C_SVC */
	int *weight_label;	/* for C_SVC */
	double* weight;		/* for C_SVC */
	double nu;	/* for NU_SVC, ONE_CLASS, and NU_SVR */
	double p;	/* for EPSILON_SVR */
	int shrinking;	/* use the shrinking heuristics */
	int probability; /* do probability estimates */
} svm_parameter;

//
// svm_model
// 
typedef struct svm_model
{
	struct svm_parameter param;	/* parameter */
	int nr_class;		/* number of classes, = 2 in regression/one class svm */
	int l;			/* total #SV */
	struct svm_node **SV;		/* SVs (SV[l]) */
	double **sv_coef;	/* coefficients for SVs in decision functions (sv_coef[k-1][l]) */
	double *rho;		/* constants in decision functions (rho[k*(k-1)/2]) */
	double *probA;		/* pariwise probability information */
	double *probB;
	int *sv_indices;        /* sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set */

							/* for classification only */

	int *label;		/* label of each class (label[k]) */
	int *nSV;		/* number of SVs for each class (nSV[k]) */
					/* nSV[0] + nSV[1] + ... + nSV[k-1] = l */
					/* XXX */
	int free_sv;		/* 1 if svm_model is created by svm_load_model*/
						/* 0 if svm_model is created by svm_train */
	int test;
} svm_model;
	
typedef struct svm_node
{
	int index;
	double value;
} svm_node;
	
svm_model *svm_load_model(void);

bool read_model_header(svm_model* model);
void parsing(char* cmd, char* temp);
static char* readLine(char* trainedModel);
int predict(char* inputData);
void svm_get_labels(const svm_model *model, int* label);
void exit_input_error(int line_num);
double svm_predict_probability(
	svm_model *model, const svm_node *x, double *prob_estimates);
double svm_predict( svm_model *model, const svm_node *x);
double svm_predict_values(svm_model *model, const svm_node *x, double* dec_values);
static double sigmoid_predict(double decision_value, double A, double B);
static void multiclass_probability(int k, double **r, double *p);
int svm_get_svm_type(const svm_model *model);
int svm_get_nr_class(const svm_model *model);
double dot(const svm_node *px, const svm_node *py);
double k_function(const svm_node *x, const svm_node *y,
	const svm_parameter param);

#endif

#endif
