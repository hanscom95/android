package se.com.band.goal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.com.band.R;
import se.com.band.utility.ULog;

/**
 * Created by moon on 2017-01-20.
 */

public class GoalTabFragment extends Fragment {

    com.shawnlin.numberpicker.NumberPicker mNumberPicker;

    int mTabCount;
    int mSelectTab;
    String mValue;

    public GoalTabFragment(int tabCount, String value) {
        mTabCount = tabCount;
        mValue = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.number_picker_tab, container, false);

        String[] stringArray = new String[15];
        if(mTabCount == 0) {
            stringArray[0] = "0";
            stringArray[1] = "10";
            stringArray[2] = "20";
            stringArray[3] = "50";
            stringArray[4] = "100";
            stringArray[5] = "200";
            stringArray[6] = "500";
            stringArray[7] = "1000";
            stringArray[8] = "1500";
            stringArray[9] = "2000";
            stringArray[10] = "2500";
            stringArray[11] = "3000";
            stringArray[12] = "5000";
            stringArray[13] = "10000";
            stringArray[14] = "20000";
        }else {
            stringArray[0] = "0";
            stringArray[1] = "100";
            stringArray[2] = "200";
            stringArray[3] = "300";
            stringArray[4] = "400";
            stringArray[5] = "500";
            stringArray[6] = "600";
            stringArray[7] = "700";
            stringArray[8] = "800";
            stringArray[9] = "900";
            stringArray[10] = "1000";
            stringArray[11] = "1100";
            stringArray[12] = "1200";
            stringArray[13] = "1300";
            stringArray[14] = "1400";
        }
//        int n=0;
//        for(int i=0; i<10; i++){
//            stringArray[i] = Integer.toString(n);
//            n+=10;
//        }
        mNumberPicker = (com.shawnlin.numberpicker.NumberPicker) v.findViewById(R.id.number_picker);
        mNumberPicker.setMaxValue(stringArray.length-1);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setDisplayedValues(stringArray);

        if(mValue != null && Integer.parseInt(mValue) > 0) {
            initSetValue();
        }
        return v;
    }

    public int getValue() {
        return Integer.parseInt(mNumberPicker.getDisplayedValues()[mNumberPicker.getValue()]);
    }

    private void initSetValue() {
        for(int i = 0; i < mNumberPicker.getDisplayedValues().length; i++) {
            if(mValue.equals(mNumberPicker.getDisplayedValues()[i])) {
                mNumberPicker.setValue(i);
            }
        }
    }
}
