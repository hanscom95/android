package com.asha.md360player4android.vrlib.strategy.interactive;

import com.asha.md360player4android.vrlib.strategy.IModeStrategy;
import com.asha.md360player4android.vrlib.MD360Director;

import java.util.List;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class AbsInteractiveStrategy implements IModeStrategy, IInteractiveMode {
    private InteractiveModeManager.Params params;

    public AbsInteractiveStrategy(InteractiveModeManager.Params params) {
        this.params = params;
    }

    public InteractiveModeManager.Params getParams() {
        return params;
    }

    protected List<MD360Director> getDirectorList() {
        return params.mDirectorList;
    }
}
