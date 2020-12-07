package com.asha.md360player4android.vrlib.strategy.display;

import android.opengl.GLSurfaceView;
import android.view.View;

import com.asha.md360player4android.vrlib.strategy.IModeStrategy;

import java.util.List;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class AbsDisplayStrategy implements IModeStrategy,IDisplayMode {
    private List<GLSurfaceView> mGLSurfaceViewList;

    public AbsDisplayStrategy(List<GLSurfaceView> glSurfaceViewList) {
        this.mGLSurfaceViewList = glSurfaceViewList;
    }

    protected List<GLSurfaceView> getGLSurfaceViewList() {
        return mGLSurfaceViewList;
    }

    void setVisibleSize(int max){
        int i = 0;
        for (GLSurfaceView surfaceView : getGLSurfaceViewList()){
            if (i < max) {
                surfaceView.setVisibility(View.VISIBLE);
            } else {
                surfaceView.setVisibility(View.GONE);
            }
            i++;
        }
    }
}
