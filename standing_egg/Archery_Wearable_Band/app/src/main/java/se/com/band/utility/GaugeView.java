package se.com.band.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import se.com.band.R;

/**
 * Created by moon on 2017-01-18.
 */

public class GaugeView extends View {

        private Path mClippingPath;
        private Context mContext;
        private Bitmap mBitmap;
        private float mPivotX;
        private float mPivotY;

        public GaugeView(Context context) {
            super(context);
            mContext = context;
//            initilizeImage();
        }

        public GaugeView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
//            initilizeImage();
        }

        private void initilizeImage(int x) {
            this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            mClippingPath = new Path();

            //Top left coordinates of image. Give appropriate values depending on the position you wnat image to be placed
            mPivotX = getScreenGridUnit();
            mPivotY = 0;

            //Adjust the image size to support different screen sizes
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.gauge_full);
//            int imageWidth = (int) (getScreenGridUnit() * 30);
//            int imageHeight = (int) (getScreenGridUnit() * 30);
//            mBitmap = Bitmap.createScaledBitmap(bitmap, 1050, 1050, false);
//            mBitmap = Bitmap.createScaledBitmap(bitmap, getMeasuredWidth(), getMeasuredHeight(), false);
            mBitmap = Bitmap.createScaledBitmap(bitmap, x, x, false);
        }

        public void setClipping(float progress) {

            //Convert the progress in range of 0 to 100 to angle in range of 0 180. Easy math.
            float angle = (progress * 180) / 100;
            mClippingPath.reset();
            //Define a rectangle containing the image
            RectF oval = new RectF(mPivotX, mPivotY, mPivotX + mBitmap.getWidth(), mPivotY + mBitmap.getHeight());
//            RectF oval = new RectF(getWidth(), getHeight(), getWidth() + mBitmap.getWidth(), getHeight() + mBitmap.getHeight());
            //Move the current position to center of rect
            mClippingPath.moveTo(oval.centerX(), oval.centerY());
            //Draw an arc from center to given angle
            mClippingPath.addArc(oval, 270, angle);
            //Draw a line from end of arc to center
            mClippingPath.lineTo(oval.centerX(), oval.centerY());
            //Redraw the canvas
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //Clip the canvas
            canvas.clipPath(mClippingPath);

            canvas.drawBitmap(mBitmap, mPivotX, mPivotY, null);
//            canvas.drawBitmap(mBitmap, getMeasuredWidth(),getMeasuredHeight(), null);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // height 진짜 크기 구하기
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = 0;
            switch(heightMode) {
                case MeasureSpec.UNSPECIFIED://mode 가 셋팅되지 않은 크기가 넘어올때
                    heightSize = heightMeasureSpec;
                    break;
                case MeasureSpec.AT_MOST://wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                    heightSize = 500;
                    break;
                case MeasureSpec.EXACTLY://fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                    heightSize = MeasureSpec.getSize(heightMeasureSpec);
                    break;
            }


            // width 진짜 크기 구하기
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = 0;
            switch(widthMode) {
                case MeasureSpec.UNSPECIFIED://mode 가 셋팅되지 않은 크기가 넘어올때
                    widthSize = widthMeasureSpec;
                    break;
                case MeasureSpec.AT_MOST://wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                    widthSize = 500;
                    break;
                case MeasureSpec.EXACTLY://fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                    widthSize = MeasureSpec.getSize(widthMeasureSpec);
                    break;
            }


            if(widthSize > 0) {
                initilizeImage(widthSize);
            }

            setMeasuredDimension(widthSize, heightSize);
        }



        private float getScreenGridUnit() {
            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels / 32;
        }


}