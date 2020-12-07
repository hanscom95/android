package artech.com.semi.normal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import artech.com.semi.R;
import artech.com.semi.normal.adapter.QuestionAdapter;
import artech.com.semi.utility.BottomNavigationViewHelper;

public class QuestionActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    JSONArray mJsonArray;
    QuestionAdapter mAdapter;

    CheckBox mManyQuestionCheckbox, mUseQuestionCheckbox, mDeliveryCheckbox, mCancelCheckbox, mPurchaseCheckbox, mPointCheckbox, mAccountCheckbox, mAdCheckbox, mEtcCheckbox;
    TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_more);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        ImageView backImg = findViewById(R.id.app_bar_back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView questionText = findViewById(R.id.app_bar_question);
        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                startActivity(intent);
            }
        });


        mTitleText = findViewById(R.id.title_text);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(mContext,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new QuestionAdapter(mContext);

        mJsonArray = new JSONArray();

//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "세상에모든미끼는 어떤 서비스인가요?");
//            jsonObject.put("contents", "세상에모든미끼는 편리하고 빠르게 전국의 미끼 정보를 얻고 \n" +
//                    "비교하며 구매(예약)할 수 있는 국내 최고 스마트 미끼 어플 \n" +
//                    "서비스입니다.\n" +
//                    "\n" +
//                    "카테고리별 미끼 검색과 내 주변 가까이에 있는 업체 검색 기능!\n" +
//                    "언제든 미리예약 할 수 있습니다.\n" +
//                    "\n" +
//                    "이제 여기저기 찾지마시고 앱 하나로 편리~하게!!! \n" +
//                    "검색, 비교, 구매하시길 바랍니다♡");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "구매한 상품은 어디에서 확인할 수 있나요?");
//            jsonObject.put("contents", "test1");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "결제는 어떻게 하나요?");
//            jsonObject.put("contents", "test2");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "환불은 어떻게 하나요?");
//            jsonObject.put("contents", "test3");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "쿠폰사용은 어떻게 하나요?");
//            jsonObject.put("contents", "test4");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "작성한 리뷰는 어디서 보나요?");
//            jsonObject.put("contents", "test5");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "아이디 비밀번호를 잊어버렸어요.");
//            jsonObject.put("contents", "test6");
//            jsonObject.put("date", "Update 2018.08.01");
//            jsonArray.put(jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        mManyQuestionCheckbox = findViewById(R.id.many_question_button);
        mUseQuestionCheckbox = findViewById(R.id.use_question_button);
        mDeliveryCheckbox = findViewById(R.id.delivery_button);
        mCancelCheckbox = findViewById(R.id.cancel_button);
        mPurchaseCheckbox = findViewById(R.id.purchase_button);
        mPointCheckbox = findViewById(R.id.point_button);
        mAccountCheckbox = findViewById(R.id.account_button);
        mAdCheckbox = findViewById(R.id.ad_button);
        mEtcCheckbox = findViewById(R.id.etc_button);


        mManyQuestionCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mUseQuestionCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mDeliveryCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mCancelCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mPurchaseCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mPointCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mAccountCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mAdCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);
        mEtcCheckbox.setOnCheckedChangeListener(setOnCheckedChangeLisnter);


        getUseData();
        getDeliveryData();
        getCancelData();
        getPurchaseData();
        getPointData();
        getAccountData();
        getAdData();

        mAdapter.clear();
        mAdapter.addList(mJsonArray);
        mRecyclerView.setAdapter(mAdapter);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(mContext, NormalMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_basket:
                    intent = new Intent(mContext, ProductManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_favorites:
                    intent = new Intent(mContext, FavoritesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_my_info:
                    intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    finish();
                    return true;
                case R.id.navigation_more:
                    return true;
            }
            return false;
        }
    };

    private CompoundButton.OnCheckedChangeListener setOnCheckedChangeLisnter = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d("QuestionActivity", "setOnCheckedChangeLisnter");
            mManyQuestionCheckbox.setChecked(false);
            mUseQuestionCheckbox.setChecked(false);
            mDeliveryCheckbox.setChecked(false);
            mCancelCheckbox.setChecked(false);
            mPurchaseCheckbox.setChecked(false);
            mPointCheckbox.setChecked(false);
            mAccountCheckbox.setChecked(false);
            mAdCheckbox.setChecked(false);
            mEtcCheckbox.setChecked(false);

            if(b) {
                switch (compoundButton.getId()) {
                    case R.id.many_question_button: {
                        mManyQuestionCheckbox.setChecked(true);
                        mTitleText.setText("자주 묻는 질문");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getUseData();
                        getDeliveryData();
                        getCancelData();
                        getPurchaseData();
                        getPointData();
                        getAccountData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.use_question_button: {
                        mUseQuestionCheckbox.setChecked(true);
                        mTitleText.setText("이용문의");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getUseData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.delivery_button: {
                        mDeliveryCheckbox.setChecked(true);
                        mTitleText.setText("배송");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getDeliveryData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.cancel_button: {
                        mCancelCheckbox.setChecked(true);
                        mTitleText.setText("반품/교환/환불");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getCancelData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.purchase_button: {
                        mPurchaseCheckbox.setChecked(true);
                        mTitleText.setText("주문결제");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getPurchaseData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.point_button: {
                        mPointCheckbox.setChecked(true);
                        mTitleText.setText("적립금");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getPointData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.account_button: {
                        mAccountCheckbox.setChecked(true);
                        mTitleText.setText("회원서비스");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getAccountData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }
                    case R.id.ad_button: {
                        mAdCheckbox.setChecked(true);
                        mTitleText.setText("광고문의");

                        mAdapter.clear();
                        mJsonArray = new JSONArray();

                        getAdData();

                        mAdapter.clear();
                        mAdapter.addList(mJsonArray);
                        mAdapter.notifyDataSetChanged();

                        break;
                    }


                }
            }
        }
    };


    private void getUseData() {
        JSONObject jsonObject;
        try {

            jsonObject = new JSONObject();
            jsonObject.put("title", "미끼를 찾고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n원하시는 미끼는 홈 ->미끼검색을 누르시면 원하시는 미끼가 검색이 됩니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "가까운 낚시방을 찾고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n낚시방검색은 홈 -> 지역검색 -> 지역선택 -> 지도에서 원하는 낚시방 선택하셔서 찾으시면 됩니다\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "베스트미끼에 대해 알고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n세미 어플의 베스트 미끼는, 실제 구매한 수가 많은 미끼를 확인 하는 곳입니다. 인기 많고 인증된 미끼를 찾아보세요!\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "핫딜에 대해 알고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n핫딜은 할인률 이벤트가 들어간 상품을 확인 하실 수 있는 버튼입니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "바다/민물 에 대해 알고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n홈화면의 바다/민물은 미끼분류에 따라 쉽게 구분하여 검색하실 수 있는 버튼입니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "명예 낚시방에 대해 알고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n명예 낚시 방은 추천수 top, 구매율 top 인 업체를 선정하여 고객님들이 믿을 수 있는 가게가 어떤 곳 인지를 알려주는 버튼입니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "Talk 기능에 대해 알고싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n저희 세미 어플에는 Talk버튼이 있습니다.\n이 Talk에는 동출요청/ 조행기 / 낚시 정보/ 자유 게시판 세부기능이 있습니다. 자유롭게 글을 남기시고 확인하시면서 커뮤니티를 즐겨주세요!\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "찜목록에 대해 알려주세요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n내가 본 상품이나 업체 중 마음에 드신 곳을 찜 하시면, 찜 목록에서 확인이 가능합니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "마이 정보에 대해 알려주세요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n마이 정보는 내가 활동한 모든 정보를 볼 수 있는 곳입니다.\n" +
                    "나의 활동, 최근 본 상품 등을 확인 할 수 있습니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "공지사항은 어디에서 확인 하나요?");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n홈 화면 하단 오른쪽 더보기 버튼을 누르시면 확인 하실 수 있습니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("title", "고객문의는 어디에 있나요?");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n홈 화면 하단 오른쪽 더보기 버튼을 누르시면 확인 하실 수 있습니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDeliveryData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", "배송서비스를 받고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n현재 배송 서비스는 지원하고 있지 않습니다. 준비 중이오니 조금만 기다려주세요!\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCancelData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", "반품/교환/환불은 어떻게 하나요?");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n현재 반품/교환/환불 서비스는 지원하고 있지 않습니다. 준비 중이오니 조금만 기다려주세요!\n\n감사합니다.");
            /*jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n반품 및 교환은 수령 시 상품에 이상이 있거나 상품에 문제가 있을 때 가능합니다. 수령 시 상품상태 확인이 필수이며, 이상이 있다고 판단되면, 반품 및 교환이 가능합니다. 교환은 동일 제품일 경우, 다른 상품으로 교환을 전제로 하고 있습니다.\n저희 세미 어플의 환불 규정은 아래와 같습니다.\n" +
                    "1) 수령예정일 기준 7일~3일전 :100% 환불\nsecond) 수령예정일 기준 1일전: 80% 환불\nthird) 수령예정일 당일 및 NO SHOW : 환불불가\n상위 규정을 확인 하시고, 아래와 같이 진행 해주세요.\n" +
                    "마이 정보 ->구매내역(사용예정) ->주문취소(환불요청) \n" +
                    "마이 정보 -> 구매내역(완료) -> 환불요청 \n" +
                    "상품 수령예정일 3일전까지는 100%주문취소(환불)가 바로 가능하시며, 1일전, 당일 취소 건은 영업일수 7일 이내에 환불수수료를 제한 후 환불 되오니 이점 참고 바랍니다.\n\n감사합니다.");*/
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPurchaseData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
//            jsonObject.put("title", "구매한 상품은 어디에서 확인 할 수 있나요?");
//            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n구매하신 상품은 마이 정보 -> 구매내역에서 확인 가능 합니다.\n\n감사합니다.");
            jsonObject.put("title", "상품을 앱을 통해 구매하고 싶어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n현재 주문결제 서비스는 지원하고 있지 않습니다.  준비 중이오니 조금만 기다려주세요!\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);

//            jsonObject = new JSONObject();
//            jsonObject.put("title", "결제는 어떻게 하나요?");
//            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n구매를 원하시는 상품의 업체 정보 및 주의사항을 꼼꼼히 확인 하세요.\n" +
//                    "수령을 원하시는 날짜와 시간을 정확하게 입력하셔야 주문취소 및 환불수수료에 대한 피해가 없습니다. \n" +
//                    "결제수단은 무통장입금/ 신용카드/ 휴대폰 결제가 있습니다.\n" +
//                    "편한 방법의 결제수단을 선택 후, 이용규정 및 개인정보수집 및 이용 등의 약관에 동의하시면 결제가 완료 됩니다.\n" +
//                    " \n" +
//                    "주식회사 알텍은 통신판매중개자로서 통신판매의 당사자가 아니며, 상품의 예약,이용 및 환불등과 관련된 의무와 책임은 각 판매자에게 있습니다.\n\n감사합니다.");
//            jsonObject.put("date", "Update 2018.09.01");
//            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPointData() {
        JSONObject jsonObject;
        try {
//            jsonObject = new JSONObject();
//            jsonObject.put("title", "적립금은 어떻게 만드나요?");
//            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n적립금은 물건 구매 시 상품금액의 0.5%가 적립됩니다.\n" +
//                    "적립금은, 마이 정보 -> 적립금 에서 적립금액 및 적립내역을 확인 하실 수 있습니다.\n\n감사합니다.");
//            jsonObject.put("date", "Update 2018.09.01");
//            mJsonArray.put(jsonObject);


//            jsonObject = new JSONObject();
//            jsonObject.put("title", "적립금은 어떻게 사용하나요?");
//            jsonObject.put("contents", "적립금은 상품 구매 시, 사용하실 수 있습니다.\n\n감사합니다.");
//            jsonObject.put("date", "Update 2018.09.01");
//            mJsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("title", "적립금에 대해 궁금해요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n현재 적립금 서비스는 지원하고 있지 않습니다.  준비 중이오니 조금만 기다려주세요!\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAccountData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", "회원가입은 어떻게 하나요?");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n초기화면에서 회원가입을 하시면 세미 어플을 이용하실 수 있습니다. 주로 사용하시는 SNS 를 통해서도 가입하실 수 있습니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("title", "회원탈퇴는 어떻게 하나요?");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n회원탈퇴는 마이 정보 -> 회원탈퇴를 하시면 됩니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("title", "비밀번호를 잊어버렸어요.");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n비밀번호 안내는 문의를 통해서 남기시거나, 유선연락(070-8676-1662)을 주시면 안내해드리겠습니다.\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAdData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", "가맹점 가입을 하고 싶어요");
            jsonObject.put("contents", "안녕하세요. 세상에 모든 미끼, 세미 팀입니다.\n저희 세미팀은 가맹점가입에 관심을 가져주셔서 대단히 감사드립니다!\n" +
                    "가맹점가입문의는, 하단 더보기 -> 광고문의 에서 연락처 or 이메일주소를 남겨주시면 성실히 답변 드리겠습니다.\n" +
                    "가입만으로 내가게 홍보는 저절로! \n" +
                    "부담갖지 마시고 연락주세요~\n\n감사합니다.");
            jsonObject.put("date", "Update 2018.09.01");
            mJsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
