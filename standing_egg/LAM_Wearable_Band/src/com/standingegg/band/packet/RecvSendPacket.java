/**
 * 
 */
/**
 * @author cczzee
 *
 */
package com.standingegg.band.packet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.os.Bundle;
import android.os.StrictMode;

public class RecvSendPacket {

	static String TAG = "RecvSendPacket";
	static int SOCKET_TIME_OUT = 3000;

	private static String mURL = "http://52.69.202.139:8080";

	/**
	 * 서버에 데이터를 보내고 받는 메소드
	 * 
	 * @param msg
	 * @return
	 */
	private String SendByHttp(String msg) {
		if (msg == null)
			msg = "";

		// 서버를 설정해주세요!!!
		// String URL = "http://0.0.0.0:8080/MyServer/JSONServer.jsp";

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			HttpPost post = new HttpPost(mURL + "/user/join?msg=" + msg);

			/* 지연시간 최대 3초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return "";
		}

	}

	public static int usingIdCheck(String id) {
		// if (msg == null)
		// msg = "";

		// 서버를 설정해주세요!!!
		String url = mURL + "/user/check?" + "user_id=" + id;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpGet post = new HttpGet(url);

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
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			final JSONObject jObject = new JSONObject(result);
			int status = jObject.getInt("result");

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}
	}
	public static int signout(String id, String pw, String address) {
		String url = mURL + "/user/signout?" + "user_id="+id+"&pw="+pw+"&device_addr="+address;
		
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
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
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			final JSONObject jObject = new JSONObject(result);
			int status = jObject.getInt("result");
			
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}
	}

	public static String UserSignIn(Bundle person) {
		// if (msg == null)
		// msg = "";

		// 서버를 설정해주세요!!!

		String user_id = person.getString(Constants.USER_ID);
		String pw = person.getString(Constants.USER_PW);
		String name = person.getString(Constants.USER_NAME);
		String device_addr = person.getString(Constants.EXTRA_DEVICE_ADDRESS);
		String birth = person.getString(Constants.USER_BIRTH);
		String gender = person.getString(Constants.USER_GENDER);
		String tall = person.getString(Constants.USER_TALL);
		String weight = person.getString(Constants.USER_WEIGHT);
		String tall_unit = person.getString(Constants.USER_TALL_UNIT);
		String weight_unit = person.getString(Constants.USER_WEIGHT_UNIT);
		int activity_goal = person.getInt(Constants.USER_ACTIVITY_GOAL);
		int sleep_goal = person.getInt(Constants.USER_SLEEP_GOAL);

		String url;
		url = mURL + "/user/signin?" + "user_id=" + user_id + "&pw=" + pw + "&gender=" + gender + "&name=" + name
				+ "&tall=" + tall + "&weight=" + weight + "&birth=" + birth + "&activity_goal=" + activity_goal
				+ "&sleep_goal=" + sleep_goal + "&device_addr=" + device_addr + "&device_serial=LAM000001";

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
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
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			// {"result":400,"messege":"invalid value","data":null}

			return result;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}

	}
	
	
	public static int updateUserInfo(Bundle person) {
		int uid = person.getInt(Constants.UID);
		String name = person.getString(Constants.USER_NAME);
		String birth = person.getString(Constants.USER_BIRTH);
		String gender = person.getString(Constants.USER_GENDER);
		int tall = person.getInt(Constants.USER_TALL);
		int weight = person.getInt(Constants.USER_WEIGHT);
		int activity_goal = person.getInt(Constants.USER_ACTIVITY_GOAL);
		int sleep_goal = person.getInt(Constants.USER_SLEEP_GOAL);

		String url;
		url = mURL + 
				"/user/update?" +
				"uid=" + uid + 
				"&gender=" + gender + 
				"&name=" + name + 
				"&tall=" + tall +
				"&weight=" + weight + 
				"&birth=" + birth + 
				"&activity_goal=" + activity_goal + 
				"&sleep_goal=" + sleep_goal;

		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		
		HttpPut put = new HttpPut(url);

		ULog.v("서버로 보낼 !", url);
		try {

			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(put);
			HttpEntity entity = response.getEntity();
			
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			// {"result":400,"messege":"invalid value","data":null}
			ULog.v("서버에서 받은 ", "===>" + result);
			JSONObject object = new JSONObject(result);
			int status = object.getInt("result");
			
			return status;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return 404;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}

	}

	public static String UserLogin(String user_id, String user_pw, String user_address) {
		String url;
		url = mURL + "/user/login?" + "user_id=" + user_id + "&pw=" + user_pw + "&device_addr=" + user_address;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpPost post = new HttpPost(url);

		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			// final JSONObject jObject = new JSONObject(result);
			// int status = jObject.getInt("result");

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}
	
	public static String findUserPw(String name, String user_id, String birth){
		String url;
		url = mURL + "/user/findpw?" + "user_id=" + user_id+"&name="+name+"&birth="+birth;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpPost post = new HttpPost(url);

		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}

	
	public static String getUserInfo(int uid) {
		String url;
		url = mURL + "/user/info?" + "uid=" + uid;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpGet post = new HttpGet(url);

		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}
	/**
	 * 비밀번호 변경 
	 * @param user_id
	 * @param pw
	 * @param key
	 * @return
	 */
	public static int changePw(String user_id, String pw, String key) {
		String url;
		url = mURL + 
				"/user/editpw?" +
				"user_id=" + user_id + 
				"&pw=" + pw + 
				"&key=" + key;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
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
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			// {"result":400,"messege":"invalid value","data":null}
			ULog.v("서버에서 받은 ", "===>" + result);
			JSONObject object = new JSONObject(result);
			int status = object.getInt("result");
			
			return status;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return 404;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}

	}


	public static int insertDailyData(Bundle b){
		int uid = b.getInt("uid");
		int walk_dist = b.getInt("walk_dis");
		int walk_time = b.getInt("walk_time");
		int walk_cal = b.getInt("walk_cal");
		int run_dist = b.getInt("run_dist");
		int run_time = b.getInt("run_time");
		int run_cal = b.getInt("run_cal");
		int total_step = b.getInt("total_step");
		String daily_date = b.getString("daily_date");

ULog.v("send daily data insert,   daily_date,"+daily_date+"/uid, " +uid +"/walk_dist, "+walk_dist +"/ walk_time,"+walk_time
		+ "/walk_cal,"+walk_cal+"/run_dist,"+run_dist+"/run_time,"+run_time+"/run_cal,"+run_cal+"/total_step,"+total_step);
		
//		유저의 DB PK 값 (uid)
//		걸은 거리 (단위 : m)
//		걸은 시간 (단위 : 분)
//		걸어서 소모한 칼로리 (단위 : cal)
//		달린 거리 (단위 : m)
//		달린 시간 (단위 : 분)
//		달려서 소모한 칼로리 (단위 : cal)
//		total_step:total_step
//		활동한 날짜 (형식 2015-01-01)"
		
		String url;
		url = mURL + "/activity/insert?" + "uid=" +uid +"&walk_dist="+walk_dist+"&walk_time="
				+walk_time+"&walk_cal="+walk_cal+"&run_dist="+run_dist+"&run_time="+run_time
				+"&run_cal="+run_cal+"&total_step="+total_step+"&daily_date="+daily_date;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpPost post = new HttpPost(url);
		
		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result =EntityUtils.toString(entity,HTTP.UTF_8);
			
			JSONObject object = new JSONObject(result);
			int status = object.getInt("result");
			
			ULog.v("서버에서 받은 ", "===>" + result);
			
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}
	}
	
	public static int insertSleepDailyData(Bundle b){
		int uid = b.getInt("uid");
		int total_sleep = b.getInt("total_sleep");
		int deep_sleep = b.getInt("deep_sleep");
		int light_sleep = b.getInt("light_sleep");
		String sleep_start = b.getString("sleep_start");
		String sleep_end = b.getString("sleep_end");
		int awake_time = b.getInt("awake_time");
		String daily_date = b.getString("daily_date");

	ULog.v("send daily data insert,   daily_date,"+daily_date+"/uid, " +uid +"/total_sleep, "+total_sleep +"/ deep_sleep,"+deep_sleep
			+ "/light_sleep,"+light_sleep+"/sleep_start,"+sleep_start+"/sleep_end,"+sleep_end+"/awake_time,"+awake_time);
		
	//유저의 DB PK 값 (uid)
	//총 수면 시간 (단위 : 분)
	//깊은 수면 시간 (단위 : 분)
	//얕은 수면 시간 (단위 : 분)
	//잠든 시간 (형식 14:30:21)
	//깬 시간 (형식 14:30:21)
	//깨어있는 시간 (단위 : 분)
	//수면 날짜 (형식 2015-01-01)"

		String url;
		url = mURL + "/sleep/insert?" + "uid=" +uid +"&total_sleep="+total_sleep+"&deep_sleep="
				+deep_sleep+"&light_sleep="+light_sleep+"&sleep_start="+sleep_start+"&sleep_end="+sleep_end
				+"&awake_time="+awake_time+"&daily_date="+daily_date;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpPost post = new HttpPost(url);
		
		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result =EntityUtils.toString(entity,HTTP.UTF_8);
			
			JSONObject object = new JSONObject(result);
			int status = object.getInt("result");
			
			ULog.v("서버에서 받은 ", "===>" + result);
			
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return 404;
		}
	}
	
	/**
	 * 일간 데이터 서버에서 조회
     "{
		  ""result"": 200,
		  ""messege"": ""OK"",
		  ""data"": [
		    {
		      ""walk_dist"": 3233,
		      ""walk_time"": 45,
		      ""walk_cal"": 1234,
		      ""run_dist"": 5000,
		      ""run_time"": 67,
		      ""run_cal"": 4959,
		      ""total_dist"": 8233,
		      ""total_time"": 112,
		      ""total_cal"": 6193,
		      ""daily_date"": 1450018800000
		    },
		     ...
		  ]
		}"
	 * @param uid
	 * @return
	 */
	public static String getDailyData(int uid) {
		String url;
		url = mURL + "/activity/daily?" + "uid=" + uid;

		HttpClient client = new DefaultHttpClient();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpGet get = new HttpGet(url);

		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}
	
	/**
	 * 주간 데이터 서버에서 조회
	 * "{
		  ""result"": 200,
		  ""messege"": ""OK"",
		  ""data"": [
		    {
		      ""avg_walk_dist"": 1223,
		      ""avg_walk_time"": 122,
		      ""avg_walk_cal"": 234,
		      ""avg_run_dist"": 1235,
		      ""avg_run_time"": 234,
		      ""avg_run_cal"": 2345,
		      ""total_dist"": 2345,
		      ""total_time"": 1234,
		      ""total_cal"": 1234,
		      ""weekly_start"": 1449414000000,
		      ""weekly_end"": 1448895600000
		    },
		    ...
		  ]
		}"
	 * @param uid
	 * @return
	 */
	public static String getWeeklyData(int uid){
		String url;
		url = mURL + "/activity/weekly?" + "uid=" +uid;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpGet get = new HttpGet(url);
		
		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String result =EntityUtils.toString(entity,HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}

	/**
	 * 월간데이터 서버에서 조회"{
		  ""result"": 200,
		  ""messege"": ""OK"",
		  ""data"": [
		    {
		      ""avg_walk_dist"": 1223,
		      ""avg_walk_time"": 122,
		      ""avg_walk_cal"": 234,
		      ""avg_run_dist"": 1235,
		      ""avg_run_time"": 234,
		      ""avg_run_cal"": 2345,
		      ""total_dist"": 2345,
		      ""total_time"": 1234,
		      ""total_cal"": 1234,
		      ""weekly_start"": 1449414000000,
		      ""weekly_end"": 1448895600000
		    },
		    ...
		  ]
		}"
	 * @param uid
	 * @return
	 */
	public static String getMontlyData(int uid){
		String url;
		url = mURL + "/activity/monthly?" + "uid=" +uid;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpGet get = new HttpGet(url);
		
		ULog.v("서버로 보낼 !", url);
		try {
			/* 지연시간 최대 3초 */
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String result =EntityUtils.toString(entity,HTTP.UTF_8);
			ULog.v("서버에서 받은 ", "===>" + result);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			return null;
		}
	}
}