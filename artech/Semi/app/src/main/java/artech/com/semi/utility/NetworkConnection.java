package artech.com.semi.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    String uri = "http://13.125.25.24/";

    public String mMsg;
    public String mResult;
    public JSONArray mAccountInfo;
    public JSONArray mProductArray;
    public JSONArray mPurchaseArray;
    public JSONArray mBannerArray;
    public JSONArray mMarketArray;
    public JSONArray mReviewArray;
    public JSONArray mFavoritesArray;
    public JSONArray mTalkArray;
    public JSONArray mPointArray;
    public JSONArray mCouponArray;
    public JSONArray mJSONArray;
    public JSONObject mJSONObject;
    public JSONObject mIamportObject;


    public boolean appSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/app/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }
        
        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fishing/users/update.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
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

        return mResult.equals("200");
    }

    public boolean accountDelete(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"Server/Users/delete.php?name=" + array.get(0) + "&pw="+array.get(1) ;
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

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
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

    public boolean accountSelect(String id) {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mAccountInfo = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountBusinessSelect(String id, String date) {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_business.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mAccountInfo = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountSelectId(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_id.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountSelectLogin(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_login.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mAccountInfo = new JSONArray(params.getString("data"));
                }

            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountMyInfoSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_my_info.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mAccountInfo = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean accountSelectIdEmail(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/update_pw.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mJSONObject = params;
                }

            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean normalSelect() {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_normal.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mJSONArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean pointSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_point.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mPointArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean couponSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/select_coupon.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mCouponArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean questionInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/users/insert_question.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean marketSelect() {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }


    public boolean marketBestSelect() {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/select_best.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean marketRecommendSelect() {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/select_recommend.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean marketItemSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/select_item.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean marketUpdateSearch(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/select_update_search.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean marketUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/market/update.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean productInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/update.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productStateUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/update_state.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productUseUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/update_use.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productRecommendUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/update_recommend.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productItemSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_item.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productSelect(String id) {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productBaitSelect(String id, String value) {
        String result = null;
        String msg = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_business_bait.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productNormalBestSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_normal.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productNormalHotDealSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_normal_hot_deal.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productNormalSeaSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_normal_sea.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productNormalItemSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_normal_item.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean productNormalItemDetailSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/product/select_normal_item_detail.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mProductArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mPurchaseArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseItemSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/select_item.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mPurchaseArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/update.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseRefundUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/update_refund.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean purchaseRemoveUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/purchase/update_remove.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean bannerSelect( ) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/banner/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mBannerArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean mapSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;
        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/map/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean mapBaitSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;
        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/map/select_bait.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mMarketArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fishing/review/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_product.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewProductInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fishing/review/insert_product.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewProductReplyInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fishing/review/insert_product_reply.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean favoritesShopSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/favorites/select_company.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mFavoritesArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean favoritesProductSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/favorites/select_product.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mFavoritesArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean favoritesUpdate(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/favorites/update.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean favoritesInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/favorites/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean favoritesDelete(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/favorites/delete.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkScrapInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/insert_scrap.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();


            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkLikeInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/insert_like.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkReplyInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/insert_reply.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkMySelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_my.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkMyReviewSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_my_review.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkMyPictureSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_my_picture.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkMyScrapSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_my_scrap.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkScrapDelete(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/delete_scrap.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkLikeDelete(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/delete_like.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkItemSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_item.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mTalkArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean talkReviewSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/talk/select_review.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mJSONArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewProductSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_business_product.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewMarketSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_business_market.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewMarketMoreSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_normal_market.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewProductDetailSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_business_product_detail.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean reviewMarketDetailSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/review/select_business_market_detail.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mReviewArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }


    public boolean iamportKeySelect() {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = "https://api.iamport.kr/users/getToken";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imp_key", "1982088553659080");
            jsonObject.put("imp_secret", "8Jhaf5zl2ry2hozQHbnHdhHBKL77zh9I0rSfxsBzU9Cbyb2gmpXzHFbrrjy0AIJB9BRH4shYCT9b4brr");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
//            conn.setRequestProperty("imp_key", "1982088553659080");
//            conn.setRequestProperty("imp_secret", "8Jhaf5zl2ry2hozQHbnHdhHBKL77zh9I0rSfxsBzU9Cbyb2gmpXzHFbrrjy0AIJB9BRH4shYCT9b4brr");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            writer.write(jsonObject.toString());
//            writer.flush();
//            writer.close();
            out.write(jsonObject.toString().getBytes());
            out.flush();
            out.close();


            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mIamportObject = params;
                Log.d("NetworkConnection", "mIamportObject : " + mIamportObject.toString());
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean iamportCancelSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = "https://api.iamport.kr/payments/cancel?_token="+jsonObject.getString("access_token");
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization", jsonObject.getString("access_token"));
            conn.setDoInput(true);
            conn.setDoOutput(true);
//            OutputStream out = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
//            writer.write(jsonObject.toString());
//            writer.flush();
//            writer.close();


            OutputStream out = conn.getOutputStream();
            out.write(jsonObject.toString().getBytes());
            out.flush();
            out.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mIamportObject = params;
                Log.d("NetworkConnection", "mIamportObject : " + mIamportObject.toString());
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean iamportVBankSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = "https://api.iamport.kr/payments/"+jsonObject.getString("imp_uid") + "?_token=" + jsonObject.getString("access_token");
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            JSONObject object = new JSONObject();
            object.put("imp_key", "1982088553659080");
            object.put("imp_secret", "8Jhaf5zl2ry2hozQHbnHdhHBKL77zh9I0rSfxsBzU9Cbyb2gmpXzHFbrrjy0AIJB9BRH4shYCT9b4brr");
            object.put("imp_uid", jsonObject.getString("imp_uid"));
            object.put("access_token", jsonObject.getString("access_token"));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            writer.write(jsonObject.toString());
//            writer.flush();
//            writer.close();
            out.write(object.toString().getBytes());
            out.flush();
            out.close();


            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mIamportObject = params;
                Log.d("NetworkConnection", "mIamportObject : " + mIamportObject.toString());
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean recentlyViewSelect(JSONObject jsonObject) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/recently_view/select.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);

                if(mResult.equals("200")) {
                    mJSONArray = new JSONArray(params.getString("data"));
                }
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    public boolean recentlyViewInsert(JSONObject jsonObject) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
//            String str = uri+"Server/Users/insert.php?id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(second) + "&email="+array.get(3) + "&sex="+array.get(4) + "&date="+array.get(5) + "&email="+array.get(7);
            String str = uri+"fishing/recently_view/insert.php";
            URL url = new URL(str);
            Log.d("NetworkConnection", "url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("NetworkConnection", "jsonObject : " + jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                mResult = params.getString("result");
                mMsg = params.getString("msg");
                Log.d("NetworkConnection", "result : " + mResult);
                Log.d("NetworkConnection", "msg : " + mMsg);
            }else {
                Log.d("NetworkConnection", "code : " + conn.getResponseCode());
                Log.d("NetworkConnection", "error : " + conn.getErrorStream());
                return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mResult.equals("200");
    }

    private boolean networkStateCheck(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
