package artech.com.fivics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class IntroActivity extends AppCompatActivity {

    Context mContext;

    RelativeLayout mIntroLayout;

    ImageView introLogoImg;

    int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_intro);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(flags);
                }
            }
        });

//        Configuration config = new Configuration();
//        config.setLocale(Locale.CHINESE);
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Configuration config = new Configuration();
        config.setLocale(Locale.CHINESE);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        introLogoImg = (ImageView) findViewById(R.id.intro_logo);
        Glide.with(mContext).asGif().load(R.raw.intro_logo).into(introLogoImg);

        mIntroLayout = (RelativeLayout) findViewById(R.id.content_intro);
//        Drawable[] layers = new Drawable[2];
//        layers[0] = getResources().getDrawable(R.mipmap.intro_bg1);
//        layers[1] = getResources().getDrawable(R.mipmap.intro_bg2);
//        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
//        mIntroLayout.setBackground(transitionDrawable);
//        transitionDrawable.startTransition(6000);
//        mIntroLayout.animate().alpha(0.0f).setDuration(5000).start();

        mIntroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TermsConditionsActivity.class);
                startActivity(intent);
                finish();

//                Intent intent = new Intent(getApplicationContext(), ArcheryBoardActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("phone", "01080196825");
//                bundle.putString("name", "문태훈");
//                bundle.putInt("member", 2);
//                bundle.putInt("menu", 3);
//                bundle.putInt("flag", 2);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
            }
        });
    }

    @Override
    @SuppressLint("NewApi")
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
