/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.standingegg.acc;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.standingegg.acc.util.CustomAdapter;
import com.standingegg.acc.util.DBManager;
import com.standingegg.acc.util.ListModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {

    private DBManager dbManager;
    private CustomAdapter adapter;

    ArrayList<ListModel> dataModels;
    ListView mListView;
    TextView mListCheckText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sensorValue();
        setContentView(R.layout.save_activity);

        mListView = (ListView) findViewById(R.id.save_list);
        mListCheckText = (TextView) findViewById(R.id.list_check_text);

        dbManager = new DBManager(getApplicationContext(), "vivo_test", null, 1);

        //dbSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.chart, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.chart:
                Intent intent = new Intent(getApplicationContext(), SaveChartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        dbSearch();
    }

    @Override
    public void onPause () {
        super.onPause();
        Log.d("test" , "================ onPause : ");
//        stopSensor();
    }

    @Override
    public boolean isDestroyed() {
        Log.d("test" , "================ isDestroyed : ");
        //stopSensor();
        return super.isDestroyed();
    }


    private void dbSearch() {
        Cursor cursor = dbManager.selectTotal();
        dataModels = new ArrayList<>();



        if(cursor.getCount() > 0){
            mListCheckText.setVisibility(View.GONE);
        }

        while (cursor.moveToNext()) {
            Log.d("SaveActivity", "total _key : " +cursor.getInt(0) +"//today : " + cursor.getString(1) + "//count : " + cursor.getInt(2) + "//walk : " + cursor.getInt(3) + "//run : " + cursor.getInt(4) +
                    "//percent : " + cursor.getInt(5) + "//hour : " + cursor.getString(6) + "//goal : " + cursor.getString(7));
            dataModels.add(new ListModel("Data_"+(cursor.getPosition()+1), cursor.getString(1), cursor.getInt(2)+"",cursor.getString(7), cursor.getInt(3)+"", cursor.getInt(4)+"", cursor.getString(1)));
        }



//        dataModels.add(new ListModel("Apple Pie", "Android 1.0", "1","September 23, 2008"));
//        dataModels.add(new ListModel("Banana Bread", "Android 1.1", "2","February 9, 2009"));
//        dataModels.add(new ListModel("Cupcake", "Android 1.5", "3","April 27, 2009"));
//        dataModels.add(new ListModel("Donut","Android 1.6","4","September 15, 2009"));
//        dataModels.add(new ListModel("Eclair", "Android 2.0", "5","October 26, 2009"));
//        dataModels.add(new ListModel("Froyo", "Android 2.2", "8","May 20, 2010"));
//        dataModels.add(new ListModel("Gingerbread", "Android 2.3", "9","December 6, 2010"));
//        dataModels.add(new ListModel("Honeycomb","Android 3.0","11","February 22, 2011"));
//        dataModels.add(new ListModel("Ice Cream Sandwich", "Android 4.0", "14","October 18, 2011"));
//        dataModels.add(new ListModel("Jelly Bean", "Android 4.2", "16","July 9, 2012"));
//        dataModels.add(new ListModel("Kitkat", "Android 4.4", "19","October 31, 2013"));
//        dataModels.add(new ListModel("Lollipop","Android 5.0","21","November 12, 2014"));
//        dataModels.add(new ListModel("Marshmallow", "Android 6.0", "23","October 5, 2015"));

        adapter = new CustomAdapter(dataModels, getApplicationContext());

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListModel dataModel= dataModels.get(position);
                Log.d("SaveActivity", "getName: " + dataModel.getName() + "///getVersion_number: " + dataModel.getVersion_number() + "///getFeature: " + dataModel.getFeature());
//                view.findViewById(R.id.row_layout).setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                Bundle doneBundle = new Bundle();
                doneBundle.putString("key", dataModel.getKey());
                doneBundle.putInt("goal", Integer.parseInt(dataModel.getFeature()));
                doneBundle.putInt("total", Integer.parseInt(dataModel.getVersion_number()));
                doneBundle.putInt("walk",  Integer.parseInt(dataModel.getWalking()));
                doneBundle.putInt("run", Integer.parseInt(dataModel.getRunning()));
                doneBundle.putInt("page", 1);
                intent.putExtras(doneBundle);
                startActivity(intent);
            }
        });

    }
}
