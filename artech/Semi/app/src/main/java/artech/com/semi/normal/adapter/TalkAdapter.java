package artech.com.semi.normal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import artech.com.semi.R;
import artech.com.semi.normal.TalkDetailActivity;
import artech.com.semi.normal.fragment.TalkFourFragment;
import artech.com.semi.normal.fragment.TalkOneFragment;
import artech.com.semi.normal.fragment.TalkThreeFragment;
import artech.com.semi.normal.fragment.TalkTwoFragment;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;
import artech.com.semi.utility.ViewPagerAdapter;

/**
 * Created by taehoon on 01/04/2018.
 */
public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.ViewHolder> {
    List<List<String>> mItems;
    TalkOneFragment mOneFragment;
    TalkTwoFragment mFragment;
    TalkThreeFragment mThreeFragment;
    TalkFourFragment mFourFragment;
    Context mContext;
    DBManager mDbManager;
    InsertTask mInsertTask;
    DeleteTask mDeleteTask;
    JSONArray mJsonArray;

    public int mFlag = -1;

    public TalkAdapter(TalkOneFragment fragment, Context context, int flag) {
        super();
        mOneFragment = fragment;
        mContext = context;
        mFlag = flag;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        mItems = new ArrayList<>();
    }

    public TalkAdapter(TalkTwoFragment fragment, Context context, int flag) {
        super();
        mFragment = fragment;
        mContext = context;
        mFlag = flag;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        mItems = new ArrayList<>();
    }

    public TalkAdapter(TalkThreeFragment fragment, Context context, int flag) {
        super();
        mThreeFragment = fragment;
        mContext = context;
        mFlag = flag;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        mItems = new ArrayList<>();
    }

    public TalkAdapter(TalkFourFragment fragment, Context context, int flag) {
        super();
        mFourFragment = fragment;
        mContext = context;
        mFlag = flag;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        mItems = new ArrayList<>();
    }


    public void addList(JSONArray jsonArray) {
        List<String> list;
        mJsonArray = jsonArray;
        Log.d("talkadapter" , "list : " + mJsonArray.toString());
        for(int i = 0; i < mJsonArray.length() ;i++) {
            list = new ArrayList<>();
            try {
                Log.d("talkadapter" , "title : " + mJsonArray.getJSONObject(i).getString("title"));
                list.add(mJsonArray.getJSONObject(i).getString("name"));
                list.add(mJsonArray.getJSONObject(i).getString("address"));
                list.add(mJsonArray.getJSONObject(i).getString("title"));
                list.add(mJsonArray.getJSONObject(i).getString("contents"));
                list.add(mJsonArray.getJSONObject(i).getString("date"));
                list.add(mJsonArray.getJSONObject(i).getString("like"));
                list.add(mJsonArray.getJSONObject(i).getString("scrap"));
                list.add(mJsonArray.getJSONObject(i).getString("review"));
                list.add(mJsonArray.getJSONObject(i).getString("picture1"));
                list.add(mJsonArray.getJSONObject(i).getString("picture2"));
                list.add(mJsonArray.getJSONObject(i).getString("picture3"));
                list.add(mJsonArray.getJSONObject(i).getString("scrap_yn"));
                list.add(mJsonArray.getJSONObject(i).getString("like_yn"));
                list.add(mJsonArray.getJSONObject(i).getString("talk_id"));
                list.add(mJsonArray.getJSONObject(i).getString("profile"));
                mItems.add(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_talk_two_list_customer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final List<String> nature = mItems.get(i);

        if(mFlag == 0) {

            viewHolder.sortTwoLayout.setVisibility(View.GONE);
            viewHolder.sortOneLayout.setVisibility(View.VISIBLE);

            viewHolder.nameOneText.setText(Html.fromHtml(nature.get(0).replace("\n", "<br>")));
            viewHolder.locOneText.setText(Html.fromHtml(nature.get(1).replace("\n", "<br>")));
            viewHolder.titleOneText.setText(Html.fromHtml(nature.get(2).replace("\n", "<br>")));
            viewHolder.contentsOneText.setText(Html.fromHtml(nature.get(3).replace("\n", "<br>")));
            viewHolder.dateOneText.setText(Html.fromHtml(nature.get(4)));
            viewHolder.favoriteOneText.setText(Util.toNumFormat(Integer.parseInt(nature.get(5))));
            viewHolder.scrapOneText.setText(Util.toNumFormat(Integer.parseInt(nature.get(6))));
            viewHolder.reviewOneText.setText(Util.toNumFormat(Integer.parseInt(nature.get(7))));
//            if(nature.get(14) != null || !"".equals(nature.get(14)) || nature.get(14) != "") {
            if(nature.get(14) != "null") {
                viewHolder.profileOneImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(14)));
            }



            int bitmap = 0;
            if(nature.get(8) != "null") {
                bitmap++;
            }
            if(nature.get(9) != "null") {
                bitmap++;
            }
            if(nature.get(10) != "null") {
                bitmap++;
            }

            Bitmap[] bitmaps = new Bitmap[bitmap];

            for(int j = 0; j < bitmap; j++) {
                bitmaps[j] = Util.setDecoded64ImageStringFromBitmap(nature.get(8+j));
                viewHolder.oneViewPager.setVisibility(View.VISIBLE);
            }
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);

            viewHolder.oneViewPager.setAdapter(viewPagerAdapter);

            if(Integer.parseInt(nature.get(11)) == 1) {
                viewHolder.scrapOneCheckbox.setChecked(true);
            }

            if(Integer.parseInt(nature.get(12)) == 1) {
                viewHolder.favoritesOneCheckbox.setChecked(true);
            }


            viewHolder.reviewWriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        Intent intent = new Intent(mContext, TalkReviewActivity.class);
//                        intent.putExtra("id", mJsonArray.getJSONObject(i).getString("talk_id"));
//                        mContext.startActivity(intent);

                        Intent intent = new Intent(mContext, TalkDetailActivity.class);
                        intent.putExtra("id", mJsonArray.getJSONObject(i).getString("talk_id"));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            viewHolder.contentsOneText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(mContext, TalkDetailActivity.class);
                        intent.putExtra("id", mJsonArray.getJSONObject(i).getString("talk_id"));
                        mContext.startActivity(intent);
                        /*if(mOneFragment != null) {
                            mOneFragment.setDetailClick(i);
                        }

                        if(mFragment != null) {
                            mFragment.setDetailClick(i);
                        }

                        if(mThreeFragment != null) {
                            mThreeFragment.setDetailClick(i);
                        }

                        if(mFourFragment != null) {
                            mFourFragment.setDetailClick(i);
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.favoritesOneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("talk_id", nature.get(13));
                        jsonObject.put("user_id", mDbManager.selectUserId());


                        if(b) {
                            mInsertTask = new InsertTask(jsonObject, 0);
                            mInsertTask.execute();
                        }else {
                            mDeleteTask = new DeleteTask(jsonObject, 0);
                            mDeleteTask.execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            viewHolder.scrapOneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("talk_id", nature.get(13));
                        jsonObject.put("user_id", mDbManager.selectUserId());

                        if(b) {
                            mInsertTask = new InsertTask(jsonObject, 1);
                            mInsertTask.execute();
                        }else {
                            mDeleteTask = new DeleteTask(jsonObject, 1);
                            mDeleteTask.execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else if(mFlag == 1) {
            viewHolder.sortOneLayout.setVisibility(View.GONE);
            viewHolder.sortTwoLayout.setVisibility(View.VISIBLE);

            viewHolder.nameTwoText.setText(Html.fromHtml(nature.get(0)));
            viewHolder.locTwoText.setText(Html.fromHtml(nature.get(1)));
            viewHolder.titleTwoText.setText(Html.fromHtml(nature.get(2)));
            viewHolder.dateTwoText.setText(Html.fromHtml(nature.get(4)));
            if(nature.get(14) != null || !"".equals(nature.get(14)) || nature.get(14) !="")
                viewHolder.profileTwoImg.setImageBitmap(Util.setDecoded64ImageStringFromBitmap(nature.get(14)));

            int bitmap = 0;
            if(nature.get(8) != "null") {
                bitmap++;
            }
            if(nature.get(9) !="null") {
                bitmap++;
            }
            if(nature.get(10) !="null") {
                bitmap++;
            }

            Bitmap[] bitmaps = new Bitmap[bitmap];

            for(int j = 0; j < bitmap; j++) {
                bitmaps[j] = Util.setDecoded64ImageStringFromBitmap(nature.get(8+j));
                viewHolder.twoViewPager.setVisibility(View.VISIBLE);
            }

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, bitmaps);

            viewHolder.twoViewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager oneViewPager, twoViewPager;
        public TextView nameOneText, locOneText, dateOneText, titleOneText, contentsOneText, favoriteOneText, reviewOneText, scrapOneText, nameTwoText, locTwoText, dateTwoText, titleTwoText;
        public CheckBox favoritesOneCheckbox, scrapOneCheckbox;
        public ImageView profileOneImg, profileTwoImg;
        public RelativeLayout detailLayout, reviewWriteLayout, sortOneLayout, sortTwoLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            oneViewPager = itemView.findViewById(R.id.item_one_viewpager);

            profileOneImg = itemView.findViewById(R.id.item_one_profile_img);
            profileTwoImg = itemView.findViewById(R.id.item_two_profile_img);

            nameOneText = (TextView) itemView.findViewById(R.id.item_one_name);
            locOneText = (TextView) itemView.findViewById(R.id.item_one_place_text);
            dateOneText = (TextView) itemView.findViewById(R.id.item_one_date);
            titleOneText = (TextView) itemView.findViewById(R.id.item_one_title_text);
            contentsOneText = (TextView) itemView.findViewById(R.id.item_one_contents_text);
            favoriteOneText = (TextView) itemView.findViewById(R.id.item_one_favorite);
            reviewOneText = (TextView) itemView.findViewById(R.id.item_one_review_value_text);
            scrapOneText = (TextView) itemView.findViewById(R.id.item_one_scrap_value_text);

            favoritesOneCheckbox = (CheckBox) itemView.findViewById(R.id.item_one_favorites_checkbox);
            scrapOneCheckbox = (CheckBox) itemView.findViewById(R.id.item_one_scrap_checkbox);

            reviewWriteLayout = (RelativeLayout) itemView.findViewById(R.id.item_one_review_write_layout);
            sortOneLayout = (RelativeLayout) itemView.findViewById(R.id.sort_one_layout);



            twoViewPager = itemView.findViewById(R.id.item_two_viewpager);

            nameTwoText = (TextView) itemView.findViewById(R.id.item_two_name);
            locTwoText = (TextView) itemView.findViewById(R.id.item_two_place_text);
            dateTwoText = (TextView) itemView.findViewById(R.id.item_two_date);
            titleTwoText = (TextView) itemView.findViewById(R.id.item_two_title_text);

            sortTwoLayout = (RelativeLayout) itemView.findViewById(R.id.sort_two_layout);


            detailLayout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
        }
    }


    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;
        int flag;

        InsertTask(JSONObject jsonObject, int flag) {
            this.jsonObject = jsonObject;
            this.flag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                if(flag == 0) {
                    connect = network.talkLikeInsert(jsonObject);
                }else if(flag == 1) {
                    connect = network.talkScrapInsert(jsonObject);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }


    private class DeleteTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;
        int flag;

        DeleteTask(JSONObject jsonObject, int flag) {
            this.jsonObject = jsonObject;
            this.flag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                if(flag == 0) {
                    connect = network.talkLikeDelete(jsonObject);
                }else if(flag == 1) {
                    connect = network.talkScrapDelete(jsonObject);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mDeleteTask = null;

            Log.d("SignUpActivity", "mAdminTask : " + success);
            if(success) {
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mDeleteTask = null;
        }
    }
}

