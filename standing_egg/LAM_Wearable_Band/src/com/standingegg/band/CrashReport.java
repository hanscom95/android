package com.standingegg.band;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.standingegg.band.R;

import android.app.Application;

/**
 * Crash report send mail
 */
@ReportsCrashes(formUri = "", // will not be used
mailTo = "jess.choi@standing-egg.co.kr", customReportContent = { ReportField.APP_VERSION_CODE,
		ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
		ReportField.STACK_TRACE,
		ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class CrashReport extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ACRA.init(this);
	}

}
