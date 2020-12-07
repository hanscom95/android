package artech.com.semi.business.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import artech.com.semi.business.ProductUpdateActivity;
import artech.com.semi.business.item.ProductManagementItem;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;

/**
 * Created by taehoon on 01/04/2018.
 */
public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ViewHolder> {
    List<ProductManagementItem> mItems;
    Context mContext;
    UpdateTask mUpdateTask;

    public ProductManagementAdapter(Context context) {
        super();
        mContext = context;
        mItems = new ArrayList<ProductManagementItem>();
    }

    public void addList(JSONArray mJsonArray) {
        ProductManagementItem nama;
        for(int i = 0; i < mJsonArray.length() ;i++) {
            nama = new ProductManagementItem();
            try {
                nama.setId(mJsonArray.getJSONObject(i).getString("product_id"));
                nama.setName(Html.fromHtml(mJsonArray.getJSONObject(i).getString("product_name")).toString());
                nama.setDate(mJsonArray.getJSONObject(i).getString("reg_dt"));
                nama.setPrice(mJsonArray.getJSONObject(i).getInt("price"));
                nama.setSalePrice(mJsonArray.getJSONObject(i).getInt("sale_price"));
                nama.setRecommand(mJsonArray.getJSONObject(i).getInt("favorites"));
                nama.setState(mJsonArray.getJSONObject(i).getInt("state"));
                nama.setThumbnail(Util.setDecoded64ImageStringFromBitmap(mJsonArray.getJSONObject(i).getString("picture")));
                mItems.add(nama);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_product_management_grid_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ProductManagementItem nature = mItems.get(i);
        viewHolder.name.setText(nature.getName());
        viewHolder.time.setText(nature.getDate());


        double mPrice = nature.getPrice()-(nature.getPrice()*(nature.getSalePrice()*0.01));
//        viewHolder.price.setText(Util.toNumFormat(nature.getPrice())+"원");
        viewHolder.price.setText(Util.toNumFormat((int) mPrice)+"원");
        if(nature.getSalePrice() > 0) {
            viewHolder.saleLayout.setVisibility(View.VISIBLE);
            viewHolder.priceEtc.setVisibility(View.VISIBLE);
            viewHolder.sale.setText(Util.toNumFormat(nature.getPrice()) + "원");
        }else {
            viewHolder.saleLayout.setVisibility(View.INVISIBLE);
            viewHolder.priceEtc.setVisibility(View.INVISIBLE);
        }
        viewHolder.recommend.setText(Util.toNumFormat(nature.getRecommend())+"찜(0개 구매)");

        if(nature.getState() == 0) {
            viewHolder.state.setText("판매중");
        }else if(nature.getState() == 1) {
//            viewHolder.state.setText("판매중지");
            viewHolder.state.setText("재고없음");
        }else if(nature.getState() == 2) {
            viewHolder.state.setText("판매종료");
        }

//        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        if(nature.getThumbnail() != null) {
            viewHolder.imgThumbnail.setImageBitmap(nature.getThumbnail());
        }


        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductUpdateActivity.class);
                intent.putExtra("product_id", nature.getId());
                mContext.startActivity(intent);
            }
        });



        viewHolder.stateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String items[] = { "판매중", "판매중지", "판매종료" };
                final String items[] = { "판매중", "재고없음", "판매종료" };
                final int[] selectedIndex = {-1};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setTitle("판매상태");
                alertBuilder.setSingleChoiceItems(items, nature.getState(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을때
                                selectedIndex[0] = whichButton;
                            }
                        }).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("id", nature.getId());
                                    jsonObject.put("state", selectedIndex[0]);

                                    mUpdateTask = new UpdateTask(jsonObject);
                                    mUpdateTask.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Cancel 버튼 클릭시
                            }
                        });
                alertBuilder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView name;
        public TextView time;
        public TextView price;
        public TextView priceEtc;
        public TextView recommend;
        public TextView sale;
        public TextView state;
        public TextView update;
        public RelativeLayout saleLayout;
        public RelativeLayout stateLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            name = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            state = itemView.findViewById(R.id.state);
            price = itemView.findViewById(R.id.price);
            priceEtc = itemView.findViewById(R.id.price_etc);
            sale = itemView.findViewById(R.id.discount);
            recommend = itemView.findViewById(R.id.recommend);
            update = itemView.findViewById(R.id.update);
            saleLayout = itemView.findViewById(R.id.discount_layout);
            stateLayout = itemView.findViewById(R.id.state_layout);
        }
    }


    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject mJsonObject;

        UpdateTask(JSONObject jsonObject) {
            mJsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.productStateUpdate(mJsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mUpdateTask = null;

            Log.d("InsertTask", "mAdminTask : " + success);
            if(success) {
                Toast.makeText(mContext, "저장됬습니다.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "저장되지 않았습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
        }
    }
}

