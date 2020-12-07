package com.example.newdrawtest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.newdrawtest.R;

import android.content.*;
import android.graphics.*;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

public class  TexturedCubeRenderer implements Renderer {
	private Context context;
	private TexturedCube cube;
	
	public  TexturedCubeRenderer (Context context) {
		this.context = context;
		//Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh);
		//cube = new TexturedCube(bitmap);
		
		Bitmap []bitmap = new Bitmap[6];

		bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh01);

		bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh02);

		bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh03);

		bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh04);

		bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh05);

		bitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fhrhfhrh06);
		
		
		cube = new TexturedCube(bitmap);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		//this.cube.loadGLTexture(gl, this.context, R.drawable.fhrhfhrh01);
		this.cube.loadGLTexture(gl);
		
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);

		// Depth buffer setup.
		gl.glClearDepthf(1.0f);

		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);

		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	float angle;
	public void onDrawFrame(GL10 gl) {
		
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();

		// Translates 4 units into the screen.
		gl.glTranslatef(0, 0, -5);
		//gl.glRotatef(angle, 1, 1, 0);
		
		 /*
	     * TEST
	     */
	    gl.glRotatef((float)TwoFragment.testX , 1.0f, 0.0f, 0.0f);
	    gl.glRotatef((float)TwoFragment.testY , 0.0f, 1.0f, 0.0f);
	    //gl.glRotatef(TwoFragment.testZ , 0, 0, 1);

		// Draw our scene.
		cube.draw(gl);
		angle += 1;
	}



	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);

		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);

		// Reset the projection matrix
		gl.glLoadIdentity();

		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,	1000.0f);

		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// Reset the modelview matrix
		gl.glLoadIdentity();

	} 

}