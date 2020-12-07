package artech.com.semi.utility.payment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import artech.com.semi.R;
import artech.com.semi.normal.PaymentSuccessActivity;

public class ImportActivity extends AppCompatActivity {

    Context mContext;

    private WebView mainWebView;
//    private static final String APP_SCHEME = "iamporttest://";
//    private static final String APP_SCHEME = "javascript://";
    private static final String APP_SCHEME = "semiapp://";
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_import);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent arrayIntent = getIntent();
        ArrayList<String> arrayList = arrayIntent.getStringArrayListExtra("arrayList");
//        Log.d("ImportActivity", "arrayList : " + arrayList.size());


        mainWebView = findViewById(R.id.webview);
        mainWebView.setWebViewClient(new InicisWebViewClient(this, arrayList));
        mainWebView.addJavascriptInterface(new DataCallback(), "android");
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent = getIntent();
        Uri intentData = intent.getData();

        if ( intentData == null ) {
            Log.d("ImportActivity", "intentData :null " );
//            mainWebView.loadUrl("http://www.iamport.kr/demo");
//            mainWebView.getSettings().setJavaScriptEnabled(true);
            mainWebView.loadUrl("file:///android_asset/payment/activity_import.html");
//            mainWebView.loadUrl("javascript:setPayment()");
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                if(!redirectURL.startsWith("https://")) {
//                    String resultURL = "https://auth.tpay.co.kr/doAuth";
//                    mainWebView.loadUrl(resultURL);
                    finish();
                }else {
                    mainWebView.loadUrl(redirectURL);
                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        Log.e("import", "onNewIntent : " + url);
        if ( url.startsWith(APP_SCHEME) ) {
            String redirectURL = url.substring(APP_SCHEME.length()+3);
            mainWebView.loadUrl(redirectURL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    	/* 실시간 계좌이체 인증 후 후속처리 루틴 */
        String resVal = data.getExtras().getString("bankpay_value");
        String resCode = data.getExtras().getString("bankpay_code");

        Log.e("import", "onActivityResult : " + resCode + " / resVal:  " + resVal);
        if("000".equals(resCode)){
            Log.e("import", "결제 성공.");
        }else if("091".equals(resCode)){//계좌이체 결제를 취소한 경우
            Log.e("import", "계좌이체 결제를 취소하였습니다.");
        }else if("060".equals(resCode)){
            Log.e("import", "타임아웃");
        }else if("050".equals(resCode)){
            Log.e("import", "전자서명 실패");
        }else if("040".equals(resCode)){
            Log.e("import", "OTP/보안카드 처리 실패");
        }else if("030".equals(resCode)){
            Log.e("import", "인증모듈 초기화 오류");
        }
    }

    final public class DataCallback {
        DataCallback() {
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @JavascriptInterface
        public void callBack(final String str) {
//            WalkRunChartActivity.infoLayoutSetup(str);
            new Thread() {
                public void run() {
//                    Intent intent = new Intent(mContext, ImportActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
                    Log.d("DataCallback", "str : "+str);
                }
            }.start();
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (mainWebView.canGoBack()) {
            mainWebView.goBack();
        } else {
//            if (System.currentTimeMillis() > this.backKeyPressedTime + 2000) {
//                this.backKeyPressedTime = System.currentTimeMillis();
//                showGuide();
//            } else if (System.currentTimeMillis() <= this.backKeyPressedTime + 2000) {
                finish();
//            }
        }
    }

    public void showGuide() {
        Toast.makeText(mContext, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }
    public void closeImport(String url) {
        Log.d("closeImport", "closeImport : "+url);


        Uri uri = Uri.parse(url);
        String server = uri.getAuthority();
        String path = uri.getPath();
        String protocol = uri.getScheme();
        Set<String> args = uri.getQueryParameterNames();
        ArrayList<String> idList = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();

        if(server.equals("mobile.vpay.co.kr")) {
            Toast.makeText(mContext, Html.fromHtml(uri.getQueryParameter("error_msg")), Toast.LENGTH_SHORT).show();
        }

//        try {
//            Log.i("ImportActivity", "server : " + server);
//            Log.i("ImportActivity", "path : " + path);
//            Log.i("ImportActivity", "protocol : " + protocol);
//            Log.i("ImportActivity", "args : " + args.toString());
//            Log.i("ImportActivity", "args size : " + args.size());
//            Log.i("ImportActivity", "args args : " + args.toArray()[0]);
//            Log.i("ImportActivity", "id : " + uri.getQueryParameter("id"));
//            Log.i("ImportActivity", "name : " + Html.fromHtml(uri.getQueryParameter("name")));
//        }catch (Exception e) {
//            finish();
//            e.printStackTrace();
//        }

        for(int i = 0; i < args.size(); i++) {
            nameList.add(Html.fromHtml(uri.getQueryParameter(args.toArray()[i].toString())).toString());
            Log.i("ImportActivity", "name : " + Html.fromHtml(uri.getQueryParameter(args.toArray()[i].toString())) + " / id : " + args.toArray()[i]);
        }

        if(Boolean.parseBoolean(uri.getQueryParameter("imp_success"))) {
            Log.i("ImportActivity", "imp_success : true");
            Intent intent = new Intent(mContext, PaymentSuccessActivity.class);
            intent.putStringArrayListExtra("nameList", nameList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            Log.i("ImportActivity", "imp_success : false");
            Toast.makeText(mContext, Html.fromHtml(uri.getQueryParameter("error_msg")), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
