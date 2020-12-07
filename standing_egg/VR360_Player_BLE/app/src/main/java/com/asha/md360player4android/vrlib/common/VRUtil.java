package com.asha.md360player4android.vrlib.common;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.view.Surface;

public class VRUtil {

    private static float[] mTmp = new float[16];

    public static void sensorRotationVector2Matrix(SensorEvent event, int rotation, float[] output) {
        float[] values = event.values;
        switch (rotation){
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:  /*Notice: not supported for ROTATION_180! */
                SensorManager.getRotationMatrixFromVector(output, values);
                break;
            case Surface.ROTATION_90:
                SensorManager.getRotationMatrixFromVector(mTmp, values);

                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, output);
                break;
            case Surface.ROTATION_270:
                SensorManager.getRotationMatrixFromVector(mTmp, values);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, output);
                break;
        }

        Matrix.rotateM(output, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    }

    public static void sensorRotationVector2Matrix( float[] q,float[] output) {
        //fRotationMatrixFromQuaternion(output, q);
        fRotationMatrixFromQuaternion(mTmp, q);
        remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, output);
        Matrix.rotateM(output, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    }
   static public float a, x, y, z;
   static float[] rv = new float[4];

    private static final float[] mTempMatrix = new float[16];
    public static boolean remapCoordinateSystem(float[] inR, int X, int Y,
                                                float[] outR)
    {
        if (inR == outR) {
            final float[] temp = mTempMatrix;
            synchronized(temp) {
                // we don't expect to have a lot of contention
                if (remapCoordinateSystemImpl(inR, X, Y, temp)) {
                    final int size = outR.length;
                    for (int i=0 ; i<size ; i++)
                        outR[i] = temp[i];
                    return true;
                }
            }
        }
        return remapCoordinateSystemImpl(inR, X, Y, outR);
    }

    public static void notNull(Object object, String error){
        if (object == null) throw new RuntimeException(error);
    }

    private static boolean remapCoordinateSystemImpl(float[] inR, int X, int Y, float[] outR)
    {
        /*
         * X and Y define a rotation matrix 'r':
         *
         *  (X==1)?((X&0x80)?-1:1):0    (X==2)?((X&0x80)?-1:1):0    (X==3)?((X&0x80)?-1:1):0
         *  (Y==1)?((Y&0x80)?-1:1):0    (Y==2)?((Y&0x80)?-1:1):0    (Y==3)?((X&0x80)?-1:1):0
         *                              r[0] ^ r[1]
         *
         * where the 3rd line is the vector product of the first 2 lines
         *
         */

        final int length = outR.length;
        if (inR.length != length)
            return false;   // invalid parameter
        if ((X & 0x7C)!=0 || (Y & 0x7C)!=0)
            return false;   // invalid parameter
        if (((X & 0x3)==0) || ((Y & 0x3)==0))
            return false;   // no axis specified
        if ((X & 0x3) == (Y & 0x3))
            return false;   // same axis specified

        // Z is "the other" axis, its sign is either +/- sign(X)*sign(Y)
        // this can be calculated by exclusive-or'ing X and Y; except for
        // the sign inversion (+/-) which is calculated below.
        int Z = X ^ Y;

        // extract the axis (remove the sign), offset in the range 0 to 2.
        final int x = (X & 0x3)-1;
        final int y = (Y & 0x3)-1;
        final int z = (Z & 0x3)-1;

        // compute the sign of Z (whether it needs to be inverted)
        final int axis_y = (z+1)%3;
        final int axis_z = (z+2)%3;
        if (((x^axis_y)|(y^axis_z)) != 0)
            Z ^= 0x80;

        final boolean sx = (X>=0x80);
        final boolean sy = (Y>=0x80);
        final boolean sz = (Z>=0x80);

        // Perform R * r, in avoiding actual muls and adds.
        final int rowLength = ((length==16)?4:3);
        for (int j=0 ; j<3 ; j++) {
            final int offset = j*rowLength;
            for (int i=0 ; i<3 ; i++) {
                if (x==i)   outR[offset+i] = sx ? -inR[offset+0] : inR[offset+0];
                if (y==i)   outR[offset+i] = sy ? -inR[offset+1] : inR[offset+1];
                if (z==i)   outR[offset+i] = sz ? -inR[offset+2] : inR[offset+2];
            }
        }
        if (length == 16) {
            outR[3] = outR[7] = outR[11] = outR[12] = outR[13] = outR[14] = 0;
            outR[15] = 1;
        }
        return true;
    }


   static void fRotationMatrixFromQuaternion(float R[], float[] q )
    {
        float f2q;
        float f2q0q0, f2q0q1, f2q0q2, f2q0q3;
        float f2q1q1, f2q1q2, f2q1q3;
        float f2q2q2, f2q2q3;
        float f2q3q3;
// calculate products
        f2q = 2.0F * q[0];
        f2q0q0 = f2q * q[0];
        f2q0q1 = f2q * q[1];
        f2q0q2 = f2q * q[2];
        f2q0q3 = f2q * q[3];
        f2q = 2.0F * q[1];
        f2q1q1 = f2q * q[1];
        f2q1q2 = f2q * q[2];
        f2q1q3 = f2q * q[3];
        f2q = 2.0F * q[2];
        f2q2q2 = f2q * q[2];
        f2q2q3 = f2q * q[3];
        f2q3q3 = 2.0F * q[3] * q[3];
// calculate the rotation matrix assuming the quaternion is normalized
        R[0] = f2q0q0 + f2q1q1 - 1.0F;
        R[1] = f2q1q2 + f2q0q3;
        R[2] = f2q1q3 - f2q0q2;
        R[3] = 0;
        R[4] = f2q1q2 - f2q0q3;
        R[5] = f2q0q0 + f2q2q2 - 1.0F;
        R[6] = f2q2q3 + f2q0q1;
        R[7] = 0;
        R[8] = f2q1q3 + f2q0q2;
        R[9] = f2q2q3 - f2q0q1;
        R[10] = f2q0q0 + f2q3q3 - 1.0F;
        R[11] =  R[12] = R[13] = R[14]  = 0.0f;
        R[15] = 1.0f;
        return;
    }
}

