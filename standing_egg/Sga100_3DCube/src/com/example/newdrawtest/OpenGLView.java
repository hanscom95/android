package com.example.newdrawtest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {

	//이벤트
	private GlRenderer mRenderer; 
	
	public OpenGLView(Context context) {
		// TODO Auto-generated constructor stub
		this(context, null);
	}
	
	public OpenGLView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OpenGLView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);

        // Tell EGL to use a ES 2.0 Context
        setEGLContextClientVersion(2);
        
		setRenderer(new GlRenderer(context));
	}

}
