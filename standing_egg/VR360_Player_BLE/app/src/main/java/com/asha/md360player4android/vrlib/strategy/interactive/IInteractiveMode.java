package com.asha.md360player4android.vrlib.strategy.interactive;

import android.content.Context;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public interface IInteractiveMode {
    void onResume(Context context);
    void onPause(Context context);
    boolean handleDrag(int distanceX, int distanceY);
}
