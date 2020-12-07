package com.asha.md360player4android.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEventListener;

import com.asha.md360player4android.vrlib.strategy.ModeManager;
import com.asha.md360player4android.vrlib.MD360Director;
import com.asha.md360player4android.vrlib.MDVRLibrary;

import java.util.List;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class InteractiveModeManager extends ModeManager<AbsInteractiveStrategy> implements IInteractiveMode {


    public static class Params{
        public List<MD360Director> mDirectorList;
        public int mMotionDelay;
        public SensorEventListener mSensorListener;
        public int mStatus = 0;
        public String mAddress;
    }

    private boolean mIsResumed;
    private Params mParams;
    public InteractiveModeManager(int mode, Params params) {
        super(mode);
        mParams = params;
    }

    @Override
    public void switchMode(Activity activity) {
        int nextMode = getMode() == MDVRLibrary.INTERACTIVE_MODE_MOTION ?
                MDVRLibrary.INTERACTIVE_MODE_SENSOR
                : MDVRLibrary.INTERACTIVE_MODE_MOTION;
        switchMode(activity,nextMode);
        if (mIsResumed) onResume(activity);
    }

    @Override
    protected AbsInteractiveStrategy createStrategy(int mode) {
        switch (mode){
            case MDVRLibrary.INTERACTIVE_MODE_MOTION:
                return new MotionStrategy(mParams);
            case MDVRLibrary.INTERACTIVE_MODE_TOUCH:
                return new TouchStrategy(mParams);
            case MDVRLibrary.INTERACTIVE_MODE_SENSOR:
            default:
                return new SensorStrategy(mParams);
        }
    }

    @Override
    public void onResume(Context context) {
        mIsResumed = true;
        if (getStrategy().isSupport((Activity)context)){
            getStrategy().onResume(context);
        }
    }

    @Override
    public void onPause(Context context) {
        mIsResumed = false;
        if (getStrategy().isSupport((Activity)context)){
            getStrategy().onPause(context);
        }
    }

    /**
     * handle touch touch to rotate the model
     *
     * @param distanceX
     * @param distanceY
     * @return true if handled.
     */
    @Override
    public boolean handleDrag(int distanceX, int distanceY) {
        return getStrategy().handleDrag(distanceX,distanceY);
    }
}
