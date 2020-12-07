package demo.sensors.se.com.ssdemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by CCZZZEE on 2016. 5. 10..
 */
public class FragmentOne extends Fragment {

    private CustomOnClickListener customListener;

    public interface CustomOnClickListener{
        public void onClicked();
    }


    private Button btnStart;

    public TextureCubeRenderer pcbRenderer = null;
    final int pcbSurfaces[] = {R.drawable.se_front_0, R.drawable.se_left_0, R.drawable.se_back_0, R.drawable.se_right_0,
            R.drawable.se_top_0, R.drawable.se_bottom_0};
    final float pcbDimensions[] = {1.5f, 0.87f, 0.5f, -2.5f};

   // Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    int screenRotation = 0;//display.getRotation();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_load, null);

        btnStart = (Button) v.findViewById(R.id.start);
        btnStart.setOnClickListener(onClickListener);
       // GLSurfaceView pcbGlview = (GLSurfaceView) v.findViewById(R.id.pcb_glview2222);
       // pcbGlview.setVisibility(View.VISIBLE);
       // pcbRenderer = new TextureCubeRenderer(getActivity().getApplicationContext(), screenRotation);
		/*
		 * void com.se.sensors.sfusion.TextureCubeRenderer.addCube( int[]
		 * surfaces, float[] dimensions, String desc)
		 */
       // pcbRenderer.addCube(pcbSurfaces, pcbDimensions, "Rev5 board");
       // pcbGlview.setRenderer(pcbRenderer);

        return v;
    }


    @Override
    public void onStart() {


        super.onStart();

       // GLSurfaceView pcbGlview = (GLSurfaceView) getView().findViewById(R.id.pcb_glview2222);
      //  pcbGlview.setVisibility(View.VISIBLE);



    }


    // 버튼에 설정한 OnClickListener의 구현, 버튼이 클릭 될 때마다 Activity의 커스텀 리스너를 호출함
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          //  getView().findViewById(R.id.pcb_glview2222).setVisibility(View.GONE);
            customListener.onClicked();
        }
    };

    // Activity 로 데이터를 전달할 커스텀 리스너를 연결
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        customListener = (CustomOnClickListener)activity;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
