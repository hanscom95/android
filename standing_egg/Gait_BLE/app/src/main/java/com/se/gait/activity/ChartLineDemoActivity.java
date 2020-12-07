/**
 * Disclaimer: IMPORTANT:  This Nulana software is supplied to you by Nulana
 * LTD ("Nulana") in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Nulana software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Nulana software.
 *
 * In consideration of your agreement to abide by the following terms, and
 * subject to these terms, Nulana grants you a personal, non-exclusive
 * license, under Nulana's copyrights in this original Nulana software (the
 * "Nulana Software"), to use, reproduce, modify and redistribute the Nulana
 * Software, with or without modifications, in source and/or binary forms;
 * provided that if you redistribute the Nulana Software in its entirety and
 * without modifications, you must retain this notice and the following
 * text and disclaimers in all such redistributions of the Nulana Software.
 * Except as expressly stated in this notice, no other rights or licenses, 
 * express or implied, are granted by Nulana herein, including but not limited 
 * to any patent rights that may be infringed by your derivative works or by other
 * works in which the Nulana Software may be incorporated.
 *
 * The Nulana Software is provided by Nulana on an "AS IS" basis.  NULANA
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE, REGARDING THE NULANA SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL NULANA BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 * MODIFICATION AND/OR DISTRIBUTION OF THE NULANA SOFTWARE, HOWEVER CAUSED
 * AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 * STRICT LIABILITY OR OTHERWISE, EVEN IF NULANA HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) 2016 Nulana LTD. All Rights Reserved.
 */
 
package com.se.gait.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Float4;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nulana.NChart.NChartAutoZoomAxes;
import com.nulana.NChart.NChartLineSeries;
import com.nulana.NChart.NChartMargin;
import com.nulana.NChart.NChartPoint;
import com.nulana.NChart.NChartPointState;
import com.nulana.NChart.NChartSeries;
import com.nulana.NChart.NChartSeriesDataSource;
import com.nulana.NChart.NChartSolidColorBrush;
import com.nulana.NChart.NChartTextureBrush;
import com.nulana.NChart.NChartTexturePosition;
import com.nulana.NChart.NChartValueAxis;
import com.nulana.NChart.NChartValueAxisDataSource;
import com.nulana.NChart.NChartValueAxisKind;
import com.nulana.NChart.NChartView;
import com.se.gait.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class ChartLineDemoActivity extends ActionBarActivity implements NChartSeriesDataSource, NChartValueAxisDataSource {
    NChartView mNChartView;
    NChartLineSeries mSeries;
    Button mSampleButton, mWifiButton, mClearButton;
    static TextView mStepText, mDistanceText, mXText, mYText, mZText, mErrorXText, mErrorYText, mErrorZText, mMotionText, mErrorPercentageText;
    RelativeLayout mZAxisRelativeLayout, mRadioGroupRelativeLayout;
    ScrollView mScrollView;
    LinearLayout mScrollImg;
    ArrayList<Float4> chartPointArray;
    public static boolean lineBoolen = false;
    final long tickMS = 10;
    int m_count = 0;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);


        mNChartView = (NChartView) findViewById(R.id.surface);
        mSampleButton = (Button) findViewById(R.id.sampleButton);
        mSampleButton.setVisibility(View.GONE);
        mWifiButton = (Button) findViewById(R.id.wifiButton);
        mWifiButton.setVisibility(View.GONE);
        mClearButton = (Button) findViewById(R.id.clearButton);
        mClearButton.setVisibility(View.GONE);
        mStepText = (TextView) findViewById(R.id.stepCountText);
        mDistanceText = (TextView) findViewById(R.id.distanceText);
        mXText = (TextView) findViewById(R.id.xText);
        mYText = (TextView) findViewById(R.id.yText);
        mZText = (TextView) findViewById(R.id.zText);
        mZText.setVisibility(View.GONE);
        mErrorXText = (TextView) findViewById(R.id.errorXText);
        mErrorYText = (TextView) findViewById(R.id.errorYText);
        mErrorZText = (TextView) findViewById(R.id.errorZText);
        mMotionText = (TextView) findViewById(R.id.motionText);
        mErrorPercentageText = (TextView) findViewById(R.id.errorPercentageText);
        mZAxisRelativeLayout = (RelativeLayout) findViewById(R.id.zAxisRelativeLayout);
        mZAxisRelativeLayout.setVisibility(View.GONE);
        mRadioGroupRelativeLayout = (RelativeLayout) findViewById(R.id.chartSizeRelativeLayout);
        mRadioGroupRelativeLayout.setVisibility(View.GONE);
        mScrollImg = (LinearLayout) findViewById(R.id.scrollImg);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = mScrollView.getChildAt(mScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));
                if (diff == 0) {
                    mScrollImg.setVisibility(View.INVISIBLE);
                }else {
                    mScrollImg.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        //chartPointArray = (ArrayList<Float3>)getIntent().getSerializableExtra("chartPoint");
        if(ChartingDemoActivity.chartPointArray != null && ChartingDemoActivity.chartPointArray.size() > 0) {
            chartPointArray = (ArrayList<Float4>)ChartingDemoActivity.chartPointArray;
            m_count = chartPointArray.size();
        }

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_count = 0;
                mNChartView.getChart().rebuildSeries();
            }
        });

        loadView();

        lineBoolen = true;
    }

    volatile boolean treadIsStopped = false;


    BufferedReader in;
    private void loadView() {
        // Paste your license key here.
        mNChartView.getChart().setLicenseKey("");

        mNChartView.getChart().getCartesianSystem().setMargin(new NChartMargin(30.0f, 30.0f, 10.0f, 20.0f));

        // Create series that will be displayed on the chart.
        mSeries = new NChartLineSeries();
        mSeries.setBrush(new NChartSolidColorBrush(Color.RED));
        mSeries.setBorderThickness(1.0f);
        mSeries.setDataSource(this);
        mNChartView.getChart().addSeries(mSeries);

        //mNChartView.getChart().getCartesianSystem().getXAxis().setDataSource(this);
        //mNChartView.getChart().getCartesianSystem().getYAxis().setDataSource(this);
        //mNChartView.getChart().getCartesianSystem().getZAxis().setDataSource(this);
        mNChartView.getChart().setMaxZoom(100);
        mNChartView.getChart().setBackground(new NChartTextureBrush(
                BitmapFactory.decodeResource(getResources(), R.drawable.background2),
                Color.TRANSPARENT, NChartTexturePosition.ScaleKeepMaxAspect));

        // Activate a streaming mode to best performance.
        mNChartView.getChart().setStreamingMode(true);

        // Increase points history length. Please, don't forget about memory using.
        // Device can crash with out of memory if value is too big.
        mNChartView.getChart().setPointsHistoryLength(50000);

        mNChartView.getChart().getCartesianSystem();
        // Enable auto-zoom of the Y-Axis.
        //mNChartView.getChart().getCartesianSystem().setShouldAutoZoom(true);
        mNChartView.getChart().getCartesianSystem().setAutoZoomAxes(NChartAutoZoomAxes.NormalAxis); // This can also be NChartAutoZoomAxes.SecondaryAxis, in case series are hosted on the secondary axis.

        // Prepare data to read.
        try {
            InputStream is = getAssets().open("test.CSV");
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            in.readLine(); // First read to skip title
        } catch (Exception e) {
            Log.d("InputStream error", e.getLocalizedMessage());
            e.printStackTrace();
        }

        // Initialize the data in the chart. Note that this call is mandatory in the beginning of AutoScroll.
        mNChartView.getChart().updateData();
    }

    public static void handlerSetText(ArrayList<String> testData) {
        mStepText.setText(testData.get(0));
        mDistanceText.setText(testData.get(1));
        mMotionText.setText(testData.get(2));
        mXText.setText(testData.get(3));
        mYText.setText(testData.get(4));
        mZText.setText(testData.get(5));
        mErrorPercentageText.setText(testData.get(6));
        mErrorXText.setText(testData.get(7));
        mErrorYText.setText(testData.get(8));
        mErrorZText.setText(testData.get(9));
    }

    protected void onResume() {
        super.onResume();
        mNChartView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mNChartView.onPause();
    }

    public NChartPoint[] points(NChartSeries series) {
        if(chartPointArray!=null && chartPointArray.size() > 0) {
            if(mNChartView.getChart().getPointsHistoryLength() < m_count) {
                m_count = mNChartView.getChart().getPointsHistoryLength()-1;
            }

            NChartPoint[] result = new NChartPoint[m_count];
            for(int i = 0; i < m_count; i++) {
                double  x, y, value;
                x = chartPointArray.get(i).x;
                y = chartPointArray.get(i).y;
                Log.d("onCreate" , "chartPointArray : " +i+"size : " +chartPointArray.size()+" x : " + chartPointArray.get(i).x + "chartPointArray y : " + chartPointArray.get(i).y + "chartPointArray z : " + chartPointArray.get(i).z);
                //result[i] = new NChartPoint(NChartPointState.PointStateAlignedToXWithXY(x, y), series);
                result[i] = new NChartPoint(NChartPointState.PointStateWithXYValue(x, y, 0), series);
            }
            return result;
        }else  {
            NChartPoint[] result = new NChartPoint[1];
            result[0] = new NChartPoint(NChartPointState.PointStateAlignedToXWithXY(15000, 15000), series);
            return result;
        }
    }

    public String name(NChartSeries series) {
        return "Distance (M)";
    }

    public Bitmap image(NChartSeries series) {
        return null;
    }
    
    public NChartPoint[] extraPoints(NChartSeries series) {
        return null;
    }

    @Override
    public String name(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number min(NChartValueAxis nChartValueAxis) {
        if (nChartValueAxis.getKind() == NChartValueAxisKind.X)
            return -1;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Y)
            return -1;

        return null;
    }

    @Override
    public Number max(NChartValueAxis nChartValueAxis) {
        if (nChartValueAxis.getKind() == NChartValueAxisKind.X)
            return 1;
        else if (nChartValueAxis.getKind() == NChartValueAxisKind.Y)
            return 1;
        return null;
    }

    @Override
    public Number step(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String[] ticks(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String[] extraTicks(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number length(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String doubleToString(double v, NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Date minDate(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Date maxDate(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public Number dateStep(NChartValueAxis nChartValueAxis) {
        return null;
    }

    @Override
    public String dateToString(Date date, double v, NChartValueAxis nChartValueAxis) {
        return null;
    }
}
