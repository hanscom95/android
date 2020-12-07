package artech.com.semi.normal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import artech.com.semi.R;
import artech.com.semi.normal.SearchLocationActivity;

/**
 * Created by moon on 2018-05-09.
 */

public class SearchLocationFragment extends Fragment {

    CheckBox mTotalCheckbox, mSeoulCheckbox, mGyeonggiCheckbox, mIncheonCheckbox, mChungbukCheckbox, mChungnamCheckbox,mJeonbukCheckbox, mJeonnamCheckbox, mGyeonnamCheckbox, mGyeonbukCheckbox, mUlsanCheckbox, mBusanCheckbox, mGangwonCheckbox;

    Button mSelectButton;

    public SearchLocationFragment() {
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_location_search, container, false);

        mTotalCheckbox = view.findViewById(R.id.total_checkbox);
        mSeoulCheckbox = view.findViewById(R.id.seoul_checkbox);
        mGyeonggiCheckbox = view.findViewById(R.id.gyeonggi_checkbox);
        mIncheonCheckbox = view.findViewById(R.id.Incheon_checkbox);
        mChungbukCheckbox = view.findViewById(R.id.chungbuk_checkbox);
        mChungnamCheckbox = view.findViewById(R.id.chungnam_checkbox);
        mJeonbukCheckbox = view.findViewById(R.id.jeonbuk_checkbox);
        mJeonnamCheckbox = view.findViewById(R.id.jeonnam_checkbox);
        mGyeonbukCheckbox = view.findViewById(R.id.gyeonbuk_checkbox);
        mGyeonnamCheckbox = view.findViewById(R.id.gyeongnam_checkbox);
        mUlsanCheckbox = view.findViewById(R.id.ulsan_checkbox);
        mBusanCheckbox = view.findViewById(R.id.busan_checkbox);
        mGangwonCheckbox = view.findViewById(R.id.gangwon_checkbox);

        mSelectButton = view.findViewById(R.id.select_button);

        mTotalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mSeoulCheckbox.setChecked(true);
                    mGyeonggiCheckbox.setChecked(true);
                    mIncheonCheckbox.setChecked(true);
                    mChungbukCheckbox.setChecked(true);
                    mChungnamCheckbox.setChecked(true);
                    mJeonbukCheckbox.setChecked(true);
                    mJeonnamCheckbox.setChecked(true);
                    mGyeonbukCheckbox.setChecked(true);
                    mGyeonnamCheckbox.setChecked(true);
                    mUlsanCheckbox.setChecked(true);
                    mBusanCheckbox.setChecked(true);
                    mGangwonCheckbox.setChecked(true);
                }else {
                    mSeoulCheckbox.setChecked(false);
                    mGyeonggiCheckbox.setChecked(false);
                    mIncheonCheckbox.setChecked(false);
                    mChungbukCheckbox.setChecked(false);
                    mChungnamCheckbox.setChecked(false);
                    mJeonbukCheckbox.setChecked(false);
                    mJeonnamCheckbox.setChecked(false);
                    mGyeonbukCheckbox.setChecked(false);
                    mGyeonnamCheckbox.setChecked(false);
                    mUlsanCheckbox.setChecked(false);
                    mBusanCheckbox.setChecked(false);
                    mGangwonCheckbox.setChecked(false);
                }
            }
        });

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] checked = new int[12];
                String str = "";
                if(!mTotalCheckbox.isChecked()) {
                    if(mSeoulCheckbox.isChecked()){
                        checked[0] = 1;
                        str = "서울";
                    }else {
                        checked[0] = 0;
                    }

                    if(mGyeonggiCheckbox.isChecked()){
                        if(checked[0] == 1) {
                            str += "|";
                        }

                        checked[1] = 1;
                        str += "경기";
                    }else {
                        checked[1] = 0;
                    }

                    if(mIncheonCheckbox.isChecked()){
                        if(checked[1] == 1) {
                            str += "|";
                        }

                        checked[2] = 1;
                        str += "인천";
                    }else {
                        checked[2] = 0;
                    }

                    if(mChungbukCheckbox.isChecked()){
                        if(checked[2] == 1) {
                            str += "|";
                        }

                        checked[3] = 1;
                        str += "충청북도";
                    }else {
                        checked[3] = 0;
                    }

                    if(mChungnamCheckbox.isChecked()){
                        if(checked[3] == 1) {
                            str += "|";
                        }

                        checked[4] = 1;
                        str += "충청남도";
                    }else {
                        checked[4] = 0;
                    }

                    if(mJeonbukCheckbox.isChecked()){
                        if(checked[4] == 1) {
                            str += "|";
                        }

                        checked[5] = 1;
                        str += "전라북도";
                    }else {
                        checked[5] = 0;
                    }

                    if(mJeonnamCheckbox.isChecked()){
                        if(checked[5] == 1) {
                            str += "|";
                        }

                        checked[6] = 1;
                        str += "전라남도";
                    }else {
                        checked[6] = 0;
                    }

                    if(mGyeonbukCheckbox.isChecked()){
                        if(checked[6] == 1) {
                            str += "|";
                        }

                        checked[7] = 1;
                        str += "경상북도";
                    }else {
                        checked[7] = 0;
                    }

                    if(mGyeonnamCheckbox.isChecked()){
                        if(checked[7] == 1) {
                            str += "|";
                        }

                        checked[8] = 1;
                        str += "경상남도";
                    }else {
                        checked[8] = 0;
                    }

                    if(mUlsanCheckbox.isChecked()){
                        if(checked[8] == 1) {
                            str += "|";
                        }

                        checked[9] = 1;
                        str += "울산";
                    }else {
                        checked[9] = 0;
                    }

                    if(mBusanCheckbox.isChecked()){
                        if(checked[9] == 1) {
                            str += "|";
                        }

                        checked[10] = 1;
                        str += "부산";
                    }else {
                        checked[10] = 0;
                    }

                    if(mGangwonCheckbox.isChecked()){
                        if(checked[10] == 1) {
                            str += "|";
                        }

                        checked[11] = 1;
                        str += "강원";
                    }else {
                        checked[11] = 0;
                    }
                }
                ((SearchLocationActivity)getActivity()).mapCall(str, 0);
            }
        });

        return view ;
    }

}
