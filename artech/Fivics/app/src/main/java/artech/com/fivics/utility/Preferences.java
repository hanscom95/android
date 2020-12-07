package artech.com.fivics.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 가맹점 정보
 * 
 * @author taehoon
 *
 */
public class Preferences {
	// Constants

	// Instance
	private static Preferences mInstance = null;

	private static final String PREFERENCE_NAME = "FIVICS";
	private static final String PREFERENCE_URL = "url";
	private static final String PREFERENCE_FRANCHISEE = "franchisee";
	private static final String PREFERENCE_YELLOWX = "YX";
	private static final String PREFERENCE_YELLOWY = "YY";
	private static final String PREFERENCE_REDX = "RX";
	private static final String PREFERENCE_REDY = "RY";
	private static final String PREFERENCE_BLUEX = "BX";
	private static final String PREFERENCE_BLUEY = "BY";
	private static final String PREFERENCE_BLACKX = "BKX";
	private static final String PREFERENCE_BLACKY = "BKY";
	private static final String PREFERENCE_WHITEX = "WX";
	private static final String PREFERENCE_WHITEY = "WY";

	private static final String PREFERENCE_ELIPSES1 = "elipses1";
	private static final String PREFERENCE_ELIPSES2 = "elipses2";
	private static final String PREFERENCE_ELIPSES3 = "elipses3";
	private static final String PREFERENCE_ELIPSES4 = "elipses4";
	private static final String PREFERENCE_ELIPSES5 = "elipses5";
	private static final String PREFERENCE_ELIPSES6 = "elipses6";
	private static final String PREFERENCE_ELIPSES7 = "elipses7";
	private static final String PREFERENCE_ELIPSES8 = "elipses8";
	private static final String PREFERENCE_ELIPSES9 = "elipses9";
	private static final String PREFERENCE_ELIPSES10 = "elipses10";
	private static final String PREFERENCE_ELIPSES11 = "elipses11";
	private static final String PREFERENCE_ELIPSES12 = "elipses12";
	private static final String PREFERENCE_ELIPSES13 = "elipses13";
	private static final String PREFERENCE_ELIPSES14 = "elipses14";
	private static final String PREFERENCE_ELIPSES15 = "elipses15";
	private static final String PREFERENCE_ELIPSES16 = "elipses16";
	private static final String PREFERENCE_ELIPSES17 = "elipses17";
	private static final String PREFERENCE_ELIPSES18 = "elipses18";
	private static final String PREFERENCE_ELIPSES19 = "elipses19";
	private static final String PREFERENCE_ELIPSES20 = "elipses20";

	private Context mContext;
	private String mVersion = null;

	private String mFranchisee = null;
	private String mURL = null;
	private int[] mLocation = null;
	private float[] mElipses = null;


	private Preferences(Context c) {
		mContext = c;

		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		
		mFranchisee = prefs.getString(PREFERENCE_FRANCHISEE, "");
		mURL = prefs.getString(PREFERENCE_URL, "");

		mLocation = new int[10];
		mLocation[0] = prefs.getInt(PREFERENCE_YELLOWX, 0);
		mLocation[1] = prefs.getInt(PREFERENCE_YELLOWY, 0);
		mLocation[2] = prefs.getInt(PREFERENCE_REDX, 0);
		mLocation[3] = prefs.getInt(PREFERENCE_REDY, 0);
		mLocation[4] = prefs.getInt(PREFERENCE_BLUEX, 0);
		mLocation[5] = prefs.getInt(PREFERENCE_BLUEY, 0);
		mLocation[6] = prefs.getInt(PREFERENCE_BLACKX, 0);
		mLocation[7] = prefs.getInt(PREFERENCE_BLACKY, 0);
		mLocation[8] = prefs.getInt(PREFERENCE_WHITEX, 0);
		mLocation[9] = prefs.getInt(PREFERENCE_WHITEY, 0);


		mElipses = new float[20];
		mElipses[0] = prefs.getFloat(PREFERENCE_ELIPSES1, 0.0f);
		mElipses[1] = prefs.getFloat(PREFERENCE_ELIPSES2, 0.0f);
		mElipses[2] = prefs.getFloat(PREFERENCE_ELIPSES3, 0.0f);
		mElipses[3] = prefs.getFloat(PREFERENCE_ELIPSES4, 0.0f);
		mElipses[4] = prefs.getFloat(PREFERENCE_ELIPSES5, 0.0f);
		mElipses[5] = prefs.getFloat(PREFERENCE_ELIPSES6, 0.0f);
		mElipses[6] = prefs.getFloat(PREFERENCE_ELIPSES7, 0.0f);
		mElipses[7] = prefs.getFloat(PREFERENCE_ELIPSES8, 0.0f);
		mElipses[8] = prefs.getFloat(PREFERENCE_ELIPSES9, 0.0f);
		mElipses[9] = prefs.getFloat(PREFERENCE_ELIPSES10, 0.0f);
		mElipses[10] = prefs.getFloat(PREFERENCE_ELIPSES11, 0.0f);
		mElipses[11] = prefs.getFloat(PREFERENCE_ELIPSES12, 0.0f);
		mElipses[12] = prefs.getFloat(PREFERENCE_ELIPSES13, 0.0f);
		mElipses[13] = prefs.getFloat(PREFERENCE_ELIPSES14, 0.0f);
		mElipses[14] = prefs.getFloat(PREFERENCE_ELIPSES15, 0.0f);
		mElipses[15] = prefs.getFloat(PREFERENCE_ELIPSES16, 0.0f);
		mElipses[16] = prefs.getFloat(PREFERENCE_ELIPSES17, 0.0f);
		mElipses[17] = prefs.getFloat(PREFERENCE_ELIPSES18, 0.0f);
		mElipses[18] = prefs.getFloat(PREFERENCE_ELIPSES19, 0.0f);
		mElipses[19] = prefs.getFloat(PREFERENCE_ELIPSES20, 0.0f);
	}

	/**
	 * Single pattern
	 */
	public synchronized static Preferences getInstance(Context c) {
		if (mInstance == null) {
			if (c != null)
				mInstance = new Preferences(c);
			else
				return null;
		}
		return mInstance;
	}

	/**
	 * Reset connection info
	 */
	public void resetPreferences() {
		mFranchisee = "";
		mURL = "";

		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PREFERENCE_FRANCHISEE, mFranchisee);
		editor.putString(PREFERENCE_URL, mURL);
		editor.putInt(PREFERENCE_YELLOWX, 0);
		editor.putInt(PREFERENCE_YELLOWY, 0);
		editor.putInt(PREFERENCE_REDX, 0);
		editor.putInt(PREFERENCE_REDY, 0);
		editor.putInt(PREFERENCE_BLUEX, 0);
		editor.putInt(PREFERENCE_BLUEY, 0);
		editor.putInt(PREFERENCE_BLACKX, 0);
		editor.putInt(PREFERENCE_BLACKY, 0);
		editor.putInt(PREFERENCE_WHITEX, 0);
		editor.putInt(PREFERENCE_WHITEY, 0);

		editor.putFloat(PREFERENCE_ELIPSES1, 0);
		editor.putFloat(PREFERENCE_ELIPSES2, 0);
		editor.putFloat(PREFERENCE_ELIPSES3, 0);
		editor.putFloat(PREFERENCE_ELIPSES4, 0);
		editor.putFloat(PREFERENCE_ELIPSES5, 0);
		editor.putFloat(PREFERENCE_ELIPSES6, 0);
		editor.putFloat(PREFERENCE_ELIPSES7, 0);
		editor.putFloat(PREFERENCE_ELIPSES8, 0);
		editor.putFloat(PREFERENCE_ELIPSES9, 0);
		editor.putFloat(PREFERENCE_ELIPSES10, 0);
		editor.putFloat(PREFERENCE_ELIPSES11, 0);
		editor.putFloat(PREFERENCE_ELIPSES12, 0);
		editor.putFloat(PREFERENCE_ELIPSES13, 0);
		editor.putFloat(PREFERENCE_ELIPSES14, 0);
		editor.putFloat(PREFERENCE_ELIPSES15, 0);
		editor.putFloat(PREFERENCE_ELIPSES16, 0);
		editor.putFloat(PREFERENCE_ELIPSES17, 0);
		editor.putFloat(PREFERENCE_ELIPSES18, 0);
		editor.putFloat(PREFERENCE_ELIPSES19, 0);

		editor.commit();
	}

	public String getFranchisee(){
		return mFranchisee;
	}
	public String getURL(){
		return mURL;
	}
	public int[] getLocation() {
		return mLocation;
	}
	public float[] getElipses() {
		return mElipses;
	}
	

	public void setFranchisee(String franchisee) {
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PREFERENCE_FRANCHISEE, franchisee);
		editor.commit();
	}

	public void setURL(String url) {
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PREFERENCE_URL, url);
		editor.commit();
	}

	public void setLocation(int[] location) {
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFERENCE_YELLOWX, location[0]);
		editor.putInt(PREFERENCE_YELLOWY, location[1]);
		editor.putInt(PREFERENCE_REDX, location[2]);
		editor.putInt(PREFERENCE_REDY, location[3]);
		editor.putInt(PREFERENCE_BLUEX, location[4]);
		editor.putInt(PREFERENCE_BLUEY, location[5]);
		editor.putInt(PREFERENCE_BLACKX, location[6]);
		editor.putInt(PREFERENCE_BLACKY, location[7]);
		editor.putInt(PREFERENCE_WHITEX, location[8]);
		editor.putInt(PREFERENCE_WHITEY, location[9]);
		editor.commit();
	}


	public void setElipses(float[] elipses) {
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putFloat(PREFERENCE_ELIPSES1, elipses[0]);
		editor.putFloat(PREFERENCE_ELIPSES2, elipses[1]);
		editor.putFloat(PREFERENCE_ELIPSES3, elipses[2]);
		editor.putFloat(PREFERENCE_ELIPSES4, elipses[3]);
		editor.putFloat(PREFERENCE_ELIPSES5, elipses[4]);
		editor.putFloat(PREFERENCE_ELIPSES6, elipses[5]);
		editor.putFloat(PREFERENCE_ELIPSES7, elipses[6]);
		editor.putFloat(PREFERENCE_ELIPSES8, elipses[7]);
		editor.putFloat(PREFERENCE_ELIPSES9, elipses[8]);
		editor.putFloat(PREFERENCE_ELIPSES10, elipses[9]);
		editor.putFloat(PREFERENCE_ELIPSES11, elipses[10]);
		editor.putFloat(PREFERENCE_ELIPSES12, elipses[11]);
		editor.putFloat(PREFERENCE_ELIPSES13, elipses[12]);
		editor.putFloat(PREFERENCE_ELIPSES14, elipses[13]);
		editor.putFloat(PREFERENCE_ELIPSES15, elipses[14]);
		editor.putFloat(PREFERENCE_ELIPSES16, elipses[15]);
		editor.putFloat(PREFERENCE_ELIPSES17, elipses[16]);
		editor.putFloat(PREFERENCE_ELIPSES18, elipses[17]);
		editor.putFloat(PREFERENCE_ELIPSES19, elipses[18]);
		editor.putFloat(PREFERENCE_ELIPSES20, elipses[19]);
		editor.commit();
	}

	private String convertToString(ArrayList<String> list) {

		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (String s : list) {
			sb.append(delim);
			sb.append(s);
			delim = ";";
		}
		return sb.toString();
	}

	private ArrayList<String> convertToArray(String string) {
		try {
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(";")));
			return list;
		} catch (Exception e) {
			return null;
		}
		
	}

}
