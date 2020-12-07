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

import artech.com.semi.R;
import artech.com.semi.normal.SearchLocationActivity;

/**
 * Created by moon on 2018-05-09.
 */

public class SearchBaitFragment extends Fragment {

    CheckBox mTotalCheckbox, mKrillCheckbox, mShrimpCheckbox, mEarthwormCheckbox, mSeaSquirtCheckbox, mConchCheckbox, mCrapCheckbox, mSeaUrchinCheckbox, mMudfishCheckbox, mCornCheckbox, mMuckwormCheckbox,
            mMixCheckbox, mFishingPasteCheckbox, mEtcCheckbox;

    Button mSelectButton;

    public SearchBaitFragment() {
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bait_search, container, false);

        mTotalCheckbox = view.findViewById(R.id.total_checkbox);
        mKrillCheckbox = view.findViewById(R.id.krill_checkbox);
        mShrimpCheckbox = view.findViewById(R.id.shrimp_checkbox);
        mEarthwormCheckbox = view.findViewById(R.id.earthworm_checkbox);
        mSeaSquirtCheckbox = view.findViewById(R.id.sea_​​squirt_checkbox);
        mConchCheckbox = view.findViewById(R.id.conch_checkbox);
        mCrapCheckbox = view.findViewById(R.id.crap_checkbox);
        mSeaUrchinCheckbox = view.findViewById(R.id.sea_urchin_checkbox);
        mMudfishCheckbox = view.findViewById(R.id.mudfish_checkbox);
        mMuckwormCheckbox = view.findViewById(R.id.muckworm_checkbox);
        mCornCheckbox = view.findViewById(R.id.corn_checkbox);
        mMixCheckbox = view.findViewById(R.id.mix_checkbox);
        mFishingPasteCheckbox = view.findViewById(R.id.fishing_paste_checkbox);
        mEtcCheckbox = view.findViewById(R.id.etc_checkbox);

        mSelectButton = view.findViewById(R.id.select_button);

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
                                "돌조개|참조개|참꼬막|꼬막|생합|참조개|새꼬막|피조개|키조개|민들조개|명주조개|" +
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

                ((SearchLocationActivity)getActivity()).mapCall(str, 1);
            }
        });

        return view ;
    }

}
