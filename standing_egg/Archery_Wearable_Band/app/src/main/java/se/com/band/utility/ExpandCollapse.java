package se.com.band.utility;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by hansc on 2017-02-22.
 */

public class ExpandCollapse {

    public static void expand(final View v) {
        v.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void setViewToWrappedHeight(View v) {
        v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public static void collapse(final View v) {
        collapse(v, null, true, 0);
    }

    public static void collapse(final View v, Animation.AnimationListener animationListener, boolean autoDuration, int duration) {

        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        if (autoDuration) {
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        } else {
            a.setDuration(duration);
        }
        if (animationListener!=null) a.setAnimationListener(animationListener);
        v.startAnimation(a);
    }}
