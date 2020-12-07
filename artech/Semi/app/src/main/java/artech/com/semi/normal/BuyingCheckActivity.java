package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import artech.com.semi.R;

public class BuyingCheckActivity extends AppCompatActivity {

    Context mContext;

    Button mPaymentButton;

    TextView mTitleText, mContentsText;
    ImageView mFreshnessImg;

    String mId, mTitle, mShop;
    int mValue = -1;
    int mFreshness = -1;
    int mPrice1, mPrice2, mPrice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_buying_check);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPaymentButton = findViewById(R.id.payment_button);

        mTitleText = findViewById(R.id.name_text);
        mContentsText = findViewById(R.id.contents_text);

        mFreshnessImg = findViewById(R.id.fresh_img);

        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(false) {
                    Toast.makeText(mContext, "서비스 준비중 입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra("id", mId);
                intent.putExtra("title",mTitle);
                intent.putExtra("value",mValue);
                intent.putExtra("freshness",mFreshness);
                intent.putExtra("shop",mShop);
                intent.putExtra("price1",mPrice1*mValue);
                intent.putExtra("price2",mPrice2*mValue);
                intent.putExtra("price3",mPrice3*mValue);
                startActivity(intent);
                finish();
            }
        });


        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mTitle = intent.getStringExtra("title");
        mShop = intent.getStringExtra("shop");
        mValue = intent.getIntExtra("value", 0);
        mFreshness = intent.getIntExtra("freshness", 0);
        mPrice1 = intent.getIntExtra("price1", 0);
        mPrice2 = intent.getIntExtra("price2", 0);
        mPrice3 = intent.getIntExtra("price3", 0);

        String freshness = "";

        if(mFreshness == 0) {
            freshness = "下";
            mFreshnessImg.setImageResource(R.mipmap.img_fresh_poor_2);
        }else if(mFreshness == 1) {
            freshness = "中";
            mFreshnessImg.setImageResource(R.mipmap.img_fresh_good_2);
        }else if(mFreshness == 2) {
            freshness = "上";
            mFreshnessImg.setImageResource(R.mipmap.img_fresh_excellent_2);
        }

        mTitleText.setText(mTitle);
        mContentsText.setText(mShop+" · " + mValue + "개 · 미끼신선도" + freshness);





        Log.d("BuyingCheckActivity", "id : " + mId + " / value : " + mValue + " / freshness : " + mFreshness );
    }

}
