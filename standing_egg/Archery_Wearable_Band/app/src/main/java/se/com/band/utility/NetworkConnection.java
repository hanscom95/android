package se.com.band.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Xml;

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

    String uri = "http://52.78.81.0/";

    public String mMsg;
    public String mResult;
    public JSONArray mAccountInfo, mActivityInfo, mFitnessInfo, mHeartInfo, mSleepInfo, mGoalInfo, mOptionInfo;

    public boolean login(String id, String pw) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"account/select.php?_id=" + id + "&pw="+pw;
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-Type", "text/html;  charset=utf-8");
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
//                    String name1 = Html.fromHtml(name).toString();
                    mAccountInfo = new JSONArray(data);
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

    public boolean accountCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"account/insert.php?_id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(2) + "&birth="+array.get(3) + "&email=" + array.get(0)+ "&tall="+array.get(4) + "&weight="+array.get(5)
                    + "&age="+array.get(7) + "&gender="+array.get(6) + "&device_addr="+array.get(8) + "&hand="+array.get(9);
//            String str = uri+"account/insert.php?_id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(2) + "&birth="+array.get(3) + "&email="+array.get(4) + "&tall="+array.get(5) + "&weight="+array.get(6)
//                    + "&age="+array.get(7) + "&gender="+array.get(8) + "&device_addr="+array.get(9) + "&hand="+array.get(10);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                ULog.d("params : " + params);
                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;

                if(Integer.parseInt(mResult)!= 200) {
                    return  false;
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

        return true;
    }

    /*public boolean accountCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        try {
            String str = uri+"account/insert.php?_id=" + array.get(0) + "&pw="+array.get(1) + "&name="+array.get(2) + "&birth="+array.get(3) + "&email="+array.get(4) + "&tall="+array.get(5) + "&weight="+array.get(6)
                            + "&age="+array.get(7) + "&gender="+array.get(8) + "&device_addr="+array.get(9) + "&hand="+array.get(10);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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
    }*/

    public boolean accountUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"account/update.php?_id=" + array.get(0) + "&name="+array.get(2) + "&birth="+array.get(3) + "&email="+array.get(0) + "&tall="+array.get(4) + "&weight="+array.get(5)
                    + "&age="+array.get(7) + "&gender="+array.get(6) + "&hand="+array.get(9);
//            String str = uri+"account/update.php?_id=" + array.get(0) + "&name="+array.get(1) + "&birth="+array.get(2) + "&email="+array.get(3) + "&tall="+array.get(4) + "&weight="+array.get(5)
//                    + "&age="+array.get(6) + "&gender="+array.get(7);
            ULog.d("uri : " +str);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;

                ULog.d("params : " + params);

                if(Integer.parseInt(mResult)!= 200) {
                    return  false;
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

        return true;
    }

    public boolean pwUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"account/pw_update.php?_id=" + array.get(0) + "&pw="+array.get(1);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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



    public boolean logout(String id) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"account/delete.php?_id=" + id;
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
                    mSleepInfo = new JSONArray(data);
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



    public boolean activityCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"activity/insert.php?_id=" + array.get(0) + "&type="+array.get(1) + "&distance="+array.get(2) + "&count="+array.get(3) + "&kcal="+array.get(4)
                    + "&time="+array.get(5) + "&date="+array.get(6) + "&duration="+array.get(7);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean activitySelect(String id, String type) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"activity/select.php?_id=" + id+"&type="+type;
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
                    mActivityInfo = new JSONArray(data);
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




    public boolean fitnessCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fitness/insert.php?_id=" + array.get(0) + "&motion="+array.get(1) + "&count="+array.get(2) + "&kcal="+array.get(3) + "&time="+array.get(4)
                    + "&date="+array.get(5) + "&duration="+array.get(6);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean fitnessSelect(String id, String type) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"fitness/select.php?_id=" + id+"&type="+type;
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
                    mFitnessInfo = new JSONArray(data);
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




    public boolean goalCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"goal/insert.php?_id=" + array.get(3) + "&motion="+array.get(0) + "&goal="+array.get(1) + "&kcal="+array.get(2);
            URL url = new URL(str);
            ULog.d("url : " + str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;

                mMsg = msg;
                ULog.d("result : " + result);
                ULog.d("msg : " + msg);
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

    public boolean goalUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"goal/update.php?_id=" + array.get(3) + "&motion="+array.get(0) + "&goal="+array.get(1) + "&kcal="+array.get(2);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;

                ULog.d("result : " + result);
                ULog.d("msg : " + msg);
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

    public boolean goalSelect(String id, String motion) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"goal/select.php?_id=" + id+"&motion="+motion;
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
                    mGoalInfo = new JSONArray(data);
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




    public boolean heartCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"heart/insert.php?_id=" + array.get(0) + "&rate="+array.get(1) + "&time="+array.get(2) + "&date="+array.get(3);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean heartSelect(String id, String type) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"heart/select.php?_id=" + id+"&type="+type;
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
                    mHeartInfo = new JSONArray(data);
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




    public boolean sleepCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"sleep/insert.php?_id=" + array.get(0) + "&type="+array.get(1) + "&s_sleep="+array.get(2) + "&e_sleep="+array.get(3) + "&date="+array.get(4);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean sleepSelect(String id, String type) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"sleep/select.php?_id=" + id+"&type="+type;
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
                    mSleepInfo = new JSONArray(data);
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




    public boolean optionCreate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"band_option/insert.php?_id=" + array.get(0) + "&ota="+array.get(1) + "&video="+array.get(2) + "&acc_date="+array.get(3) + "&date="+array.get(4);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean optionUpdate(ArrayList<Object> array) {
        String result = null;
        String msg = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"band_option/update.php?_id=" + array.get(0) + "&ota="+array.get(1) + "&video="+array.get(2) + "&acc_date="+array.get(3) + "&date="+array.get(4);
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buf = "";
                JSONObject params = null;

                while ((buf = reader.readLine()) != null) {
                    params = new JSONObject(buf);
                }

                result = params.getString("result");
                msg = params.getString("msg");
                mResult = result;
                mMsg = msg;
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

    public boolean optionSelect(String id, String type) {
        String result = null;
        String msg = null;
        String data = null;

        if(!networkStateCheck(mContext)) {
            return false;
        }

        try {
            String str = uri+"band_option/select.php?_id=" + id;
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
                    mSleepInfo = new JSONArray(data);
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

    public String weather() {
        String result = null;
        String msg = null;
        String data = null;
        try {
            String str = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4146556000";
            URL url = new URL(str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-Type", "text/html;  charset=utf-8");
            conn.setDoInput(true);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                String buf = "";
                String weather = "";

                while ((buf = reader.readLine()) != null) {
                    if(buf.indexOf("wfKor") != -1) {
//                        weather = buf.split("<sky>")[1].substring(0,1);
                        weather = buf;
                        return weather;
                    }
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return "";
    }

    private boolean networkStateCheck(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
