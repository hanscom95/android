package artech.com.semi.login;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import artech.com.semi.R;
import artech.com.semi.utility.AddressesByNameIntentService;

public class AddressSearchActivity extends AppCompatActivity {

    Context mContext;

    AddressTask mAddressTask;

    AddressListResultReceiver addressResultReceiver;

    EditText mEditText;
    ListView mListView;

    double[] mLat, mLon;

    private static final String CLIENT_ID = "n4MZunQRR8wghUZ_aoq4";
    private static final String CLIENT_SECRET_ID = "eoJl4iFxfe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_address_search);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView closeImg = findViewById(R.id.app_bar_close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddressesByName();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        mEditText = findViewById(R.id.address_search_text);
        mListView = findViewById(R.id.address_search_list);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    getAddressesByName();
//                    mAddressTask = new AddressTask(mEditText.getText().toString());
//                    mAddressTask.execute();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        addressResultReceiver = new AddressListResultReceiver(new Handler());
    }

    public void getAddressesByName(){
        getAddresses(mEditText.getText().toString());
    }

    private void getAddresses(String addName) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(AddressSearchActivity.this,
                    "주소를 찾을 수 없습니다,",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, AddressesByNameIntentService.class);
        intent.putExtra("address_receiver", addressResultReceiver);
        intent.putExtra("address_name", addName);
        startService(intent);
    }

    private class AddressListResultReceiver extends ResultReceiver {
        AddressListResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                Toast.makeText(mContext,
                        "Enter address name, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (resultCode == 1) {
                Toast.makeText(mContext,
                        "검색된 위치 정보가 없습니다." ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String[] addressList = resultData.getStringArray("addressList");

            mLat = resultData.getDoubleArray("lat");
            mLon = resultData.getDoubleArray("lon");


//            if(addressList.length > 0) {
//                for (int i = 0; i < addressList.length; i++) {
//                    addressList[i] += (lat[i]+", "+lon[i]);
//                }
//            }

            showResults(addressList);
        }
    }

    private void showResults(final String[] addressList){
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, addressList);

        final ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();

        int titleLen = addressList.length;
        for(int i =0; i < titleLen; i++) {
            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("address", addressList[i]);
            itemDataList.add(listItemMap);
        }


        SimpleAdapter arrayAdapter = new SimpleAdapter(this,itemDataList,R.layout.adapter_address_search_list_view,
                new String[]{"address"},new int[]{R.id.text1});

        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                if(itemDataList.get(i).get("address").toString().split(" ")[0].equals("대한민국")) {
                    intent.putExtra("address",itemDataList.get(i).get("address").toString().substring(4, itemDataList.get(i).get("address").toString().length()));
                    intent.putExtra("lat", mLon[i]);
                    intent.putExtra("lon", mLat[i]);
                }else {
                    intent.putExtra("address",itemDataList.get(i).get("address").toString());
                    intent.putExtra("lat", mLat[i]);
                    intent.putExtra("lon", mLon[i]);
                }
                setResult(1000, intent);
                finish();
            }
        });
    }


    private class AddressTask extends AsyncTask<Void, Void, Boolean> {
        JSONObject mJsonObject;
        String mAddr;

        AddressTask(String addr) {
            mAddr = addr;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            String clientId = CLIENT_ID;//애플리케이션 클라이언트 아이디값";
            String clientSecret = CLIENT_SECRET_ID;//애플리케이션 클라이언트 시크릿값";
            try {
                String addr = URLEncoder.encode(mAddr, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
                //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    connect = true;
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                mJsonObject = new JSONObject(response.toString());
                Log.d("AddressTask", "r : "+ response.toString());
            } catch (Exception e) {
                Log.d("AddressTask", "e : "+ e);
//                System.out.println(e);
            }

            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAddressTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
                try {
                    JSONArray jsonArray = mJsonObject.getJSONObject("result").getJSONArray("items");
                    String[] addressList = new String[jsonArray.length()];
                    mLat = new double[jsonArray.length()];
                    mLon = new double[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i++ ){
                        addressList[i] = jsonArray.getJSONObject(i).get("address").toString();
                        mLat[i] = jsonArray.getJSONObject(i).getJSONObject("point").getDouble("x");
                        mLon[i] = jsonArray.getJSONObject(i).getJSONObject("point").getDouble("y");
                    }

                    if(addressList.length > 0) {
                        showResults(addressList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(mContext, "주소를 찾을 수 없습니다,", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAddressTask = null;
        }
    }
}
