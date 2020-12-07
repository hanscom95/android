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

import android.opengl.Matrix;
import android.util.Log;

/**
* A utility class managing quaternions.  This class does NOT implement all possible quaternion functions.
* Only those functions required by this application are implemented.
* Developers who are not familiar with quaternion math are encouraged to read "Quaternions
* and Rotation Sequences", by Jack B. Kuipers (Princeton University Press, 1999).
* @author Michael Stanley
*/
public class DemoQuaternion {
	public float q0, q1, q2, q3;
	float [] pm = null; // p Matrix
	float [] qv = null;  // q Vector
	float [] rv = null;  // r Vector

	public synchronized String toString()  {
		String str; // scratch string
		str = q0 + " " + q1 + " " + q2 + " " + q3 ;
		return(str);
	}
	public synchronized String toCsvString()  {
		String str; // scratch string
		str = q0 + ", " + q1 + ", " + q2 + ", " + q3 ;
		return(str);
	}
	DemoQuaternion() {
		initialize();
		this.setIdentity();
	}
	DemoQuaternion(float[] q) {
		initialize();
		this.set(q);
	}
	DemoQuaternion(DemoQuaternion q) {
		initialize();
		this.set(q);
	}
	void initialize() {
		pm = new float[16]; // p Matrix
		qv = new float[4];  // q Vector
		rv = new float[4];  // r Vector	
	}
	void computeFromAxisAngle(float angle, float[] axis) {
		assert(axis.length==3);
		q0= (float) Math.cos(angle/2);
		float sao2 = (float) Math.sin(angle/2);
		q1 = sao2*axis[0];
		q2 = sao2*axis[1];
		q3 = sao2*axis[2];
		
//		Log.e("WIFI", "quaternion:q0"+q0 +",q1:"+q1+",q2:"+q2+",q3:"+q3);
	}
	synchronized void toVector(float [] result) {
		result[0]=q0; result[1]=q1; result[2]=q2; result[3]=q3;
	}
	void computeFromRM(float[] rm) {
		// rm is a 3x3 rotation matrix
		assert(rm.length==9); // rm is assumed to be 3x3 matrix of form
		// rm is assumed to be 3x3 matrix of form
		// rm0 rm1 rm2
		// rm3 rm4 rm5
		// rm6 rm7 rm8
		// From p169 of Quaternionjs and Rotation Sequences by Jack B. Kuipers, we have
		q0 = 0.5f * (float) Math.sqrt((double) (rm[0] + rm[4] + rm[8] + 1));
		q1 = (rm[5] - rm[7]) / (4*q0);
		q2 = (rm[6] - rm[2]) / (4*q0);
		q3 = (rm[1] - rm[3]) / (4*q0);
		
		Log.d("WIFIQ", this.toCsvString());
	}
	
	
	synchronized void set(float [] q) {
		assert(q.length==4);
		q0=q[0];
		q1=q[1];
		q2=q[2];
		q3=q[3];
	}
	synchronized void set(DemoQuaternion q) {
		this.q0 = q.q0;
		this.q1 = q.q1;
		this.q2 = q.q2;
		this.q3 = q.q3;
	}
	synchronized void setIdentity() {
		// set to identity quaternion
		q0=1;
		q1=0;
		q2=0;
		q3=0;		
	}	
	
	void eqPxQ(DemoQuaternion p, DemoQuaternion q) {
		// This is a direct coding of equation 5.3 on page 109 of Quaternions and Rotation Sequences
		q.toVector(qv);  // this works
		//sQ[0]=q0; sQ[1]=q1; sQ[2]=q2; sQ[3]=q3;  // this does not

		// Matrix form is column-major as defined at http://developer.android.com/reference/android/opengl/Matrix.html
		pm[0] = pm[5] = pm[10] = pm[15] = p.q0;
		pm[1] = pm[11] = p.q1;
		pm[2] = pm[13] = p.q2;
		pm[3] = pm[6] = p.q3;
		pm[4] = pm[14] = -p.q1;
		pm[8] = pm[7] = -p.q2;
		pm[9] = pm[12] = -p.q3;
		Matrix.multiplyMV(rv, 0, pm, 0, qv, 0);
		this.set(rv);
	}

	void reverse() {
		q0=-q0;
	}
	void toRotationVector(RotationVector rv, MyUtils.AngleUnits unit) {
		float theta = (float) Math.acos(q0);
		float x, y, z, sinTheta;
		float scale_factor=1;
		if (q0==1) {
			x = y = z = (float) 0.0;
		} else {
			sinTheta = (float) Math.sin(theta);
			x = q1/sinTheta;
			y = q2/sinTheta;
			z = q3/sinTheta;
		}
		if (unit==MyUtils.AngleUnits.DEGREES) {
			scale_factor = MyUtils.degreesPerRadian;
		}
		rv.set(unit, 2*theta*scale_factor, x, y, z);
	}


}
