/**
 * 
 */
/**
 * @author cczzee
 *
 */
package com.standingegg.band.packet;

import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

/**
 * 
 * 매일 12시에 서버 자동 update
 * 
 * @author cczzee
 *
 */
public class serverAutoUpdate extends BroadcastReceiver {
	
	TodayPreferences mToday;
	Preferences mUser;
	
	@Override
	public void onReceive(Context context, Intent intent) // 10초마다 이리루 들어옵니다
	{
		mToday = TodayPreferences.getInstance(context);
		mUser = Preferences.getInstance(context);
		
		
		
		
		
		
		//UR에서 today 말고그 이상의 데이터가 쌓였을 경우도 생각해야함 
		//UR로 받은 데이터도 server로 update
		
		
		
		Bundle b = new Bundle();
		
		
		b.putInt(Constants.UID, mUser.getUid());
		b.putInt(Constants.TODAY_ACT_WALK_DIS, mToday.getWalkDistance());
		b.putInt(Constants.TODAY_ACT_WALK_M, mToday.getWalkMin());
		b.putInt(Constants.TODAY_ACT_WALK_CAL, mToday.getWalkCal());
		b.putInt(Constants.TODAY_ACT_RUN_DIS, mToday.getRunDistance());
		b.putInt(Constants.TODAY_ACT_RUN_M, mToday.getRunMin());
		b.putInt(Constants.TODAY_ACT_RUN_CAL, mToday.getRunCal());
		b.putString(Constants.TODAY_SAVE_DATA_DATE, mToday.getDataDate());
		
//		http://52.69.202.139:8080	
		dailyDataUpdate(b);
		weeklyDataUpdate();
		
		final Calendar c = Calendar.getInstance();
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);

		Toast.makeText(context, "현재 시간" + mHour + ":" + mMinute + ":" + mSecond, Toast.LENGTH_SHORT).show();
	}
	
	
	private void weeklyDataUpdate() {
		
		
	}


	public static int dailyDataUpdate(Bundle b) {
		int uid = b.getInt(Constants.UID);
		int walk_dist = b.getInt(Constants.TODAY_ACT_WALK_DIS);
		int walk_time = b.getInt(Constants.TODAY_ACT_WALK_M);
		int walk_cal = b.getInt(Constants.TODAY_ACT_WALK_CAL);
		int run_dist = b.getInt(Constants.TODAY_ACT_RUN_DIS);
		int run_time = b.getInt(Constants.TODAY_ACT_RUN_M);
		int run_cal = b.getInt(Constants.TODAY_ACT_RUN_CAL);
		String daily_date = b.getString(Constants.TODAY_SAVE_DATA_DATE);

		String url;
		url = "http://52.69.202.139:8080" + "/activity/insert?" + "uid=" + uid + "&walk_dist=" + walk_dist + "&walk_time=" + walk_time + "&walk_cal=" + walk_cal
				+ "&run_dist=" + run_dist + "&run_time=" + run_time + "&run_cal=" + run_cal + "&daily_date=" + daily_date ;

		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 3000);
		HttpPost post = new HttpPost(url);

		ULog.v("서버로 보낼 !", url);
		try {

			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result =EntityUtils.toString(entity,HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			JSONObject res = new JSONObject(result);
			int result_code = res.getInt("result");
			
			return result_code;
		}catch(IllegalStateException e){
			e.printStackTrace();
			return 404;
		}catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}
		
	}

}