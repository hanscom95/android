package com.example.newdrawtest;

import com.example.newdrawtest.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Main android application file.  This starts everything else.
 *
 * @author Jim Cornmell
 * @since July 2013
 */
public class ThreeDFragment extends Fragment {
  /** The OpenGL view. */
  private GLSurfaceView mGlSurfaceView;

  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
 
	  View fragment = inflater.inflate(R.layout.threed, container, false);
	  //이벤트 처리전
//	  mGlSurfaceView = new GLSurfaceView(fragment.getContext());
//	  mGlSurfaceView.setRenderer(new GlRenderer(fragment.getContext()));
//	  return mGlSurfaceView;
	  
	  //이벤트 처리
	  mGlSurfaceView = new EventSurfaceView(fragment.getContext());
	  
	  //투명처리(렌더러 설정)
	  mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	  
	  mGlSurfaceView.setRenderer(new TexturedCubeRenderer(fragment.getContext())); // Cube
//	  mGlSurfaceView.setRenderer(new GlRenderer(fragment.getContext())); // Spere
	  
	  //배경투명
	  mGlSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	  mGlSurfaceView.setZOrderOnTop(true);
	  
	  mGlSurfaceView.requestFocus();
	  //mGlSurfaceView.setFocusableInTouchMode(true); // 이벤트 처리에 필요
	  return mGlSurfaceView;
	  
}
	@Override
	public void onPause() {
		this.mGlSurfaceView.onPause();
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.mGlSurfaceView.onResume();
	}
	
	public class EventSurfaceView extends GLSurfaceView{
		
		public EventSurfaceView(Context context) {
			super(context);
		}		
	}
}
