package com.standingegg.band.sensor;

/*
 * 
 * 
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class RealTimeChart extends View {
	static boolean bDrawing = false; //���� onDraw()�� ���� ������ üũ [true: onDraw() ������]
	
	int mWidth = 500; // ���� ����
	int mHeight = 500; // ���� ����

	//ǥ�� �� ������ ����
	int leftPadding = 70;
	int topPadding = 20;
	int rightPadding = 30;
	int bottomPadding = 80;
	
	ArrayList<Data> arrDataX = new ArrayList<Data>();
	ArrayList<Data> arrDataY = new ArrayList<Data>();
	ArrayList<Data> arrDataZ = new ArrayList<Data>();
	
	ArrayList<Data> arrData1 = new ArrayList<Data>();
	ArrayList<Data> arrData2 = new ArrayList<Data>();
	ArrayList<Data> arrData3 = new ArrayList<Data>();
	ArrayList<Data> arrData4 = new ArrayList<Data>();

	int dataPointer = 0;// data array �� ���� ��ġ(data�� �� ���� �������� �ѱ�)

	int maxLength = (mWidth - (leftPadding + rightPadding))/2;//data �迭 �ִ�ġ
	
	boolean otherMcu = true;

	
	public RealTimeChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

//		setBackgroundResource(R.drawable.chanrt_basic_bk);
		setBackgroundColor(Color.TRANSPARENT); // ���� ����(�����)
	}

	/*
	 * �䰡 ȭ�鿡 ��Ÿ�� ũ�� ����
	 */
	public boolean Initialize(int width, int height, int labelPointer) {
		
		//������ ȭ�� ũ�Ⱑ ������ ȭ�� ũ�⸦ ���� ����.
		if(width < leftPadding + rightPadding || height < topPadding + bottomPadding){
			
			width = leftPadding + rightPadding + 100;
			height = topPadding + bottomPadding + 100;
			
			return false;
		}
		
		dataPointer = labelPointer;
		SetViewSize(width, height);

		return true;
	}

	/**
	 * SetViewSize()
	 * 
	 * RealTimeChar ũ�� ����
	 * 
	 * @param _width
	 * @param _height
	 * @return
	 */
	public boolean SetViewSize(int _width, int _height) {

		try {
			LayoutParams lp = getLayoutParams();// �䰡 �����Ǳ� ���� ȣ��Ǹ�
												// NullPointerException �߻�

			if (lp == null) {
				return false;
			} else {
				lp.width = _width;
				lp.height = _height;

				mWidth = _width;
				mHeight = _height;

				setLayoutParams(lp);
				
				maxLength = (mWidth - (leftPadding + rightPadding));
				
				arrDataX = new ArrayList<Data>(maxLength);
				arrDataY = new ArrayList<Data>(maxLength);
				arrDataZ = new ArrayList<Data>(maxLength);

				arrData1 = new ArrayList<Data>(maxLength);
				arrData2 = new ArrayList<Data>(maxLength);
				arrData3 = new ArrayList<Data>(maxLength);
				arrData4 = new ArrayList<Data>(maxLength);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * 
	 * ȭ�鿡 �׸���
	 */

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		bDrawing = true;
		
		Paint paint = new Paint();	//��Ʈ �ܰ���
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		
		Paint paintLine = new Paint(); //��Ʈ ���м�
		paintLine.setColor(Color.WHITE);
		paintLine.setAntiAlias(true);
		paintLine.setStrokeWidth(1);
		
		Paint paintText = new Paint(); //X, Y �� ���� text
		paintText.setColor(Color.WHITE);
		paintText.setAntiAlias(true);
		paintText.setTextAlign(Align.CENTER);
		paintText.setTextSize(30);
		
		String strText;

		// ��Ʈ ��� �׸���

		canvas.drawLine(leftPadding, topPadding, leftPadding, mHeight - bottomPadding, paint); // ���� ������
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), paintLine); // �߾� ����
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), paintLine); // �߾� ����
		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), paintLine); // �߾� ����
		canvas.drawLine(leftPadding, mHeight - bottomPadding, mWidth - rightPadding, mHeight - bottomPadding, paint);

//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.125), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.125), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.25), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.375), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.375), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.5), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.625), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.625), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.75), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding+1, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.875), mWidth - rightPadding, topPadding+(int)((mHeight-topPadding-bottomPadding )*0.875), paintLine); // �߾� ����
//		canvas.drawLine(leftPadding, mHeight - bottomPadding, mWidth - rightPadding, mHeight - bottomPadding, paint);
		
		// 16383, -16384
		// �� �׸���
		paintText.setTextAlign(Align.RIGHT);
		strText = "2G"; //""+Data.MAX;
		canvas.drawText(strText, leftPadding - 5 , topPadding+20, paintText); // 40
		strText = "0";
		canvas.drawText(strText, leftPadding - 5 , topPadding+((mHeight-topPadding-bottomPadding )/ 2), paintText);// 370 340
		strText = "-2G";//""+Data.MIN;
		canvas.drawText(strText, leftPadding - 5 , mHeight - bottomPadding, paintText); // 720
		
		paintText.setTextAlign(Align.CENTER);
		if(dataPointer == 1) {
			strText = "X-axis";
		}else if(dataPointer == 2) {
			strText = "Y-axis";
		}else if(dataPointer == 3) {
			strText = "Z-axis";
		}
		canvas.drawText(strText, leftPadding + 320 , mHeight - bottomPadding + 40, paintText); // 40

		if(otherMcu) {
			//� path
			Path path1 = new Path();
			Path path2 = new Path();
			Path path3 = new Path();
			Path path4 = new Path();
			
			// �� ������ ��, ���� ����
			Paint paint1 = new Paint();
			Paint paint2 = new Paint();
			Paint paint3 = new Paint();
			Paint paint4 = new Paint();
			
			paint1.setColor(Color.parseColor("#ff6347"));
			paint1.setAntiAlias(true);
			paint1.setStrokeWidth(2);
			paint1.setStyle(Paint.Style.STROKE);
			
			paint2.setColor(Color.parseColor("#BCE55C"));
			paint2.setAntiAlias(true);
			paint2.setStrokeWidth(2);
			paint2.setStyle(Paint.Style.STROKE);
			
			paint3.setColor(Color.parseColor("#6799FF"));
			paint3.setAntiAlias(true);
			paint3.setStrokeWidth(2);
			paint3.setStyle(Paint.Style.STROKE);
			
			paint4.setColor(Color.parseColor("#FF00DD"));
			paint4.setAntiAlias(true);
			paint4.setStrokeWidth(2);
			paint4.setStyle(Paint.Style.STROKE);
			
			int nSize = arrData1.size();
			
			///2014-12-11
			//������ ũ�⸦ ���Ͽ� ���������� ���� �Է� �Ǿ����� Ȯ��
			if(arrData1.size() != arrData2.size() || arrData1.size() != arrData3.size() || arrData1.size() != arrData4.size()){ // x, y, z �迭�� ũ�Ⱑ ���� ������ �Է¿� ������ �ִٰ� �Ǵ�
				
				//���� ���� ũ���� �迭�� ����
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
			
			if (nSize >= 2){ // data�� 2�� �̻��̾�� �� �׸��� ����
				//� ������
				
				path1.moveTo(leftPadding + 2, valueToPoint(arrData1.get(arrData1.size()-1).value));
				path2.moveTo(leftPadding + 2, valueToPoint(arrData2.get(arrData2.size()-1).value));
				path3.moveTo(leftPadding + 2, valueToPoint(arrData3.get(arrData3.size()-1).value));
				path4.moveTo(leftPadding + 2, valueToPoint(arrData4.get(arrData4.size()-1).value));
				
				//Log.i("x, y, z", (topPadding + arrDataX.get(arrDataX.size()-1).value)+", "+(topPadding + arrDataY.get(arrDataY.size()-1).value)+", "+(topPadding + arrDataZ.get(arrDataZ.size()-1).value));
				
				for (int i = 1; i < maxLength; i++) {
					
					if (i >= nSize)// ���� ����Ǿ��ִ� data �̻��� index�� �Ǹ� �׸��� ����
						break;
					
					//Log.i("nSize, i", nSize+", "+i);
					//� ��� �߰�
					path1.rLineTo(1, valueToPoint(arrData1.get(nSize-i-1).value)-valueToPoint(arrData1.get(nSize-i).value));
					path2.rLineTo(1, valueToPoint(arrData2.get(nSize-i-1).value)-valueToPoint(arrData2.get(nSize-i).value));
					path3.rLineTo(1, valueToPoint(arrData3.get(nSize-i-1).value)-valueToPoint(arrData3.get(nSize-i).value));
					path4.rLineTo(1, valueToPoint(arrData4.get(nSize-i-1).value)-valueToPoint(arrData4.get(nSize-i).value));
					
//				int nCut = maxLength/4; //100;//maxLength/4;
//				
//				// ���� ���м�
//				if((arrDataX.get(nSize - i).x % nCut) == 0){
//					String str = new String();
//					str = ""+arrDataX.get(nSize-i).x;
//					//str.format("%d", arrDataX.get(nSize - i));
//					
//					//���м�
//					canvas.drawLine(leftPadding+1 + (i*2), topPadding+5, leftPadding+1 + (i*2), mHeight-bottomPadding-1, paintLine);
//					//���м� ���� text 
//					canvas.drawText(str, leftPadding+1 + (i*2), mHeight - bottomPadding+30, paintText);
//				}
				}
				
				//� �׸���
				/*if(OneFragment.sgaCheckBox.isChecked()){
					canvas.drawPath(path1, paint1);
				}
				if(OneFragment.bma250CheckBox.isChecked()){
					canvas.drawPath(path2, paint2);
				}
				if(OneFragment.lis331dlhCheckBox.isChecked()){
					canvas.drawPath(path3, paint3);
				}
				if(OneFragment.mpu6500CheckBox.isChecked()){
					canvas.drawPath(path4, paint4);
				}*/
			}

		}else {
			//� path
			Path pathX = new Path();
			Path pathY = new Path();
			Path pathZ = new Path();
			
			// �� ������ ��, ���� ����
			Paint paintX = new Paint();
			Paint paintY = new Paint();
			Paint paintZ = new Paint();
			
			paintX.setColor(Color.parseColor("#ff6347"));
			paintX.setAntiAlias(true);
			paintX.setStrokeWidth(2);
			paintX.setStyle(Paint.Style.STROKE);
			
			paintY.setColor(Color.parseColor("#BCE55C"));
			paintY.setAntiAlias(true);
			paintY.setStrokeWidth(2);
			paintY.setStyle(Paint.Style.STROKE);
			
			paintZ.setColor(Color.parseColor("#6799FF"));
			paintZ.setAntiAlias(true);
			paintZ.setStrokeWidth(2);
			paintZ.setStyle(Paint.Style.STROKE);
			
			int nSize = arrDataX.size();
			
			///2014-12-11
			//������ ũ�⸦ ���Ͽ� ���������� ���� �Է� �Ǿ����� Ȯ��
			
			if(arrDataX.size() != arrDataY.size() || arrDataX.size() != arrDataZ.size()){ // x, y, z �迭�� ũ�Ⱑ ���� ������ �Է¿� ������ �ִٰ� �Ǵ�
				
				
				//���� ���� ũ���� �迭�� ����
				if(nSize > arrDataY.size()) nSize = arrDataY.size();
				if(nSize > arrDataZ.size()) nSize = arrDataZ.size();
				
				while(arrDataX.size() > nSize) arrDataX.remove(0);
				while(arrDataY.size() > nSize) arrDataY.remove(0);
				while(arrDataZ.size() > nSize) arrDataZ.remove(0);
			}
			///2014-12-11
			
			pathX.reset();
			pathY.reset();
			pathZ.reset();
			
			
			
			paintText.setTextAlign(Align.CENTER);
			
			//Log.i("nSize", maxLength+": "+nSize);
			
			if (nSize >= 2){ // data�� 2�� �̻��̾�� �� �׸��� ����
				//� ������
				
				pathX.moveTo(leftPadding + 2, valueToPoint(arrDataX.get(arrDataX.size()-1).value));
				pathY.moveTo(leftPadding + 2, valueToPoint(arrDataY.get(arrDataY.size()-1).value));
				pathZ.moveTo(leftPadding + 2, valueToPoint(arrDataZ.get(arrDataZ.size()-1).value));
				
				//Log.i("x, y, z", (topPadding + arrDataX.get(arrDataX.size()-1).value)+", "+(topPadding + arrDataY.get(arrDataY.size()-1).value)+", "+(topPadding + arrDataZ.get(arrDataZ.size()-1).value));
				
				for (int i = 1; i < maxLength; i++) {
					
					if (i >= nSize)// ���� ����Ǿ��ִ� data �̻��� index�� �Ǹ� �׸��� ����
						break;
					
					//Log.i("nSize, i", nSize+", "+i);
					//� ��� �߰�
					pathX.rLineTo(10, valueToPoint(arrDataX.get(nSize-i-1).value)-valueToPoint(arrDataX.get(nSize-i).value));
					pathY.rLineTo(10, valueToPoint(arrDataY.get(nSize-i-1).value)-valueToPoint(arrDataY.get(nSize-i).value));
					pathZ.rLineTo(10, valueToPoint(arrDataZ.get(nSize-i-1).value)-valueToPoint(arrDataZ.get(nSize-i).value));
					
//				int nCut = maxLength/4; //100;//maxLength/4;
//				
//				// ���� ���м�
//				if((arrDataX.get(nSize - i).x % nCut) == 0){
//					String str = new String();
//					str = ""+arrDataX.get(nSize-i).x;
//					//str.format("%d", arrDataX.get(nSize - i));
//					
//					//���м�
//					canvas.drawLine(leftPadding+1 + (i*2), topPadding+5, leftPadding+1 + (i*2), mHeight-bottomPadding-1, paintLine);
//					//���м� ���� text 
//					canvas.drawText(str, leftPadding+1 + (i*2), mHeight - bottomPadding+30, paintText);
//				}
				}
				
				//� �׸���
				canvas.drawPath(pathX, paintX);
				canvas.drawPath(pathY, paintY);
				canvas.drawPath(pathZ, paintZ);
			}
		}
		
		
		bDrawing = false;
		
	}

	/**
	 * ������ ���� ����
	 * 
	 * @author L
	 * 
	 */
	class Data {
		// -32768 +32767
		final static public int MIN = -32768;	//�ּ� �Է°�
		final static public int MAX = 32767;	//�ִ� �Է°�
		int x; // �Է��� ����
		int value; // ���� ��ǥ ��

		Data(int _x, int _value) {
			x = _x;
			value = _value;
		}
	}

	/**
	 * data �߰�
	 * 
	 * �� �Լ��� �̿��Ͽ� ���� �߰��ϸ� �ڵ����� ȭ���� ���ŵ�
	 * 
	 * ������ x ���� ���ؼ��� ������ ������ ���Ŀ� y, z ���� ���Խ�ų ����
	 */

	boolean addData(int x, int valueX, int valueY, int valueZ) {
/*
		if(bDrawing){
			Log.i("add fail", "add fail");
			return false;
		}
		*/
		otherMcu = false;
		//���� �ִ� �ּҰ� ����
		if (valueX < Data.MIN)		valueX = Data.MIN;
		if(valueX > Data.MAX)		valueX = Data.MAX;
		
		if (valueY < Data.MIN)		valueY = Data.MIN;
		if(valueY > Data.MAX)		valueY = Data.MAX;

		if (valueZ < Data.MIN)		valueZ = Data.MIN;
		if(valueZ > Data.MAX)		valueZ = Data.MAX;

		arrDataX.add(0, new Data(x, valueX)); // 0���� ���� ������ ������ ������ �ڷ� �и��鼭 �ڵ����ĵ�
		arrDataY.add(0, new Data(x, valueY));
		arrDataZ.add(0, new Data(x, valueZ));

		if (arrDataX.size() == maxLength) { // ȭ�鿡 ǥ���Ұ� �̻��� �����Ͱ� ����Ǹ� ����(�޸𸮸� ����
											// �縸 ����ϵ��� �ϱ����ؼ� ��������)
			arrDataX.remove(maxLength - 1);
			arrDataY.remove(maxLength - 1);
			arrDataZ.remove(maxLength - 1);
		}

		postInvalidate(); // �񵿱�ȭ ������� ȭ���� ���� (OnDraw() ȣ��) �� ��
		//Log.i("postInvalidate", "invalidate");
		
		//invalidate();
		return true;
	}	
	/**
	 * data �߰�
	 * 
	 * �� �Լ��� �̿��Ͽ� ���� �߰��ϸ� �ڵ����� ȭ���� ���ŵ�
	 * 
	 * ������ x ���� ���ؼ��� ������ ������ ���Ŀ� y, z ���� ���Խ�ų ����
	 */

	boolean addData(int x, int value1, int value2, int value3, int value4) {
/*
		if(bDrawing){
			Log.i("add fail", "add fail");
			return false;
		}
		*/
		otherMcu = true;
		//���� �ִ� �ּҰ� ����
		if (value1 < Data.MIN)		value1 = Data.MIN;
		if(value1 > Data.MAX)		value1 = Data.MAX;
		
		if (value2 < Data.MIN)		value2 = Data.MIN;
		if(value2 > Data.MAX)		value2 = Data.MAX;

		if (value3 < Data.MIN)		value3 = Data.MIN;
		if(value3 > Data.MAX)		value3 = Data.MAX;
		
		if (value4 < Data.MIN)		value4 = Data.MIN;
		if(value4 > Data.MAX)		value4 = Data.MAX;
		
		arrData1.add(0, new Data(x, value1)); // 0���� ���� ������ ������ ������ �ڷ� �и��鼭 �ڵ����ĵ�
		arrData2.add(0, new Data(x, value2));
		arrData3.add(0, new Data(x, value3));
		arrData4.add(0, new Data(x, value4));

		if (arrData1.size() == maxLength) { // ȭ�鿡 ǥ���Ұ� �̻��� �����Ͱ� ����Ǹ� ����(�޸𸮸� ����
											// �縸 ����ϵ��� �ϱ����ؼ� ��������)
			arrData1.remove(maxLength - 1);
			arrData2.remove(maxLength - 1);
			arrData3.remove(maxLength - 1);
			arrData4.remove(maxLength - 1);
		}

		postInvalidate(); // �񵿱�ȭ ������� ȭ���� ���� (OnDraw() ȣ��) �� ��
		//Log.i("postInvalidate", "invalidate");
		
		//invalidate();
		return true;
	}
	
	float valueToPoint(int value){
		float height = mHeight - topPadding - bottomPadding;
		
		return height/2 -( (height/2) * value / ((Data.MAX- Data.MIN)/2) ) + topPadding;
	}

}
