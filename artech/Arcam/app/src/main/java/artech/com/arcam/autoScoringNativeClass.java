package artech.com.arcam;

/**
 * Created by Gustavo on 11/20/2017.
 */

public class autoScoringNativeClass {
    // define a JNI function
    public native static float[] autoScoringSegmentation(long frame1, int[] colorCoors, float[] foundElipses);
    public native static int autoScoringEvaluation(long frame1, long frame2, long debug1);
    public native static int detectMotion(long frame1, long frame2);
}

