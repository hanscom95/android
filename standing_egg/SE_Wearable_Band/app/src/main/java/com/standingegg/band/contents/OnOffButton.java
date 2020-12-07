package com.standingegg.band.contents;

import com.standingegg.band.setting.AlarmAcitivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.SlidingDrawer;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class OnOffButton extends SlidingDrawer implements Checkable, OnDrawerOpenListener, OnDrawerCloseListener {
	int mButtonResource = 0;
	OnCheckChangedListner mOnCheckChangedListner;
	OnCheckedChangeListener mOnCheckedChangeListener;

	public OnOffButton(Context context, AttributeSet attr) {
		super(context, attr);

		setOnDrawerOpenListener(this);
		setOnDrawerCloseListener(this);
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return !isOpened();
	}

	@Override
	public void setChecked(boolean checked) {
		if (!isOpened() != checked) {
			if (checked) {
				this.animateClose();
			} else {
				this.animateOpen();
			}
		}

	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		if (!isOpened()) {
			this.animateOpen();
		} else {
			this.animateClose();
		}

	}

	public interface OnCheckChangedListner {
		public void onCheckChanged(View v, boolean isChecked);
	}

	@Override
	public void onDrawerOpened() {
		// TODO Auto-generated method stub
		if (mOnCheckChangedListner != null) {
			mOnCheckChangedListner.onCheckChanged(this, isChecked());
		}
	}

	@Override
	public void onDrawerClosed() {
		// TODO Auto-generated method stub
		if (mOnCheckChangedListner != null) {
			mOnCheckChangedListner.onCheckChanged(this, isChecked());
		}
	}

	public OnCheckChangedListner getOnCheckChangedListner() {
		return mOnCheckChangedListner;
	}

	public void setOnCheckChangedListner(OnCheckChangedListner onCheckChangedListner) {
		this.mOnCheckChangedListner = onCheckChangedListner;
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
		this.mOnCheckedChangeListener = onCheckedChangeListener;
	}

}