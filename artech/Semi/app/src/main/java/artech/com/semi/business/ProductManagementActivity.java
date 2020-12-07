package artech.com.semi.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.business.fragment.ProductManagementFragment;
import artech.com.semi.utility.BottomNavigationViewHelper;

public class ProductManagementActivity extends AppCompatActivity {

    Context mContext;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    RelativeLayout mTitleLayout, mSearchLayout, mProductLayout, mFilterLayout;
    Spinner mSpinner;
    ImageView mBackImg;


    CheckBox mTotalCheckbox, mKrillCheckbox, mShrimpCheckbox, mEarthwormCheckbox, mSeaSquirtCheckbox, mConchCheckbox, mCrapCheckbox, mSeaUrchinCheckbox, mMudfishCheckbox, mCornCheckbox, mMuckwormCheckbox,
            mMixCheckbox, mFishingPasteCheckbox, mEtcCheckbox;

    Button mSelectButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_product_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBackImg = findViewById(R.id.app_bar_back_img);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitleLayout = toolbar.findViewById(R.id.toolbar_title_layout);
        SearchView searchView = toolbar.findViewById(R.id.search_view);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getColor(R.color.black));
        searchEditText.setHintTextColor(getColor(R.color.black));
        searchEditText.setTextSize(11);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ProductManagementFragment fragment = (ProductManagementFragment) mFragmentManager.findFragmentById(R.id.product_management_fragment);
                fragment.findList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTitleLayout.setVisibility(View.VISIBLE);
                mBackImg.setVisibility(View.VISIBLE);
//                mFragment.recommendSort();
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProductManagementFragment fragment = (ProductManagementFragment) mFragmentManager.findFragmentById(R.id.product_management_fragment);

                mTitleLayout.setVisibility(View.GONE);
                mBackImg.setVisibility(View.GONE);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_product_manage);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSpinner = findViewById(R.id.sort_spinner);
        mSearchLayout = findViewById(R.id.search_layout);
        mProductLayout = findViewById(R.id.product_layout);
        mFilterLayout = findViewById(R.id.filter_layout);


        mTotalCheckbox = findViewById(R.id.total_checkbox);
        mKrillCheckbox = findViewById(R.id.krill_checkbox);
        mShrimpCheckbox = findViewById(R.id.shrimp_checkbox);
        mEarthwormCheckbox = findViewById(R.id.earthworm_checkbox);
        mSeaSquirtCheckbox = findViewById(R.id.sea_​​squirt_checkbox);
        mConchCheckbox = findViewById(R.id.conch_checkbox);
        mCrapCheckbox = findViewById(R.id.crap_checkbox);
        mSeaUrchinCheckbox = findViewById(R.id.sea_urchin_checkbox);
        mMudfishCheckbox = findViewById(R.id.mudfish_checkbox);
        mMuckwormCheckbox = findViewById(R.id.muckworm_checkbox);
        mCornCheckbox = findViewById(R.id.corn_checkbox);
        mMixCheckbox = findViewById(R.id.mix_checkbox);
        mFishingPasteCheckbox = findViewById(R.id.fishing_paste_checkbox);
        mEtcCheckbox = findViewById(R.id.etc_checkbox);

        mSelectButton = findViewById(R.id.select_button);

        mFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductLayout.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.VISIBLE);
            }
        });

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.product_management_fragment, new ProductManagementFragment(mContext)).commit();

        mTotalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mKrillCheckbox.setChecked(true);
                    mShrimpCheckbox.setChecked(true);
                    mEarthwormCheckbox.setChecked(true);
                    mSeaSquirtCheckbox.setChecked(true);
                    mConchCheckbox.setChecked(true);
                    mCrapCheckbox.setChecked(true);
                    mSeaUrchinCheckbox.setChecked(true);
                    mMudfishCheckbox.setChecked(true);
                    mMuckwormCheckbox.setChecked(true);
                    mCornCheckbox.setChecked(true);
                    mMixCheckbox.setChecked(true);
                    mFishingPasteCheckbox.setChecked(true);
                    mEtcCheckbox.setChecked(true);
                }else {
                    mKrillCheckbox.setChecked(false);
                    mShrimpCheckbox.setChecked(false);
                    mEarthwormCheckbox.setChecked(false);
                    mSeaSquirtCheckbox.setChecked(false);
                    mConchCheckbox.setChecked(false);
                    mCrapCheckbox.setChecked(false);
                    mSeaUrchinCheckbox.setChecked(false);
                    mMudfishCheckbox.setChecked(false);
                    mMuckwormCheckbox.setChecked(false);
                    mCornCheckbox.setChecked(false);
                    mMixCheckbox.setChecked(false);
                    mFishingPasteCheckbox.setChecked(false);
                    mEtcCheckbox.setChecked(false);
                }
            }
        });

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] checked = new int[15];
                String str = "";

                if(!mTotalCheckbox.isChecked()) {
                    if(mKrillCheckbox.isChecked()){
                        checked[0] = 1;
                        str = "크릴";
                    }else {
                        checked[0] = 0;
                    }

                    if(mShrimpCheckbox.isChecked()){
                        if(checked[0] == 1) {
                            str += "|";
                        }

                        checked[1] = 1;
                        str += "민물새우|줄새우|생이|가에비|호산새우|도랑새우|토하|중하|꽃새우|보리새우|파래새우|파란새우|깐새우|생새우|냉동새우|곤쟁이";
                    }else {
                        checked[1] = 0;
                    }

                    if(mEarthwormCheckbox.isChecked()){
                        if(checked[1] == 1) {
                            str += "|";
                        }

                        checked[2] = 1;
                        str += "참갯지렁이|혼무시|홍갯지렁이|홍개비|홍거시|청갯지렁이|청개비|청거시|실갯지렁이|넓적집갯지렁이|" +
                                "돌굴치|안점꽃갯지렁이|풀굴치|갯지렁이|치로리미갑갯지렁이|혈충|까막지렁이|청지렁이|꽃지렁이|" +
                                "산지렁이|거머리|물지렁이";
                    }else {
                        checked[2] = 0;
                    }

                    if(mSeaSquirtCheckbox.isChecked()){
                        if(checked[2] == 1) {
                            str += "|";
                        }

                        checked[3] = 1;
                        str += "개불|멍게|비단멍게|돌멍게";
                    }else {
                        checked[3] = 0;
                    }

                    if(mConchCheckbox.isChecked()){
                        if(checked[3] == 1) {
                            str += "|";
                        }

                        checked[4] = 1;
                        str += "바지락|전복|전복소라|참소라|삐뚤이소라|뿔소라|골뱅이|가리비|" +
                                "해만가리비|홍가리비|비단가리비|대합|동죽조개|빛조개|명주조개|" +
                                "모시조개|밀조개|민들조개|맛조개|웅피조개|물바지락|백합|백생합|비단조개|" +
                                "돌조개|참조개|참꼬막|꼬막|생합|참조개|새꼬막|피조개|키조개|민들조개|명주조개" +
                                "칼조개|홍합|따개비|굴|오분자기";
                    }else {
                        checked[4] = 0;
                    }

                    if(mCrapCheckbox.isChecked()){
                        if(checked[4] == 1) {
                            str += "|";
                        }

                        checked[5] = 1;
                        str += "쏙|설게|소라게|게고동|풀게|장돌게|방게|갯강구|강구";
                    }else {
                        checked[5] = 0;
                    }

                    if(mSeaUrchinCheckbox.isChecked()){
                        if(checked[5] == 1) {
                            str += "|";
                        }

                        checked[6] = 1;
                        str += "말똥성게|보라성게|성게";
                    }else {
                        checked[6] = 0;
                    }

                    if(mMudfishCheckbox.isChecked()){
                        if(checked[6] == 1) {
                            str += "|";
                        }

                        checked[7] = 1;
                        str += "미꾸라지";
                    }else {
                        checked[7] = 0;
                    }

                    if(mMuckwormCheckbox.isChecked()){
                        if(checked[7] == 1) {
                            str += "|";
                        }

                        checked[8] = 1;
                        str += "구더기";
                    }else {
                        checked[8] = 0;
                    }

                    if(mCornCheckbox.isChecked()){
                        if(checked[8] == 1) {
                            str += "|";
                        }

                        checked[9] = 1;
                        str += "옥수수";
                    }else {
                        checked[9] = 0;
                    }

                    if(mMixCheckbox.isChecked()){
                        if(checked[9] == 1) {
                            str += "|";
                        }

                        checked[10] = 1;
                        str += "집어제";
                    }else {
                        checked[10] = 0;
                    }

                    if(mFishingPasteCheckbox.isChecked()){
                        if(checked[10] == 1) {
                            str += "|";
                        }

                        checked[11] = 1;
                        str += "떡밥|어분";
                    }else {
                        checked[11] = 0;
                    }

                    if(mEtcCheckbox.isChecked()){
                        if(checked[11] == 1) {
                            str += "|";
                        }

                        checked[12] = 1;
                        str += "참붕어|오징어살|오징어내장|낙지|문어|꼴뚜기|땅강아지|우동|깻묵|번데기|염장고등어|기타";
                    }else {
                        checked[12] = 0;
                    }
                }else {
                    str = "크릴|";
                    str += "민물새우|줄새우|생이|가에비|호산새우|도랑새우|토하|중하|꽃새우|보리새우|파래새우|파란새우|깐새우|생새우|냉동새우|곤쟁이|";
                    str += "참갯지렁이|혼무시|홍갯지렁이|홍개비|홍거시|청갯지렁이|청개비|청거시|실갯지렁이|넓적집갯지렁이|" +
                            "돌굴치|안점꽃갯지렁이|풀굴치|갯지렁이|치로리미갑갯지렁이|혈충|까막지렁이|청지렁이|꽃지렁이|" +
                            "산지렁이|거머리|물지렁이|";
                    str += "개불|멍게|비단멍게|돌멍게|";
                    str += "바지락|전복|전복소라|참소라|삐뚤이소라|뿔소라|골뱅이|가리비|" +
                            "해만가리비|홍가리비|비단가리비|대합|동죽조개|빛조개|명주조개|" +
                            "모시조개|밀조개|민들조개|맛조개|웅피조개|물바지락|백합|백생합|비단조개|" +
                            "돌조개|참조개|참꼬막|꼬막|생합|참조개|새꼬막|피조개|키조개|민들조개|명주조개|" +
                            "칼조개|홍합|따개비|굴|오분자기|";
                    str += "쏙|설게|소라게|게고동|풀게|장돌게|방게|갯강구|강구|";
                    str += "말똥성게|보라성게|성게|";
                    str += "미꾸라지|구더기|옥수수|집어제|떡밥|어분|";
                    str += "참붕어|오징어살|오징어내장|낙지|문어|꼴뚜기|땅강아지|우동|깻묵|번데기|염장고등어|기타";
                }

                mSearchLayout.setVisibility(View.GONE);
                mProductLayout.setVisibility(View.VISIBLE);

                ProductManagementFragment fragment = (ProductManagementFragment) mFragmentManager.findFragmentById(R.id.product_management_fragment);
                fragment.searchProduct(str);
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProductManagementFragment fragment = (ProductManagementFragment) mFragmentManager.findFragmentById(R.id.product_management_fragment);
                if(i  == 0) {
                    fragment.dateSort();
                }else if(i  == 2) {
                    fragment.recommendSort();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected void onResume() {
        Log.d("MyInfoActivity", "onResume");
        ProductManagementFragment fragment = (ProductManagementFragment) mFragmentManager.findFragmentById(R.id.product_management_fragment);
        fragment.searchProduct("");

        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, BusinessMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_insert:
                    intent = new Intent(mContext, ProductInsertActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_product_manage:
//                    intent = new Intent(mContext, ProductManagementActivity.class);
//                    startActivity(intent);
//                    finish();
                    return true;
//                case R.id.navigation_sale:
//                    intent = new Intent(mContext, SaleManagementActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    finish();
//                    return true;
                case R.id.navigation_question:
                    intent = new Intent(mContext, QuestionManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    return true;
            }
            return false;
        }
    };
}
