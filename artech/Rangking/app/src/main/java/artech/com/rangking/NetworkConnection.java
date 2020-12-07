package artech.com.rangking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by moon on 2017-01-24.
 */

public class NetworkConnection {

    Context mContext;

    public NetworkConnection(Context context) {
        mContext = context;
    }

//    String uri = "http://34.208.110.160/";
    String uri = "http://52.78.53.155/";

    public String mMsg;
    public String mResult;
    public JSONArray mScoreInfo, mRankInfo;

    public boolean rankSelectScoreDESC() {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Rank/select.php";
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");

                if(!result.equals("200")) {
                    return false;
                }else {
                    data = params.getString("data");
                    mScoreInfo = new JSONArray(data);
                    mResult = result;
                    mMsg = msg;
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean rankSelectTotalDESC() {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Rank/select_total_desc.php";
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");

                if(!result.equals("200")) {
                    return false;
                }else {
                    data = params.getString("data");
                    mRankInfo = new JSONArray(data);
                    mResult = result;
                    mMsg = msg;
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private boolean networkStateCheck(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
