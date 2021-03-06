/*
Copyright (c) 2013, 2014, Freescale Semiconductor, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
 * Neither the name of Freescale Semiconductor, Inc. nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL FREESCALE SEMICONDUCTOR, INC. BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package demo.sensors.se.com.ssdemo;

import demo.sensors.se.com.ssdemo.MainActivity.GuiState;

/**
* This class acts as a funnel for collecting data from either local or remote sensors.  
* ALL sensor data should be called via this class.  Only one instance of this class is present in the application.
* @author Michael Stanley
*/
public class DataSelector {
	MainActivity demo;
	SensorsWrapper choice=null;
	SensorsWrapper stopped = null;
	SensorsWrapper fixed = null;
	DemoQuaternion zeroPosition = null;
	DemoQuaternion workingQuaternion1 = null;
	DemoQuaternion workingQuaternion2 = null;
	DemoQuaternion workingQuaternion3 = null;
	float myBoxRotation;
	
	DataSelector(MainActivity demo) {
		this.demo=demo;
		myBoxRotation=0;
		zeroPosition = new DemoQuaternion();
		stopped = new SensorsWrapper(demo);
		fixed = new SensorsWrapper(demo);
		stopped.setSensorDescriptions("Placeholder for STOPPED sensors");
		fixed.setSensorDescriptions("Placeholder for FIXED sensors");
		choice=stopped;
		workingQuaternion1 = new DemoQuaternion();
		workingQuaternion2 = new DemoQuaternion();
		workingQuaternion3 = new DemoQuaternion();
	}
	public void updateSelection() {
		switch (demo.dataSource) {
		case LOCAL: choice=demo.localSensors; break;
		case WIFI:   //choice=demo.wifi;
			 break;
		case STOPPED: choice=this.stopped; break;
		case FIXED: choice=this.fixed; break;
		default:
			// no change
		}				
	}

	synchronized void adjustForZero(RotationVector rv, DemoQuaternion q) {
		if (demo.zeroPending) {
			zeroPosition.set(q);
			zeroPosition.reverse();
			demo.zeroPending = false;
			demo.zeroed = true;
			rv.setIdentity();
		} else if (demo.zeroed) {
			workingQuaternion1.eqPxQ(zeroPosition, q);
			rv.computeFromQuaternion(workingQuaternion1, MyUtils.AngleUnits.DEGREES);
		} else {
			rv.computeFromQuaternion(q, MyUtils.AngleUnits.DEGREES);
		}
	}

	synchronized void getData(RotationVector rv, int screenRotation) {
		updateSelection();
		switch (demo.dataSource) {
		case LOCAL:
			demo.localSensors.computeQuaternion(workingQuaternion2, demo.algorithm);
			rv.computeFromQuaternion(workingQuaternion2, MyUtils.AngleUnits.DEGREES);
			break;
		case WIFI:
			demo.computeQuaternion(workingQuaternion2, demo.algorithm);
			if (demo.dualModeRequired()) {
				workingQuaternion3.set(demo.localSensors.quaternion());
				workingQuaternion3.reverse();
				workingQuaternion1.eqPxQ(workingQuaternion3, workingQuaternion2);
				adjustForZero(rv, workingQuaternion1);
			} else {
				adjustForZero(rv, workingQuaternion2);
			}
			
			
			break;			
		case STOPPED:
			if (demo.guiState == GuiState.DEVICE) {
				rv.set(MyUtils.AngleUnits.DEGREES, 0, 0, 0, 1);
			} else {
				rv.set(MyUtils.AngleUnits.DEGREES, 0, 0, 0, 1);			   
			}
			break;
		case FIXED:
		default:
			if (demo.guiState == GuiState.DEVICE) {
				rv.set(MyUtils.AngleUnits.DEGREES, myBoxRotation, 0.1f, 0.1f, 0.1f);
				myBoxRotation += 1.0f;
			} else {
				if (screenRotation==0) {
					rv.set(MyUtils.AngleUnits.DEGREES, myBoxRotation, 0.0f, 1.0f, 0.0f);
					myBoxRotation += 0.2f;			   
				} else {
					rv.set(MyUtils.AngleUnits.DEGREES, myBoxRotation, 1.0f, 0.0f, 0.0f);
					myBoxRotation -= 0.2f;			   
				}
			}
			break;
		}
		return;
	}



}
