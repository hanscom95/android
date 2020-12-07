package com.standingegg.band;

public interface IFragmentListener {
	public static final int CALLBACK_RUN_IN_BACKGROUND = 1;
	public static final int CALLBACK_SEND_MESSAGE = 2;
	
	public void OnFragmentCallback(int msgType, String arg2);
}
