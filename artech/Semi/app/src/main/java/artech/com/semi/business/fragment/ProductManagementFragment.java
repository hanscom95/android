package artech.com.semi.business.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.business.adpater.ProductManagementAdapter;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;

/**
 * Created by moon on 2018-05-09.
 */

@SuppressLint("ValidFragment")
public class ProductManagementFragment extends Fragment{

    Context mContext;

    DBManager mDbManager;
    SelectTask mSelectTask;
    JSONArray mJsonArray;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProductManagementAdapter mAdapter;

    public ProductManagementFragment(Context context) {
        // Required empty public constructor
        mContext = context;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_product_management, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSelectTask = new SelectTask(mDbManager.selectUserId(), "");
        mSelectTask.execute();

        return view ;
    }

    public void searchProduct(String value) {
        mSelectTask = new SelectTask(mDbManager.selectUserId(), value);
        mSelectTask.execute();
    }

    private class SelectTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        String id, value;

        SelectTask(String _id, String value) {
            this.id = _id;
            this.value = value;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                if("".equals(value)) {
                    connect = network.productSelect(id);
                }else {
                    Log.d("ProductManagementFragment" , "value : " + value);
                    connect = network.productBaitSelect(id, value);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mSelectTask = null;
            String path = null;

            if(success) {
                if("".equals(value)) {
                    Log.d("ProductManagementFragment" , "productSelect");
                    mJsonArray = network.mProductArray;
                    mAdapter = new ProductManagementAdapter(mContext);
                    mAdapter.clear();
                    mAdapter.addList(mJsonArray);
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    Log.d("ProductManagementFragment" , "productBaitSelect");
                    mAdapter = new ProductManagementAdapter(mContext);
                    mAdapter.clear();
                    mAdapter.addList(network.mProductArray);
                    mRecyclerView.setAdapter(mAdapter);
                }

                Log.d("ProductManagementFragment", "mAdminTask : " + mJsonArray.toString());
                //                setDecoded64ImageStringFromBitmap(path);
            }else {
                Log.d("ProductManagementFragment", "not success mAdminTask : " + network.mResult);
                Log.d("ProductManagementFragment", "not success mAdminTask : " + network.mMsg);
                mAdapter = new ProductManagementAdapter(mContext);
                mAdapter.clear();
                mRecyclerView.setAdapter(mAdapter);
            }

        }

        @Override
        protected void onCancelled() {
            mSelectTask = null;
        }
    }

    public void dateSort(){
        try {
            if(mJsonArray == null) {
                return;
            }

            List<JSONObject> myJsonArrayAsList = new ArrayList<JSONObject>();
            for (int i = 0; i < mJsonArray.length(); i++){
                myJsonArrayAsList.add(mJsonArray.getJSONObject(i));
                Log.d("mJsonArray", "i : " + i +" , date : " + mJsonArray.getJSONObject(i).get("reg_dt"));
            }


            Collections.sort(myJsonArrayAsList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                    int compare = 0;
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date keyA = dateFormat.parse(jsonObjectA.getString("reg_dt"));
                        Date keyB = dateFormat.parse(jsonObjectB.getString("reg_dt"));

                        compare =  keyB.compareTo(keyA);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.d("dateSort", "compare : " + compare);

                    return compare;
                }
            });

            for (int i = 0; i < myJsonArrayAsList.size(); i++){
                Log.d("myJsonArrayAsList", "i : " + i +" , date : " + myJsonArrayAsList.get(i).get("reg_dt"));
            }

            mJsonArray = new JSONArray();
            for (int i = 0; i < myJsonArrayAsList.size(); i++) {
                mJsonArray.put(myJsonArrayAsList.get(i));
            }


            mAdapter.clear();
            mAdapter.addList(mJsonArray);
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void recommendSort(){
        try {
            List<JSONObject> myJsonArrayAsList = new ArrayList<JSONObject>();
            for (int i = 0; i < mJsonArray.length(); i++){
                myJsonArrayAsList.add(mJsonArray.getJSONObject(i));
                Log.d("mJsonArray", "i : " + i +" , recommend : " + mJsonArray.getJSONObject(i).get("favorites"));
            }


            Collections.sort(myJsonArrayAsList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                    int compare = 0;
                    try{
                        int keyA = jsonObjectA.getInt("favorites");
                        int keyB = jsonObjectB.getInt("favorites");

                        compare =  Integer.compare(keyB, keyA);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("dateSort", "compare : " + compare);

                    return compare;
                }
            });

            for (int i = 0; i < myJsonArrayAsList.size(); i++){
                Log.d("myJsonArrayAsList", "i : " + i +" , favorites : " + myJsonArrayAsList.get(i).get("favorites"));
            }

            mJsonArray = new JSONArray();
            for (int i = 0; i < myJsonArrayAsList.size(); i++) {
                mJsonArray.put(myJsonArrayAsList.get(i));
            }

            mAdapter.clear();
            mAdapter.addList(mJsonArray);
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findList(String query){
        try {
            List<JSONObject> myJsonArrayAsList = new ArrayList<JSONObject>();
            for (int i = 0; i < mJsonArray.length(); i++){
                if(mJsonArray.getJSONObject(i).getString("product_name").contains(query)) {
                    myJsonArrayAsList.add(mJsonArray.getJSONObject(i));
                    Log.d("mJsonArray", "i : " + i + " , product_name : " + mJsonArray.getJSONObject(i).get("product_name"));
                }
            }

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < myJsonArrayAsList.size(); i++) {
                jsonArray.put(myJsonArrayAsList.get(i));
            }

            mAdapter.clear();
            mAdapter.addList(jsonArray);
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
