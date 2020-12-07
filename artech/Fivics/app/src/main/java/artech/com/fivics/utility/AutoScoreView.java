package artech.com.fivics.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Gustavo on 11/7/2017.
 */

public class AutoScoreView extends View {
    private Bitmap mBitmap;

    public AutoScoreView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setContent(Bitmap bitmap){
        mBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(mBitmap != null){
            double viewWidth = canvas.getWidth();
            double viewHeight = canvas.getHeight();

            double imageWidth = mBitmap.getWidth();
            double imageHeight  = mBitmap.getHeight();

            //double scale = Math.min(viewWidth/imageWidth, viewHeight/imageHeight);

            //Rect destBounds = new Rect(0,0, (int)(imageHeight*scale), (int)(imageWidth*scale));

            Rect destBounds = new Rect(0,0, (int)imageWidth, (int)imageHeight);

            canvas.drawBitmap(mBitmap,null,destBounds,null);


        }
    }

    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }
}
