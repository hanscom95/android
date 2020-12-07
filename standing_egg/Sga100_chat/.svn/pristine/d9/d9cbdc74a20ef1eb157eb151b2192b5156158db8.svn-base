package com.example.test1218;

/*
 * 
 * 
 */

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class RealTimeNoiseChart extends View {
	static boolean bDrawing = false; //현재 onDraw()가 수행 중인지 체크 [true: onDraw() 수행중]
	
	int mWidth = 500; // 뷰의 넓이
	int mHeight = 500; // 뷰의 높이

	//표와 뷰 사이의 넓이
	int leftPadding = 100;
	int topPadding = 20;
	int rightPadding = 10;
	int bottomPadding = 80;
	
	ArrayList<Data> arrData1 = new ArrayList<Data>();
	ArrayList<Data> arrData2 = new ArrayList<Data>();
	ArrayList<Data> arrData3 = new ArrayList<Data>();
	ArrayList<Data> arrData4 = new ArrayList<Data>();

	int dataPointer = 0;// data array 의 시작 위치(data가 꽉 차면 다음으로 넘김)

	int maxLength = (mWidth - (leftPadding + rightPadding))/2;//data 배열 최대치

	
	public RealTimeNoiseChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		setBackgroundResource(R.drawable.noise_chart_bk);
//		setBackgroundColor(Color.TRANSPARENT); // 배경색 설정(투명색)
	}

	/*
	 * 뷰가 화면에 나타날 크기 설정
	 */
	public boolean Initialize(int width, int height) {
		
		//설정된 화면 크기가 작으면 화면 크기를 임의 변경.
		if(width < leftPadding + rightPadding || height < topPadding + bottomPadding){
			
			width = leftPadding + rightPadding + 100;
			height = topPadding + bottomPadding + 100;
			
			return false;
		}
			
		
		SetViewSize(width, height);

		return true;
	}

	/**
	 * SetViewSize()
	 * 
	 * RealTimeChar 크기 설정
	 * 
	 * @param mWidth
	 * @param mHeight
	 * @return
	 */
	public boolean SetViewSize(int _width, int _height) {

		try {
			LayoutParams lp = getLayoutParams();// 뷰가 생성되기 전에 호출되면
												// NullPointerException 발생

			if (lp == null) {
				return false;
			} else {
				lp.width = _width;
				lp.height = _height;

				mWidth = _width;
				mHeight = _height;

				setLayoutParams(lp);
				
				maxLength = (mWidth - (leftPadding + rightPadding))/10;

				arrData1 = new ArrayList<RealTimeNoiseChart.Data>(maxLength);
				arrData2 = new ArrayList<RealTimeNoiseChart.Data>(maxLength);
				arrData3 = new ArrayList<RealTimeNoiseChart.Data>(maxLength);
				arrData4 = new ArrayList<RealTimeNoiseChart.Data>(maxLength);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * 
	 * 화면에 그리기
	 */

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		bDrawing = true;
		
		Paint paint = new Paint();	//차트 외곽선	
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		
		Paint paintLine = new Paint(); //차트 구분선
		paintLine.setColor(Color.WHITE);
		paintLine.setAntiAlias(true);
		paintLine.setStrokeWidth(1);
		
		Paint paintText = new Paint(); //X, Y 축 설명 text
		paintText.setColor(Color.WHITE);
		paintText.setAntiAlias(true);
		paintText.setTextAlign(Align.CENTER);
		paintText.setTextSize(30);
		
		String strText;

		//// 차트 배경 그리기

		canvas.drawLine(leftPadding, topPadding, leftPadding, mHeight - bottomPadding, paint); // 왼쪽 수직선
		canvas.drawLine(leftPadding+700, topPadding, leftPadding+700, mHeight - bottomPadding, paintLine); // 왼쪽 수직선
		canvas.drawLine(leftPadding+1400, topPadding, leftPadding+1400, mHeight - bottomPadding, paintLine); // 왼쪽 수직선
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), paintLine); // 중앙 수평선
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), paintLine); // 중앙 수평선
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), paintLine); // 중앙 수평선
		canvas.drawLine(leftPadding, mHeight - bottomPadding, mWidth - rightPadding, mHeight - bottomPadding, paint);

		
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.125), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.125), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.375), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.375), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.625), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.625), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.875), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.875), paintLine); // 중앙 수평선
//		canvas.drawLine(leftPadding, mHeight - bottomPadding, mWidth - rightPadding, mHeight - bottomPadding, paint);
		
		// 16383, -16384
		paintText.setTextAlign(Align.RIGHT);
		strText = "600"; //""+Data.MAX;
		canvas.drawText(strText, leftPadding - 5 , topPadding+20, paintText); // 40
		strText = "300";
		canvas.drawText(strText, leftPadding - 5 , topPadding+((mHeight-topPadding-bottomPadding )/ 2), paintText);// 370 340
		strText = "0";//""+Data.MIN;
		canvas.drawText(strText, leftPadding - 5 , mHeight - bottomPadding, paintText); // 720
		
		
		// 16383, -16384
		paintText.setTextAlign(Align.CENTER);
		strText = "X-axis"; //""+Data.MAX;
		canvas.drawText(strText, leftPadding + 350 , mHeight - bottomPadding + 40, paintText); // 40
		strText = "Y-axis";
		canvas.drawText(strText, leftPadding + 1050 , mHeight - bottomPadding + 40, paintText);// 370 340
		strText = "Z-axis";//""+Data.MIN;
		canvas.drawText(strText, leftPadding +1750 , mHeight - bottomPadding + 40, paintText); // 720
		//// 선 그리기

		//곡선 path
		Path path1 = new Path();
		Path path2 = new Path();
		Path path3 = new Path();
		Path path4 = new Path();
		
		// 각 선들의 색, 굵기 설정
		Paint paint1 = new Paint();
		Paint paint2 = new Paint();
		Paint paint3 = new Paint();
		Paint paint4 = new Paint();
		
		paint1.setColor(Color.parseColor("#ff6347"));
		paint1.setAntiAlias(true);
		paint1.setStrokeWidth(2);
        paint1.setStyle(Paint.Style.FILL);
		
		paint2.setColor(Color.parseColor("#BCE55C"));
		paint2.setAntiAlias(true);
		paint2.setStrokeWidth(2);
		paint2.setStyle(Paint.Style.FILL);
		
		paint3.setColor(Color.parseColor("#6799FF"));
		paint3.setAntiAlias(true);
		paint3.setStrokeWidth(2);
		paint3.setStyle(Paint.Style.FILL);
		
		paint4.setColor(Color.parseColor("#FF00DD"));
		paint4.setAntiAlias(true);
		paint4.setStrokeWidth(2);
		paint4.setStyle(Paint.Style.FILL);
		
		int nSize = arrData1.size();
		
		///2014-12-11
		//각각의 크기를 비교하여 정상적으로 값이 입력 되었는지 확인
		if(arrData1.size() != arrData2.size() || arrData1.size() != arrData3.size() || arrData1.size() != arrData4.size()){ // x, y, z 배열의 크기가 같지 않으면 입력에 문제가 있다고 판단
			//가장 작은 크기의 배열에 맞춤
			if(nSize > arrData2.size()) nSize = arrData2.size();
			if(nSize > arrData3.size()) nSize = arrData3.size();
			if(nSize > arrData4.size()) nSize = arrData4.size();
			
			while(arrData1.size() > nSize) arrData1.remove(0);
			while(arrData2.size() > nSize) arrData2.remove(0);
			while(arrData3.size() > nSize) arrData3.remove(0);
			while(arrData4.size() > nSize) arrData4.remove(0);
		}
		///2014-12-11
		
		path1.reset();
		path2.reset();
		path3.reset();
		path4.reset();
		
		paintText.setTextAlign(Align.CENTER);
		
		if (nSize >= 2){ // data가 2개 이상이어야 선 그리기 가능
			//곡선 시작점
			
			path1.moveTo(leftPadding + 350, valueToPoint(arrData1.get(arrData1.size()-1).value));
			path2.moveTo(leftPadding + 350, valueToPoint(arrData2.get(arrData2.size()-1).value));
			path3.moveTo(leftPadding + 350, valueToPoint(arrData3.get(arrData3.size()-1).value));
			path4.moveTo(leftPadding + 350, valueToPoint(arrData4.get(arrData4.size()-1).value));
			
	        int circleLeftPadding = leftPadding + 350;
	        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData1.get(arrData1.size()-1).value), 10, paint1);
	        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData2.get(arrData2.size()-1).value), 10, paint2);
	        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData3.get(arrData3.size()-1).value), 10, paint3);
	        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData4.get(arrData4.size()-1).value), 10, paint4);
			
			for (int i = 1; i < maxLength; i++) {
				
				if (i >= nSize)// 현재 저장되어있는 data 이상의 index가 되면 그리기 종료
					break;
				
				//곡선 경로 추가
				path1.rLineTo(700, valueToPoint(arrData1.get(nSize-i-1).value)-valueToPoint(arrData1.get(nSize-i).value));
				path2.rLineTo(700, valueToPoint(arrData2.get(nSize-i-1).value)-valueToPoint(arrData2.get(nSize-i).value));
				path3.rLineTo(700, valueToPoint(arrData3.get(nSize-i-1).value)-valueToPoint(arrData3.get(nSize-i).value));
				path4.rLineTo(700, valueToPoint(arrData4.get(nSize-i-1).value)-valueToPoint(arrData4.get(nSize-i).value));

				circleLeftPadding = circleLeftPadding+700;
		        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData1.get(nSize-i-1).value), 10, paint1);
		        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData2.get(nSize-i-1).value), 10, paint2);
		        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData3.get(nSize-i-1).value), 10, paint3);
		        canvas.drawCircle(circleLeftPadding, valueToPoint(arrData4.get(nSize-i-1).value), 10, paint4);
				
			}
			paint1.setStyle(Paint.Style.STROKE);
			paint2.setStyle(Paint.Style.STROKE);
			paint3.setStyle(Paint.Style.STROKE);
			paint4.setStyle(Paint.Style.STROKE);
			
			//곡선 그리기
			canvas.drawPath(path1, paint1);
			canvas.drawPath(path2, paint2);
			canvas.drawPath(path3, paint3);
			canvas.drawPath(path4, paint4);
		}
		
		
		bDrawing = false;
		
	}

	/**
	 * 데이터 저장 단위
	 * 
	 * @author L
	 * 
	 */
	class Data {
		// -32768 +32767
		final static public int MIN = 0;	//최소 입력값
		final static public int MAX = 600;	//최대 입력값
		int x; // 입려된 순서
		int value; // 실재 좌표 값

		Data(int _x, int _value) {
			x = _x;
			value = _value;
		}
	}

	/**
	 * data 추가
	 * 
	 * 이 함수를 이용하여 값을 추가하면 자동으로 화면이 갱신됨
	 * 
	 * 지금은 x 값에 대해서만 저장을 하지만 추후에 y, z 까지 보함시킬 예정
	 */

	boolean addData(int x, int value1, int value2, int value3, int value4) {
/*
		if(bDrawing){
			Log.i("add fail", "add fail");
			return false;
		}
		*/
		//센서 최대 최소값 고정
		if (value1 < Data.MIN)		value1 = Data.MIN;
		if(value1 > Data.MAX)		value1 = Data.MAX;
		
		if (value2 < Data.MIN)		value2 = Data.MIN;
		if(value2 > Data.MAX)		value2 = Data.MAX;

		if (value3 < Data.MIN)		value3 = Data.MIN;
		if(value3 > Data.MAX)		value3 = Data.MAX;
		
		if (value4 < Data.MIN)		value4 = Data.MIN;
		if(value4 > Data.MAX)		value4 = Data.MAX;
		
		arrData1.add(0, new Data(x, value1)); // 0번에 값을 넣으면 기존의 값들이 뒤로 밀리면서 자동정렬됨
		arrData2.add(0, new Data(x, value2));
		arrData3.add(0, new Data(x, value3));
		arrData4.add(0, new Data(x, value4));

		if (arrData1.size() == 4) { // 화면에 표현할것 이상의 대이터가 저장되면 삭제(메모리를 일정
											// 양만 사용하도록 하기위해서 설정했음)
			arrData1.remove(3);
			arrData2.remove(3);
			arrData3.remove(3);
			arrData4.remove(3);
		}

		postInvalidate(); // 비동기화 방식으로 화면을 갱신 (OnDraw() 호출) 을 함
		//Log.i("postInvalidate", "invalidate");
		
		//invalidate();
		return true;
	}
	
	float valueToPoint(int value){
		float height = mHeight - topPadding - bottomPadding;
		
		return height/2 -( (height/2) * value / ((Data.MAX-Data.MIN)/2) ) + topPadding+ 600;
	}

}
