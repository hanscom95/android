package artech.com.semi.normal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import artech.com.semi.R;
import artech.com.semi.utility.DBManager;
import artech.com.semi.utility.ExpandableAdapter;
import artech.com.semi.utility.NetworkConnection;
import artech.com.semi.utility.Util;
import artech.com.semi.utility.payment.ImportActivity;

public class PaymentActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    ExpandableListView mExpandableListView;
    ExpandableRelativeLayout mExpandLayout;
    RelativeLayout mDateLayout, mTimeLayout, mTermsLayout, mExpandTermsLayout;
    TextView mDateText, mDayText, mHalfTimeText, mTimeText, mReservationValueText, mDiscountValueText, mTotalValueText;
    EditText mBookerNameEdit, mBookerPhoneEdit;
    CheckBox mUnBankBookCheckbox, mCreditCardCheckbox, mMobileBillingCheckbox, mTermsTotalCheckbox, mFirstTermCheckbox, mSecondTermCheckbox, mThirdTermCheckbox, mFourthTermCheckbox;
    Button mPaymentButton;

    InsertTask mInsertTask;

    String mId, mTitle, mDate, mShop;
    int mValue = -1;
    int mFreshness = -1;
    int mPrice1, mPrice2, mPrice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "semi.db", null, 1);

        setContentView(R.layout.activity_payment);
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

//        mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

        mDateLayout = findViewById(R.id.date_layout);
        mTimeLayout = findViewById(R.id.time_layout);

        mTermsLayout = findViewById(R.id.seventh_detail_layout);
        mExpandTermsLayout = findViewById(R.id.expandable_first_layout);
        mExpandLayout = findViewById(R.id.expandableLayout);

        mDateText = findViewById(R.id.month_date_text);
        mDayText = findViewById(R.id.day_text);
        mHalfTimeText = findViewById(R.id.half_day_text);
        mTimeText = findViewById(R.id.time_text);
        mReservationValueText = findViewById(R.id.reservation_amount_value_text);
        mDiscountValueText = findViewById(R.id.discount_amount_value_text);
        mTotalValueText = findViewById(R.id.total_amount_value_text);

        mBookerNameEdit = findViewById(R.id.booker_name_edit);
        mBookerPhoneEdit = findViewById(R.id.booker_phone_edit);

        mUnBankBookCheckbox = findViewById(R.id.un_bank_book_checkbox);
        mCreditCardCheckbox = findViewById(R.id.credit_card_checkbox);
        mMobileBillingCheckbox = findViewById(R.id.mobile_billing_checkbox);
        mTermsTotalCheckbox = findViewById(R.id.terms_text);
        mFirstTermCheckbox = findViewById(R.id.first_terms_text);
        mSecondTermCheckbox = findViewById(R.id.second_terms_text);
        mThirdTermCheckbox = findViewById(R.id.third_terms_text);
        mFourthTermCheckbox = findViewById(R.id.fourth_terms_text);

        mPaymentButton = findViewById(R.id.payment_button);

        mDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar ca = Calendar.getInstance();
                        ca.set(i, i1, i2);
                        mDateText.setText(Util.toDateFormat(i1+1)+"."+Util.toDateFormat(i2));
                        mDate = i+"-" + Util.toDateFormat(i1+1)+"-"+Util.toDateFormat(i2);


                        int dy = ca.get(Calendar.DAY_OF_WEEK);
                        String dyT = "";
                        if(dy == 1) {
                            dyT = "일요일";
                        }else if(dy == 2) {
                            dyT = "월요일";
                        }else if(dy == 3) {
                            dyT = "화요일";
                        }else if(dy == 4) {
                            dyT = "수요일";
                        }else if(dy == 5) {
                            dyT = "목요일";
                        }else if(dy == 6) {
                            dyT = "금요일";
                        }else if(dy == 7) {
                            dyT = "토요일";
                        }

                        mDayText.setText(dyT);
                    }
                }, year, month, day);

                calendar.add(Calendar.MONTH, 1);
                dialog.getDatePicker().setMinDate(new Date().getTime());
                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // After 31 Days from Now
                dialog.show();
            }
        });


        mTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.d("timeclk","i : " + i + " , i1 : " + i1);

                        String am_pm = "";
                        if(i < 12) {
                            am_pm = "오전";
                        } else {
                            am_pm = "오후";
                        }

                        mTimeText.setText(Util.toDateFormat(i)+":"+Util.toDateFormat(i1));
                        mHalfTimeText.setText(am_pm);

                    }
                }, 8,0,true);

                dialog.show();
            }
        });

        mUnBankBookCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mCreditCardCheckbox.setChecked(false);
                    mMobileBillingCheckbox.setChecked(false);
                }
            }
        });

        mCreditCardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mUnBankBookCheckbox.setChecked(false);
                    mMobileBillingCheckbox.setChecked(false);
                }
            }
        });

        mMobileBillingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mCreditCardCheckbox.setChecked(false);
                    mUnBankBookCheckbox.setChecked(false);
                }
            }
        });

        mPaymentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(mBookerNameEdit.getText().toString() == "" || "".equals(mBookerNameEdit.getText().toString())) {
                    Toast.makeText(mContext, "예약자 이름을 설정하신후 진행해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mBookerPhoneEdit.getText().toString() == "" || "".equals(mBookerPhoneEdit.getText().toString())) {
                    Toast.makeText(mContext, "휴대폰 번호 설정하신후 진행해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mTermsTotalCheckbox.isChecked()) {
//                Intent intent = new Intent(mContext, PaymentSuccessActivity.class);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(mId);
                    arrayList.add(mDbManager.selectUserId());
                    if(mUnBankBookCheckbox.isChecked()) {
                        arrayList.add("vbank");
                        arrayList.add("0");
                    }else if(mCreditCardCheckbox.isChecked()) {
                        arrayList.add("card");
                        arrayList.add("1");
                    }else if(mMobileBillingCheckbox.isChecked()) {
                        arrayList.add("phone");
                        arrayList.add("second");
                    }else {
                        arrayList.add("card");
                        arrayList.add("0");
                    }

                    arrayList.add(mTitle);
                    arrayList.add(mPrice3+"");
                    arrayList.add("test001@naver.com");
                    arrayList.add(mBookerNameEdit.getText().toString());
                    arrayList.add(mBookerPhoneEdit.getText().toString());
                    arrayList.add(mShop);
                    arrayList.add(mDate + " " + mTimeText.getText().toString()+":00");
                    arrayList.add(mValue+"");
                    arrayList.add(mPrice2+"");
                    arrayList.add("0"); // coupon
                    arrayList.add("0"); // point

                    Intent intent = new Intent(mContext, ImportActivity.class);//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putStringArrayListExtra("arrayList", arrayList);
                    startActivity(intent);
//                finish();

//                    try {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("user_id", mDbManager.selectUserId());
//                        jsonObject.put("product_id", mId);
//                        jsonObject.put("receipt_dt", mDate + " " + mTimeText.getText().toString() + ":00");
//                        jsonObject.put("price", mPrice3);
//                        jsonObject.put("quantity", mValue);
//                        jsonObject.put("discount", mPrice2);
//                        jsonObject.put("state", 0);
//                        jsonObject.put("name", mBookerNameEdit.getText().toString());
//                        jsonObject.put("phone", mBookerPhoneEdit.getText().toString());
//                        jsonObject.put("shop", mShop);

//                        mInsertTask = new InsertTask(jsonObject);
//                        mInsertTask.execute();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }else {
                    Toast.makeText(mContext, "필수 동의 항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandLayout.expand();
                mTermsLayout.setVisibility(View.GONE);
            }
        });

        mExpandTermsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandLayout.collapse();
                mTermsLayout.setVisibility(View.VISIBLE);
            }
        });


        mTermsTotalCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTermsTotalCheckbox.isChecked()) {
                    mFirstTermCheckbox.setChecked(true);
                    mSecondTermCheckbox.setChecked(true);
                    mThirdTermCheckbox.setChecked(true);
                    mFourthTermCheckbox.setChecked(true);
                }else {
                    mFirstTermCheckbox.setChecked(false);
                    mSecondTermCheckbox.setChecked(false);
                    mThirdTermCheckbox.setChecked(false);
                    mFourthTermCheckbox.setChecked(false);
                }
            }
        });

        mFirstTermCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFirstTermCheckbox.isChecked()) {
                    mTermsTotalCheckbox.setChecked(true);
                    mSecondTermCheckbox.setChecked(true);
                    mThirdTermCheckbox.setChecked(true);
                    mFourthTermCheckbox.setChecked(true);
                }else {
                    mTermsTotalCheckbox.setChecked(false);
                    mSecondTermCheckbox.setChecked(false);
                    mThirdTermCheckbox.setChecked(false);
                    mFourthTermCheckbox.setChecked(false);
                }
            }
        });

        mSecondTermCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSecondTermCheckbox.isChecked() && mThirdTermCheckbox.isChecked() && mFourthTermCheckbox.isChecked()) {
                    mTermsTotalCheckbox.setChecked(true);
                    mFirstTermCheckbox.setChecked(true);
                }else {
                    mTermsTotalCheckbox.setChecked(false);
                    mFirstTermCheckbox.setChecked(false);
                }
            }
        });

        mThirdTermCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSecondTermCheckbox.isChecked() && mThirdTermCheckbox.isChecked() && mFourthTermCheckbox.isChecked()) {
                    mTermsTotalCheckbox.setChecked(true);
                    mFirstTermCheckbox.setChecked(true);
                }else {
                    mTermsTotalCheckbox.setChecked(false);
                    mFirstTermCheckbox.setChecked(false);
                }
            }
        });

        mFourthTermCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSecondTermCheckbox.isChecked() && mThirdTermCheckbox.isChecked() && mFourthTermCheckbox.isChecked()) {
                    mTermsTotalCheckbox.setChecked(true);
                    mFirstTermCheckbox.setChecked(true);
                }else {
                    mTermsTotalCheckbox.setChecked(false);
                    mFirstTermCheckbox.setChecked(false);
                }
            }
        });

        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mTitle = intent.getStringExtra("title");
        mShop = intent.getStringExtra("shop");
        mValue = intent.getIntExtra("value", 0);
        mFreshness = intent.getIntExtra("freshness", 0);
        mPrice1 = intent.getIntExtra("price1", 0);
        mPrice2 = intent.getIntExtra("price2", 0);
        mPrice3 = intent.getIntExtra("price3", 0);

        mReservationValueText.setText(Util.toNumFormat(mPrice1) + "원");
        mDiscountValueText.setText(Util.toNumFormat(mPrice2) + "원");
        mTotalValueText.setText(Util.toNumFormat(mPrice3) + "원");

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> subList = new ArrayList<>();
        ArrayList<ArrayList<String>> subMap = new ArrayList<>();

        titleList.add("이용 규정, 개인정보 수집 및 이용, 개인정보 제 3자 제공 동의");
        subList.add("이용 규칙 및 취소/환불규정 동의");
        subList.add("개인정보 수집 및 이용 동의");
        subList.add("개인정보 제 3자 제공 동의");

        subMap.add(subList);

        ExpandableAdapter adapter = new ExpandableAdapter(this, titleList, subMap);
//        mExpandableListView.setAdapter(adapter);


        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int dy = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);


        String dyT = "";
        if(dy == 1) {
            dyT = "일요일";
        }else if(dy == 2) {
            dyT = "월요일";
        }else if(dy == 3) {
            dyT = "화요일";
        }else if(dy == 4) {
            dyT = "수요일";
        }else if(dy == 5) {
            dyT = "목요일";
        }else if(dy == 6) {
            dyT = "금요일";
        }else if(dy == 7) {
            dyT = "토요일";
        }

        String am_pm = "";
        if(hour < 12) {
            am_pm = "오전";
        } else {
            am_pm = "오후";
        }



        mDate = year+"-" + Util.toDateFormat(month+1)+"-"+Util.toDateFormat(day);
        mDateText.setText(Util.toDateFormat(month+1)+"."+Util.toDateFormat(day));
        mDayText.setText(dyT);

        mTimeText.setText(Util.toDateFormat(hour)+":"+Util.toDateFormat(minute));
        mHalfTimeText.setText(am_pm);
    }


    private class InsertTask extends AsyncTask<Void, Void, Boolean> {
        NetworkConnection network;
        JSONObject jsonObject;

        InsertTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean connect = false;

            try {
                network = new NetworkConnection(mContext);

                connect = network.purchaseInsert(jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return connect;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mInsertTask = null;
            String path = null;

            if(success) {
//                Intent intent = new Intent(mContext, PaymentSuccessActivity.class);
                Intent intent = new Intent(mContext, NormalMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
            }else {
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mResult);
                Log.d("BusinessMainActivity", "not success mAdminTask : " + network.mMsg);
            }

        }

        @Override
        protected void onCancelled() {
            mInsertTask = null;
        }
    }

}
