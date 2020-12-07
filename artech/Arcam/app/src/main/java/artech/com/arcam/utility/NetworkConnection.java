package artech.com.arcam.utility;

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
    public JSONArray mAccountInfo;



    public boolean accountInsert(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }
        
        try {
            String str = uri+"Server/Arcam/Account/insert.php?name=" + array.get(0) + "&pw="+array.get(1) + "&loc="+array.get(2) + "&team="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
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

    public boolean accountUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Arcam/Account/update.php?name=" + array.get(0) + "&pw="+array.get(1) + "&loc="+array.get(2) + "&team="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(6);
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

    public boolean accountDelete(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Arcam/Account/delete.php?name=" + array.get(0) + "&pw="+array.get(1) ;
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

    public boolean accountSelect(String value) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Arcam/Account/select_id.php?name="+value;
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

    public boolean equipmentInsert(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Arcam/Equipment/insert.php?name=" + array.get(0) + "&e1="+array.get(1) + "&e2="+array.get(2) + "&e3="+array.get(3) + "&e4="+array.get(4) + "&e5="+array.get(5) + "&e6="+array.get(6)
                    + "&e7="+array.get(7) + "&e8="+array.get(8) + "&e9="+array.get(9) + "&e10="+array.get(10) + "&e11="+array.get(11) + "&e12="+array.get(12) + "&e13="+array.get(13) + "&e14="+array.get(14) + "&e15="+array.get(15)
                    + "&e16="+array.get(16) + "&e17="+array.get(17) + "&e18="+array.get(18) + "&e19="+array.get(19) + "&e20="+array.get(20) + "&e21="+array.get(21) + "&e22="+array.get(22) + "&e23="+array.get(23) + "&e24="+array.get(24)
                    + "&e25="+array.get(25) + "&e26="+array.get(26) + "&e27="+array.get(27) + "&e28="+array.get(28) + "&e29="+array.get(29) + "&e30="+array.get(30) + "&e31="+array.get(31) + "&e32="+array.get(32);
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

    public boolean equipmentUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Arcam/Equipment/update.php?name=" + array.get(0) + "&e1="+array.get(1) + "&e2="+array.get(2) + "&e3="+array.get(3) + "&e4="+array.get(4) + "&e5="+array.get(5) + "&e6="+array.get(6)
                    + "&e7="+array.get(7) + "&e8="+array.get(8) + "&e9="+array.get(9) + "&e10="+array.get(10) + "&e11="+array.get(11) + "&e12="+array.get(12) + "&e13="+array.get(13) + "&e14="+array.get(14) + "&e15="+array.get(15)
                    + "&e16="+array.get(16) + "&e17="+array.get(17) + "&e18="+array.get(18) + "&e19="+array.get(19) + "&e20="+array.get(20) + "&e21="+array.get(21) + "&e22="+array.get(22) + "&e23="+array.get(23) + "&e24="+array.get(24)
                    + "&e25="+array.get(25) + "&e26="+array.get(26) + "&e27="+array.get(27) + "&e28="+array.get(28) + "&e29="+array.get(29) + "&e30="+array.get(30) + "&e31="+array.get(31) + "&e32="+array.get(32);
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

    private boolean networkStateCheck(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
