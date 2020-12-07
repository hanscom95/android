package com.asha.md360player4android.vrlib.objects;


import android.content.Context;

import com.asha.md360player4android.vrlib.common.GLUtil;
import com.asha.md360player4android.R;

/**
 * Created by hzqiujiadi on 16/1/22.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class MDDome3D extends MDAbsObject3D {

    @Override
    protected void executeLoad(Context context) {
        GLUtil.loadObject3D(context, R.raw.dome,this);
    }
}
