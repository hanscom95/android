package com.se.gait.utility;

import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.se.gait.activity.ChartingDemoActivity;
import com.se.gait.ble.BluetoothLeService;

import java.net.DatagramPacket;

import static com.se.gait.utility.StnEggPkt.*;

/**
 * Created by moon on 2016-08-09.
 */
public class Serf{
    private static float[] quat = new float[4];
    private static float accX, accY, accZ;
    private static float gyroX, gyroY, gyroZ;
    private static float magX, magY, magZ;
    private static float gaitX, gaitY, gaitZ, gaitDistance;
    private static int gaitStepCount;
    private static float gaitX_before, gaitY_before, gaitZ_before;
    private static float Mouse3D_euler1, Mouse3D_euler2, Mouse3D_euler3;
    private static int gaitZupt;
    private StnEggPacket build;

    public byte[] data1, data2, buffer,  startBuffer, endBuffer;
    public boolean confirmFalg = false;
    boolean flags = false;
    boolean seFlag = false;
    boolean meFlag = false;
    boolean epFlag = false;

    public Serf() {
    }

    public void prtocolBufferCheck(byte[] inPacket) {
        if(false) {
            byte[] data = inPacket;
            if (data != null && data.length > 0) {


                byte[] ddd = new byte[2];
                ddd[0] = data[data.length - 2];
                ddd[1] = data[data.length - 1];


                if (!"EP".equals(new String(ddd))) {
                    data2 = data;
                    flags = true;

                    return;
                }

                if (flags) {
                    Log.d("Serf", " 잘렸던 msg 이어 붙임 ");
                    byte[] datas = data;
                    data = new byte[data2.length + datas.length];

                    Log.d("Serf", " DATA LENGTH :" + data.length);
                    for (int i = 0; i < data.length; i++) {
                        if (i < data2.length)
                            data[i] = data2[i];
                        else
                            data[i] = datas[(i - data2.length)];
                    }
                    flags = false;
                }

                byte[] d = new byte[2];
                d[0] = data[0];
                d[1] = data[1];

                ULog.d("Serf", "d->>" + new String(d));

                String cha = new String(d);
                ULog.d("Serf", "d2->>" + cha);


                ULog.d("Serf", "# data ----->: " + byteArrayToHexString(data));
                //20160822 start
                try {
                    int contentStart = 0, contentEnd = 0;
                    boolean seFlag = false;
                    if (data != null && data.length > 0) {
                        Log.d("Serf", "# Read characteristic 20160817: data 있음");

                        for (int i = 0; i < data.length - 1; i++) {
                            //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                            if (data[i] == 'S' && data[i + 1] == 'E') {
                                //20160804 old version(vr) contentStart = i + 4;
                                contentStart = i + 2;
                                seFlag = true;
                            }
                            if (seFlag && data[i] == 'E' && data[i + 1] == 'P') {
                                contentEnd = i - 2;
                                seFlag = false;
                                break;
                            }
                        }
                        Log.d("Serf", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
                    }

                    byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                    System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
//                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);
                    build = StnEggPacket.parseFrom(resultBuffer);

                    Log.d("Serf", "getAcceleroDataG1 : " + build.getAcceleroDataG().getF1());
                    Log.d("Serf", "getAcceleroDataG2 : " + build.getAcceleroDataG().getF2());
                    Log.d("Serf", "getAcceleroDataG3 : " + build.getAcceleroDataG().getF3());



                    if(build != null) {
                        ULog.d("Serf", "build gait getF1: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF3());
                        ChartingDemoActivity.timeStamp = build.getTimeStamp();
                        ChartingDemoActivity.quat[0] = build.getKalman9AxesQuat().getF1();
                        ChartingDemoActivity.quat[1] = build.getKalman9AxesQuat().getF2();
                        ChartingDemoActivity.quat[2] = build.getKalman9AxesQuat().getF3();
                        ChartingDemoActivity.quat[3] = build.getKalman9AxesQuat().getF4();
                        ChartingDemoActivity.accX = build.getAcceleroDataG().getF1();
                        ChartingDemoActivity.accY = build.getAcceleroDataG().getF2();
                        ChartingDemoActivity.accZ = build.getAcceleroDataG().getF3();
                        ChartingDemoActivity.gyroX = build.getGyroDataDps().getF1();
                        ChartingDemoActivity.gyroY = build.getGyroDataDps().getF2();
                        ChartingDemoActivity.gyroZ = build.getGyroDataDps().getF3();
                        ChartingDemoActivity.magX = build.getMagDataUT().getF1();
                        ChartingDemoActivity.magY = build.getMagDataUT().getF2();
                        ChartingDemoActivity.magZ = build.getMagDataUT().getF3();
                        ChartingDemoActivity.gaitX = build.getGaitPos().getF1();
                        ChartingDemoActivity.gaitY = build.getGaitPos().getF2();
                        ChartingDemoActivity.gaitZ = build.getGaitPos().getF3();
                        ChartingDemoActivity.gaitStepCount = build.getGait().getZuptStepCounter();
                        ChartingDemoActivity.gaitDistance = build.getGait().getTotalDistance();
                        ChartingDemoActivity.gaitMotion = build.getGait().getZuptMotion();
                        ChartingDemoActivity.gaitZupt = build.getGait().getZupt();
                        ChartingDemoActivity.gaitErrorX = build.getGait().getErrorPositionX();
                        ChartingDemoActivity.gaitErrorY = build.getGait().getErrorPositionY();
                        ChartingDemoActivity.gaitErrorZ = build.getGait().getErrorPositionZ();
                        ChartingDemoActivity.gaitErrorPercentage = build.getGait().getErrorPercentage();
                        ULog.d("Serf", "gait gaitStepCount: " + ChartingDemoActivity.gaitStepCount+ "///gaitDistance: " + ChartingDemoActivity.gaitDistance + "///gaitMotion: " + ChartingDemoActivity.gaitMotion);
                        ULog.d("Serf", "gait getF1: " + ChartingDemoActivity.gaitX+ "///getF2: " + ChartingDemoActivity.gaitY + "///getF3: " + ChartingDemoActivity.gaitZ);
                        confirmFalg = true;
                    }else {
                        confirmFalg = false;
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }else if(false){
            byte[] data = inPacket;

            byte[] test = new byte[1];
           /* for (int i = 0; i < data.length; i++) {
                test[0] = data[i];
                Log.d("Serf", "REAL RECEIVE MESSAGE ==>" + new String(test));
                //ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + test);
            }*/


            for(int i = 0; i < inPacket.length-1; i++) {
//                if (!"EP".equals(new String(ddd))) {


                if (!flags && inPacket[i] == 'E' && inPacket[i + 1] == 'P') {
                    data2 = new byte[i + 2];
                    for (int j = 0; j < i + 2; j++) {
                        data2[j] = inPacket[j];
                    }
                    flags = true;
                }

                if (inPacket[i] == 'S' && inPacket[i + 1] == 'E') {
                    if(buffer != null) {
                        data1 = buffer;
                    }

                    Log.d("Serf", "SE ==>" + i);
                    buffer = new byte[inPacket.length - i];

                    for(int k = 0; k < buffer.length; k++) {
                        buffer[k] = inPacket[i+k];
                    }
                    seFlag = true;
                    return;
                }
            }

            int sInt = 0;
            int eInt = 0;

            if(data1 != null && data1.length > 0) {
                Log.d("Serf", "data1->>" + data1.length);
                Log.d("Serf", "S->>" + data1[0]);
                Log.d("Serf", "E->>" + data1[1]);
                sInt = data1.length;
            }

            if(data2 != null && data2.length > 0) {
                Log.d("Serf", "data2->>" + data2.length);
                Log.d("Serf", "E->>" + data2[data2.length - 2]);
                Log.d("Serf", "P->>" + data2[data2.length - 1]);
                eInt = data2.length;
            }

            ULog.d("Serf", "# data ----->: " + byteArrayToHexString(data));
            if(flags) {
                byte[] sample = new byte[sInt+eInt];
                for(int i = 0, j=0; i < sample.length; i++){
                    if(sInt > i) {
                        test[0] = data1[i];
                        Log.d("Serf", "REAL RECEIVE MESSAGE ==>" + new String(test));
                        sample[i] = data1[i];
                    }else {
                        test[0] = data2[j];
                        Log.d("Serf", "REAL RECEIVE MESSAGE ==>" + new String(test));
                        sample[i] = data2[j];
                        j++;
                    }
                }
                ULog.d("Serf", "# data length ----->: " + sample.length);
                ULog.d("Serf", "# sample ----->: " + byteArrayToHexString(sample));

                data = sample;
            }



            /*if (flags) {
                Log.d("Serf", " 잘렸던 msg 이어 붙임 ");
                byte[] datas = data;
                data = new byte[data2.length + datas.length];

                Log.d("Serf", " DATA LENGTH :" + data.length);
                for (int i = 0; i < data.length; i++) {
                    if (i < data2.length)
                        data[i] = data2[i];
                    else
                        data[i] = datas[(i - data2.length)];
                }
                flags = false;
            }

            byte[] d = new byte[2];
            d[0] = data[0];
            d[1] = data[1];

            Log.d("Serf", "d->>" + new String(d));

            String cha = new String(d);
            Log.d("Serf", "d2->>" + cha);*/


            //20160822 start
            try {
                int contentStart = 0, contentEnd = 0;
                if (data != null && data.length > 0) {
                    Log.d("Serf", "# Read characteristic 20160817: data 있음");

                    for (int i = 0; i < data.length - 1; i++) {
                        //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                        if (data[i] == 'S' && data[i + 1] == 'E') {
                            //20160804 old version(vr) contentStart = i + 4;
                            contentStart = i + 2;
                        }
                        if (data[i] == 'E' && data[i + 1] == 'P') {
                            //contentEnd = i - 2;
                            contentEnd = i - 1;
                            break;
                        }
                    }
                    ULog.d("Serf", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
                }

                byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                ULog.d("Serf", "resultBuffer : " + resultBuffer+ "///resultBuffer len : " + resultBuffer.length  + "//////dataArr : " + data + "//////dataArr len : " + data.length);
                ULog.d("Serf", "# resultBuffer: " + byteArrayToHexString(resultBuffer));
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);
                ULog.d("Serf", "getF1: " + build.getAcceleroDataG().getF1()+ "///getF2: " + build.getAcceleroDataG().getF2() + "///getF3: " + build.getAcceleroDataG().getF3());

            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }else if(false){

            if(!flags) {
                if (inPacket != null && inPacket.length > 0) {
                    try {
                        ULog.d("Serf", "# resultBuffer: " + byteArrayToHexString(inPacket));
                        epFlag = true;
                        for (int i = 0; i < inPacket.length - 1; i++) {
                            if(!flags) {
                                if (inPacket[i] == 'S' && inPacket[i + 1] == 'E') {
                                    seFlag = true;
                                    epFlag = false;
                                    data1 = new byte[inPacket.length - i];
                                    for (int j = 0; j < inPacket.length - i; j++) {
                                        data1[j] = inPacket[i + j];
                                    }
                                }
                            }

                            if(seFlag) {
                                if(inPacket[i] == 'E' && inPacket[i+1] == 'P') {
                                    flags = true;
                                    seFlag = false;
                                    epFlag = false;
                                    data2 = new byte[i+2];
                                    for(int j = 0; j < i+2; j++) {
                                        if(!(inPacket[j] == 'S' && inPacket[j+1] == 'E')) {
                                            data2[j] = inPacket[j];
                                        }
                                    }
                                }
                            }
                        }

                        /*if(epFlag) {
                            buffer = inPacket;
                        }*/


                        if(flags) {
                            ULog.d("Serf", "# data1: " + byteArrayToHexString(data1));
                            ULog.d("Serf", "# data2: " + byteArrayToHexString(data2));
                            if(buffer != null && buffer.length >0) {
                                ULog.d("Serf", "# buffer: " + byteArrayToHexString(buffer));
                            }
                            int sInt = data1.length;
                            int eInt = data2.length;
                            byte[] data;
                            if(buffer != null && buffer.length > 0) {
                                data = new byte[sInt + eInt+ buffer.length];
                            }else {
                                data = new byte[sInt + eInt];
                            }

                            for(int i = 0; i < data.length; i++) {
                                if(data1.length > i) {
                                    data[i] = data1[i];
                                }else if(buffer != null && buffer.length > 0 && buffer.length+data1.length > i) {
                                    data[i] = buffer[i-data1.length];
                                }else {
                                    if(buffer != null && buffer.length > 0) {
                                        data[i] = data2[i - data1.length- buffer.length];
                                    }else {
                                        data[i] = data2[i - data1.length];
                                    }
                                }
                            }
                            ULog.d("Serf", "# total data: " + byteArrayToHexString(data));



                            int contentStart = 0, contentEnd = 0;
                            if (data != null && data.length > 0) {
                                for (int i = 0; i < data.length - 1; i++) {
                                    //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                                    if (data[i] == 'S' && data[i + 1] == 'E') {
                                        //20160804 old version(vr) contentStart = i + 4;
                                        contentStart = i + 2;
                                    }
                                    if (data[i] == 'E' && data[i + 1] == 'P') {
                                        //contentEnd = i - 2;
                                        contentEnd = i - 1;
                                        break;
                                    }
                                }
                            }

                            byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                            System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                            ULog.d("Serf", "# last resultBuffer: " + byteArrayToHexString(resultBuffer));
                            build = StnEggPacket.parseFrom(resultBuffer);

                            if(build != null) {
                                ULog.d("Serf", "build gait getF1: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF3());
                                ChartingDemoActivity.timeStamp = build.getTimeStamp();
                                ChartingDemoActivity.quat[0] = build.getKalman9AxesQuat().getF1();
                                ChartingDemoActivity.quat[1] = build.getKalman9AxesQuat().getF2();
                                ChartingDemoActivity.quat[2] = build.getKalman9AxesQuat().getF3();
                                ChartingDemoActivity.quat[3] = build.getKalman9AxesQuat().getF4();
                                ChartingDemoActivity.accX = build.getAcceleroDataG().getF1();
                                ChartingDemoActivity.accY = build.getAcceleroDataG().getF2();
                                ChartingDemoActivity.accZ = build.getAcceleroDataG().getF3();
                                ChartingDemoActivity.gyroX = build.getGyroDataDps().getF1();
                                ChartingDemoActivity.gyroY = build.getGyroDataDps().getF2();
                                ChartingDemoActivity.gyroZ = build.getGyroDataDps().getF3();
                                ChartingDemoActivity.magX = build.getMagDataUT().getF1();
                                ChartingDemoActivity.magY = build.getMagDataUT().getF2();
                                ChartingDemoActivity.magZ = build.getMagDataUT().getF3();
                                ChartingDemoActivity.gaitX = build.getGaitPos().getF1();
                                ChartingDemoActivity.gaitY = build.getGaitPos().getF2();
                                ChartingDemoActivity.gaitZ = build.getGaitPos().getF3();
                                ChartingDemoActivity.gaitStepCount = build.getGait().getZuptStepCounter();
                                ChartingDemoActivity.gaitDistance = build.getGait().getTotalDistance();
                                ChartingDemoActivity.gaitMotion = build.getGait().getZuptMotion();
                                ChartingDemoActivity.gaitZupt = build.getGait().getZupt();
                                ChartingDemoActivity.gaitErrorX = build.getGait().getErrorPositionX();
                                ChartingDemoActivity.gaitErrorY = build.getGait().getErrorPositionY();
                                ChartingDemoActivity.gaitErrorZ = build.getGait().getErrorPositionZ();
                                ChartingDemoActivity.gaitErrorPercentage = build.getGait().getErrorPercentage();
                                ULog.d("Serf", "gait gaitStepCount: " + ChartingDemoActivity.gaitStepCount+ "///gaitDistance: " + ChartingDemoActivity.gaitDistance + "///gaitMotion: " + ChartingDemoActivity.gaitMotion);
                                ULog.d("Serf", "gait getF1: " + ChartingDemoActivity.gaitX+ "///getF2: " + ChartingDemoActivity.gaitY + "///getF3: " + ChartingDemoActivity.gaitZ);
                                confirmFalg = true;
                            }else {
                                confirmFalg = false;
                            }
                            flags = false;
                            seFlag = false;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        flags = false;
                        seFlag = false;
                        confirmFalg = false;
                        e.printStackTrace();
                    }
                }
            }
        }else {
            if (inPacket != null && inPacket.length > 0) {

                try {
                    for(int i = 0; i < inPacket.length-1; i++) {
                        if (inPacket[i] == 'E' && inPacket[i + 1] == 'P' && inPacket[i + 2] == 'S' && inPacket[i + 3] == 'E') {
                            flags = true;
                        }
                    }

                    if(!flags && seFlag) {
                        buffer = inPacket;
                        meFlag = true;
                    }


                    for(int i = 0; i < inPacket.length-1; i++) {
                        if (flags) {

                            if (inPacket[i] == 'E' && inPacket[i + 1] == 'P' && inPacket[i + 2] == 'S' && inPacket[i + 3] == 'E') {
                                data2 = new byte[i + 2];
                                for (int j = 0; j < i + 2; j++) {
                                    data2[j] = inPacket[j];
                                }
//                                ULog.d("Serf", "# data2: " + byteArrayToHexString(data2));


                                if (data1 != null && data1.length > 0) {
//                                    ULog.d("Serf", "# data1 not null: " + byteArrayToHexString(data1));
                                    int sInt = data1.length;
                                    int eInt = data2.length;
                                    byte[] data;
                                    if (meFlag) {
                                        data = new byte[sInt + eInt + buffer.length];
                                        for (int k = 0; k < data.length; k++) {
                                            if (data1.length > k) {
                                                //SE 처음 시작된 부분 붙인다.
                                                data[k] = data1[k];
                                            } else if (data1.length + buffer.length > k) {
                                                // SEEP 가 없는 부분 가지고 와서 붙이기 S,E,E,P 짤린 부분이 있을 수 있어 seFlag로 SE가 시작 되야지만 buffer에 값 push
                                                data[k] = buffer[k - data1.length];
                                            } else {
                                                // EP 부분
                                                data[k] = data2[k - data1.length - buffer.length];
                                            }
                                        }
                                    } else {
                                        data = new byte[sInt + eInt];
                                        for (int k = 0; k < data.length; k++) {
                                            if (data1.length > k) {
                                                data[k] = data1[k];
                                            } else {
                                                data[k] = data2[k - data1.length];
                                            }
                                        }
                                    }

                                    ULog.d("Serf", "# data: " + byteArrayToHexString(data));

                                    int contentStart = 0, contentEnd = 0;
                                    if (data != null && data.length > 0) {
                                        for (int j = 0; j < data.length - 1; j++) {
                                            //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                                            if (data[j] == 'S' && data[j + 1] == 'E') {
                                                //20160804 old version(vr) contentStart = i + 4;
                                                contentStart = j + 2;
                                            }
                                            if (data[j] == 'E' && data[j + 1] == 'P') {
                                                //contentEnd = j - 2;
                                                contentEnd = j - 1;
                                                break;
                                            }
                                        }
                                        ULog.d("Serf", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
                                    }

                                    byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                                    System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                                    ULog.d("Serf", "# last resultBuffer: " + byteArrayToHexString(resultBuffer));
                                    build = StnEggPacket.parseFrom(resultBuffer);

                                    if(build != null) {
                                        ULog.d("Serf", "build gait getF1: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF1()+ "///getF2: " + build.getGaitPos().getF3());
                                        ChartingDemoActivity.timeStamp = build.getTimeStamp();
                                        ChartingDemoActivity.quat[0] = build.getKalman9AxesQuat().getF1();
                                        ChartingDemoActivity.quat[1] = build.getKalman9AxesQuat().getF2();
                                        ChartingDemoActivity.quat[2] = build.getKalman9AxesQuat().getF3();
                                        ChartingDemoActivity.quat[3] = build.getKalman9AxesQuat().getF4();
                                        ChartingDemoActivity.accX = build.getAcceleroDataG().getF1();
                                        ChartingDemoActivity.accY = build.getAcceleroDataG().getF2();
                                        ChartingDemoActivity.accZ = build.getAcceleroDataG().getF3();
                                        ChartingDemoActivity.gyroX = build.getGyroDataDps().getF1();
                                        ChartingDemoActivity.gyroY = build.getGyroDataDps().getF2();
                                        ChartingDemoActivity.gyroZ = build.getGyroDataDps().getF3();
                                        ChartingDemoActivity.magX = build.getMagDataUT().getF1();
                                        ChartingDemoActivity.magY = build.getMagDataUT().getF2();
                                        ChartingDemoActivity.magZ = build.getMagDataUT().getF3();
                                        ChartingDemoActivity.gaitX = build.getGaitPos().getF1();
                                        ChartingDemoActivity.gaitY = build.getGaitPos().getF2();
                                        ChartingDemoActivity.gaitZ = build.getGaitPos().getF3();
                                        ChartingDemoActivity.gaitStepCount = build.getGait().getZuptStepCounter();
                                        ChartingDemoActivity.gaitDistance = build.getGait().getTotalDistance();
                                        ChartingDemoActivity.gaitMotion = build.getGait().getZuptMotion();
                                        ChartingDemoActivity.gaitZupt = build.getGait().getZupt();
                                        ChartingDemoActivity.gaitErrorX = build.getGait().getErrorPositionX();
                                        ChartingDemoActivity.gaitErrorY = build.getGait().getErrorPositionY();
                                        ChartingDemoActivity.gaitErrorZ = build.getGait().getErrorPositionZ();
                                        ChartingDemoActivity.gaitErrorPercentage = build.getGait().getErrorPercentage();
                                        ULog.d("Serf", "gait gaitStepCount: " + ChartingDemoActivity.gaitStepCount+ "///gaitDistance: " + ChartingDemoActivity.gaitDistance + "///gaitMotion: " + ChartingDemoActivity.gaitMotion);
                                        ULog.d("Serf", "gait getF1: " + ChartingDemoActivity.gaitX+ "///getF2: " + ChartingDemoActivity.gaitY + "///getF3: " + ChartingDemoActivity.gaitZ);
                                        confirmFalg = true;
                                    }else {
                                        confirmFalg = false;
                                    }
                                }

                                data1 = new byte[inPacket.length - i - 2];
                                for (int k = 0; k < data1.length; k++) {
                                    data1[k] = inPacket[i + k + 2];
                                }
                                seFlag = true;
                                flags = false;//true
//                                ULog.d("Serf", "# data1: " + byteArrayToHexString(data1));

                            }
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.d("ArrayIndexOutOfBoundsException", "e :" + e.getMessage());
                    e.getStackTrace();
                    flags = false;
                    seFlag = false;
                    epFlag = false;
                    confirmFalg = false;
                    data1 = null;
                    data2 = null;
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    if(inPacket != null && inPacket.length > 0) {
                        for(int i = 0; i < inPacket.length-1; i++) {
                            if (inPacket[i] == 'E' && inPacket[i + 1] == 'P' && inPacket[i + 2] == 'S' && inPacket[i + 3] == 'E') {
                                data1 = new byte[inPacket.length - i - 2];
                                for (int k = 0; k < data1.length; k++) {
                                    data1[k] = inPacket[i + k + 2];
                                }
                                seFlag = true;
                            }
                        }
                    }
                }
            }
        }





        /*byte[] data = inPacket;
        if (data != null && data.length > 0) {
            // final StringBuilder stringBuilder = new
            // StringBuilder(data.length);
            // final StringBuilder number = new StringBuilder(1);
            // for(byte byteChar : data)
            // stringBuilder.append(String.format("%02X ", byteChar));
            String a = "";

            byte[] test = new byte[1];
            for (int i = 0; i < data.length; i++) {
                test[0] = data[i];
                Log.d("Serf", "REAL RECEIVE MESSAGE ==>" + new String(test));
                //ULog.d(TAG, "REAL RECEIVE MESSAGE ==>" + test);
            }

//				if(data.length == 1) {
            if (true) {


            //20160822 start
            try {
                int contentStart = 0, contentEnd = 0;
                byte[] newData = new byte[data.length];
                if (data != null && data.length > 0) {
                    for (int i = 0; i < data.length - 1; i++) {
                        //Log.d("MainActivity", "# resultBuffer: " + i + ": " + resultBuffer[i]);
                        if (data[i] == 'E' && data[i + 1] == 'P' || data[i + 2] == 'S' && data[i + 3] == 'E') {
                            //20160804 old version(vr) contentStart = i + 4;
                            for(int j = 0; j < ( data.length-1) - i; j++) {
                                newData[j] = data[i];
                            }
                            contentStart = i + 2;
                        }
                        if (data[i] == '\r' && data[i + 1] == '\n') {
                            contentEnd = i - 2;
                            break;
                        }
                    }
                    Log.d("Serf", "contentStart : " + contentStart + "////contentEnd : " + contentEnd);
                }

                byte[] resultBuffer = new byte[contentEnd - contentStart + 1];
                System.arraycopy(data, contentStart, resultBuffer, 0, contentEnd - contentStart + 1);
                build = StnEggPkt.StnEggPacket.parseFrom(resultBuffer);

                Float3 accRaw = build.getAcceleroDataG();
                accX = accRaw.getF1();
                accY = accRaw.getF2();
                accZ = accRaw.getF3();
                Log.d("Serf", "Acc : x=" + accX + ", y=" + accY + ", z=" + accZ);

                Float3 gyroRaw = build.getGyroDataDps();
                gyroX = gyroRaw.getF1();
                gyroY = gyroRaw.getF2();
                gyroZ = gyroRaw.getF3();
                Log.d("Serf", "Gyro : x=" + gyroX + ", y=" + gyroY + ", z=" + gyroZ);

                Float3 magRaw = build.getMagDataUT();
                magX = magRaw.getF1();
                magY = magRaw.getF2();
                magZ = magRaw.getF3();
                Log.d("Serf", "Mag : x=" + magX + ", y=" + magY + ", z=" + magZ);
                Log.d("Serf", "getSerpCommand: " + build.getSerpCommand());


            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }*/
    }




    //20160728 start
    private static final String HEXES = "0123456789ABCDEF";
    public static String byteArrayToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        boolean firstEntry = true;
        sb.append('[');

        for (final byte b : array) {
            if (!firstEntry) {
                sb.append(", ");
            }
            sb.append(HEXES.charAt((b & 0xF0) >> 4));
            sb.append(HEXES.charAt((b & 0x0F)));
            firstEntry = false;
        }

        sb.append(']');
        return sb.toString();
    }
    //20160728 end
}
