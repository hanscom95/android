package artech.com.fivics.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Gustavo on 11/7/2017.
 */

public class TouchView extends View {
    private Bitmap mBitmap;
    int p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, p5X, p5Y;
    int flag;

    public TouchView(Context context, AttributeSet attrs){
        super(context, attrs);
        flag = 0;
    }

    void setContent(Bitmap bitmap){
        mBitmap = bitmap;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(flag == 0) {
            p1X = (int) event.getX(0);
            p1Y = (int) event.getY(0);
        }else if(flag == 1) {
            p2X = (int) event.getX(0);
            p2Y = (int) event.getY(0);
        }else if(flag == 2) {
            p3X = (int) event.getX(0);
            p3Y = (int) event.getY(0);
        }else if(flag == 3) {
            p4X = (int) event.getX(0);
            p4Y = (int) event.getY(0);
        }else if(flag == 4) {
            p5X = (int) event.getX(0);
            p5Y = (int) event.getY(0);
        }else {
            flag--;
        }

        flag++;
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

//        Paint paint = new Paint();
//        paint.setTextSize(50);
//        paint.setColor(Color.YELLOW);
//        canvas.drawText("("+x+", "+y+")에서 터치 발생", x, y, paint);


        if(flag > 0) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(p1X, p1Y, 10, paint);
        }

        if(flag > 1) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(p2X, p2Y, 10, paint);
        }

        if(flag > 2) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(p3X, p3Y, 10, paint);
        }

        if(flag > 3) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(p4X, p4Y, 10, paint);
        }

        if(flag > 4) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(p5X, p5Y, 10, paint);
        }
        /*if(mBitmap != null){
            double viewWidth = canvas.getWidth();
            double viewHeight = canvas.getHeight();

            double imageWidth = mBitmap.getWidth();
            double imageHeight  = mBitmap.getHeight();

            double scale = Math.min(viewWidth/imageWidth, viewHeight/imageHeight);

            Rect destBounds = new Rect(0,0, (int)(imageHeight*scale), (int)(imageWidth*scale));

            canvas.drawBitmap(mBitmap,null,destBounds,null);


        }*/
    }

    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }

    public int getP1X() {
        int one = p1X + p1Y;
        int two = p2X + p2Y;
        int three = p3X + p3Y;
        int four = p4X + p4Y;
        int five = p5X + p5Y;

        if((one < two) && (one < three) && (one < four) && (one < five)) {
            return p1X;
        }else if((two < three) && (two < four) && (two < five)) {
            return p2X;
        }else if((three < four) && (three < five)) {
            return p3X;
        }else if((four < five)) {
            return p4X;
        }else{
            return p5X;
        }
    }

    public int getP1Y() {
        int one = p1X + p1Y;
        int two = p2X + p2Y;
        int three = p3X + p3Y;
        int four = p4X + p4Y;
        int five = p5X + p5Y;

        if((one < two) && (one < three) && (one < four) && (one < five)) {
            return p1Y;
        }else if((two < three) && (two < four) && (two < five)) {
            return p2Y;
        }else if((three < four) && (three < five)) {
            return p3Y;
        }else if((four < five)) {
            return p4Y;
        }else{
            return p5Y;
        }
    }

    public int getP2X() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreX.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreX.length; j++) {
                if(scoreX[i]<scoreX[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 1) {
                for(int q = 0; q < rank.length; q++) {
                    if(rank[q] == 2) {
                        if(scoreY[k] < scoreY[q]) {
                            return scoreX[k];
                        }else {
                            return scoreX[q];
                        }
                    }
                }
            }
        }


        return p2X;
    }

    public int getP2Y() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreX.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreX.length; j++) {
                if(scoreX[i]<scoreX[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 1) {
                for(int q = 0; q < rank.length; q++) {
                    if(rank[q] == 2) {
                        if(scoreY[k] < scoreY[q]) {
                            return scoreY[k];
                        }else {
                            return scoreY[q];
                        }
                    }
                }
            }
        }


        return p2Y;
    }

    public int getP3X() {
        int one = p1X + p1Y;
        int two = p2X + p2Y;
        int three = p3X + p3Y;
        int four = p4X + p4Y;
        int five = p5X + p5Y;

        if((one > two) && (one > three) && (one > four) && (one > five)) {
            return p1X;
        }else if((two > three) && (two > four) && (two > five)) {
            return p2X;
        }else if((three > four) && (three > five)) {
            return p3X;
        }else if((four > five)) {
            return p4X;
        }else{
            return p5X;
        }
    }

    public int getP3Y() {
        int one = p1X + p1Y;
        int two = p2X + p2Y;
        int three = p3X + p3Y;
        int four = p4X + p4Y;
        int five = p5X + p5Y;

        if((one > two) && (one > three) && (one > four) && (one > five)) {
            return p1Y;
        }else if((two > three) && (two > four) && (two > five)) {
            return p2Y;
        }else if((three > four) && (three > five)) {
            return p3Y;
        }else if((four > five)) {
            return p4Y;
        }else{
            return p5Y;
        }
    }

    public int getP4X() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreY.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreY.length; j++) {
                if(scoreY[i]<scoreY[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 1) {
                for(int q = 0; q < rank.length; q++) {
                    if(rank[q] == 2) {
                        if(scoreX[k] < scoreX[q]) {
                            return scoreX[k];
                        }else {
                            return scoreX[q];
                        }
                    }
                }
            }
        }


        return p4X;
    }

    public int getP4Y() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreY.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreY.length; j++) {
                if(scoreY[i]<scoreY[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 1) {
                for(int q = 0; q < rank.length; q++) {
                    if(rank[q] == 2) {
                        if(scoreX[k] < scoreX[q]) {
                            return scoreY[k];
                        }else {
                            return scoreY[q];
                        }
                    }
                }
            }
        }


        return p4X;
    }

    public int getP5X() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreX.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreX.length; j++) {
                if(scoreX[i]<scoreX[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 3) {
                return scoreX[k];
            }
        }

        return p3X;
    }

    public int getP5Y() {
        int[] scoreX = {p1X,p2X,p3X,p4X,p5X};
        int[] scoreY = {p1Y,p2Y,p3Y,p4Y,p5Y};
        int[] rank = {1,1,1,1,1};

        for(int i = 0; i < scoreY.length; i++){
            rank[i] = 1;

            for (int j = 0; j < scoreY.length; j++) {
                if(scoreY[i]<scoreY[j]){
                    rank[i]++;
                }
            }
        }

        for(int k = 0; k < rank.length; k++) {
            if(rank[k] == 3) {
                return scoreY[k];
            }
        }


        return p3Y;
    }

    public void clearFlag() {
        flag = 0;
        invalidate();
    }
}
