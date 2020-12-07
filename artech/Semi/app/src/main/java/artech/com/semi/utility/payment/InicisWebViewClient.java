package artech.com.semi.utility.payment;

/**
 * Created by moon on 2018-06-25.
 */
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class InicisWebViewClient extends WebViewClient {

    private Activity activity;
    private ArrayList<String> arrayList;

    public InicisWebViewClient(Activity activity, ArrayList arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.e("import", "shouldOverrideUrlLoading : " + url);
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            Intent intent = null;

            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
                Uri uri = Uri.parse(intent.getDataString());

                activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            } catch (URISyntaxException ex) {
                return false;
            } catch (ActivityNotFoundException e) {
                if ( intent == null )	return false;

                if ( handleNotFoundPaymentScheme(intent.getScheme()) )	return true;

                String packageName = intent.getPackage();
                if (packageName != null) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    return true;
                }

                return false;
            }
        }else {
            if(url.startsWith("http://")) {
                Uri uri = Uri.parse(url);
                String server = uri.getAuthority();
                if(!server.equals("mobile.vpay.co.kr")) {
                    ((ImportActivity) activity).closeImport(url);
                }
            }
        }

        return false;
    }

    /**
     * @param scheme
     * @return 해당 scheme에 대해 처리를 직접 하는지 여부
     *
     * 결제를 위한 3rd-party 앱이 아직 설치되어있지 않아 ActivityNotFoundException이 발생하는 경우 처리합니다.
     * 여기서 handler되지않은 scheme에 대해서는 intent로부터 Package정보 추출이 가능하다면 다음에서 packageName으로 market이동합니다.
     *
     */
    protected boolean handleNotFoundPaymentScheme(String scheme) {
        //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
        if ( PaymentScheme.ISP.equalsIgnoreCase(scheme) ) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
            return true;
        } else if ( PaymentScheme.BANKPAY.equalsIgnoreCase(scheme) ) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
            return true;
        }

        return false;
    }


    @Override
    public void onPageFinished(WebView view, String url)
    {
//        view.loadUrl("javascript:callMe(\""+data_val+"\")");
//        view.loadUrl("javascript:setPayment('"+arrayList.get(0)+"', '"+arrayList.get(1)+"', '"+arrayList.get(second)+"', '"+arrayList.get(third)+"', '"+arrayList.get(4)+"''"+arrayList.get(5)+"''"+arrayList.get(6)+"''"+arrayList.get(7)+"''"+arrayList.get(8)+"')");
        if(url.equals("file:///android_asset/payment/activity_import.html")) {
            String urls = "javascript:setPayment('"+arrayList.get(0)+"', '"+arrayList.get(1)+"', '"+arrayList.get(2)+"', '"+arrayList.get(3)+"', '"+arrayList.get(4)+"', '"+arrayList.get(5)+"', '"+arrayList.get(6)+"', '"+arrayList.get(7)+"', '"+arrayList.get(8)+"', '"+arrayList.get(9)+"', '"+arrayList.get(10)+"', '"+arrayList.get(11)+"', '"+arrayList.get(12)+"', '"+arrayList.get(13)+"', '"+arrayList.get(14)+"')";
            view.loadUrl(urls);
            Log.d("InicsWebview", "urls : " + urls);
        }
        Log.d("InicsWebview", "url : " + Html.fromHtml(url));

    }

}