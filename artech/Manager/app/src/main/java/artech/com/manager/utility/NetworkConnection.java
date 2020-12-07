package artech.com.manager.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
import java.util.ArrayList;

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
    public JSONArray mShopInfo;



    public boolean shopInsert(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Shop/insert.php?col=" + array.get(0) + "&name="+array.get(1) + "&info="+array.get(2) + "&number="+array.get(3);
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;

                mMsg = msg;
                Log.d("NetworkConnection", "result : " + result);
                Log.d("NetworkConnection", "msg : " + msg);
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

        return true;
    }

    public boolean shopUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Shop/update.php?_id=" + array.get(0) + "&name="+array.get(1) + "&info="+array.get(2) + "&number="+array.get(3);
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;

                mMsg = msg;
                Log.d("NetworkConnection", "result : " + result);
                Log.d("NetworkConnection", "msg : " + msg);
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

        return true;
    }


    public boolean shopDelete(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Shop/delete.php?_id=" + array.get(0);
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;

                mMsg = msg;
                Log.d("NetworkConnection", "result : " + result);
                Log.d("NetworkConnection", "msg : " + msg);
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

        return true;
    }


    public boolean shopSelect(int value) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Shop/select.php?col="+value;
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
                    mShopInfo = new JSONArray(data);
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
