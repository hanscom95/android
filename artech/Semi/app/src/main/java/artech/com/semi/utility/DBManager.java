package artech.com.semi.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
	public String name;
	public String password;
	public int location;
	public String team;
	public int sex;
	public String date;
	public String email;

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1
		db.execSQL("CREATE TABLE user\n" +
				"(\n" +
				"    `id`       VARCHAR(45)    NOT NULL, \n" +
				"    `name`       VARCHAR(45)    NOT NULL, \n" +
				"    `state`       INT(1)   , \n" +
				"    PRIMARY KEY (id)\n" +
				");");

		/*db.execSQL("CREATE TABLE market\n" +
				"(\n" +
				"    `user_id`         VARCHAR(45)    NOT NULL    COMMENT '회원 아이디', \n" +
				"    `name`            VARCHAR(45)    NULL        COMMENT '판매자 이름', \n" +
				"    `company`         VARCHAR(45)    NULL        COMMENT '업체명', \n" +
				"    `address`         VARCHAR(45)    NULL        COMMENT '업체 주소', \n" +
				"    `number`          VARCHAR(45)    NULL        COMMENT '사업자등록번호', \n" +
				"    `number_pictrue`  TEXT           NULL        COMMENT '사업자등록번호 사진', \n" +
				"    `bank`            VARCHAR(45)    NULL        COMMENT '은행', \n" +
				"    `account_holder`  VARCHAR(45)    NULL        COMMENT '예금주', \n" +
				"    `account_number`  VARCHAR(45)    NULL        COMMENT '계좌번호', \n" +
				"    PRIMARY KEY (user_id)\n" +
				");" +
				"ALTER TABLE market ADD CONSTRAINT FK_market_user_id_user_user_id FOREIGN KEY (user_id)\n" +
				" REFERENCES user (user_id)  ON DELETE RESTRICT ON UPDATE RESTRICT;");*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("user", null, null);
//		db.delete("market", null, null);
		db.close();

	}

	public void insert(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void update(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void delete(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public String selectUserId() {
		SQLiteDatabase db = getReadableDatabase();
		String _id = "";

		Cursor cursor = db.rawQuery("select id from user;", null);
		while (cursor.moveToNext()) {
			_id = cursor.getString(0);
		}
		return _id;
	}

	public int selectId() {
		SQLiteDatabase db = getReadableDatabase();
		int _id = -1;

		Cursor cursor = db.rawQuery("select count(id) from user;", null);
		while (cursor.moveToNext()) {
			_id = cursor.getInt(0);
		}
		return _id;
	}

    public int selectState() {
        SQLiteDatabase db = getReadableDatabase();
        int state = -1;

        Cursor cursor = db.rawQuery("select state from user;", null);
        while (cursor.moveToNext()) {
			state = cursor.getInt(0);
        }
        return state;
    }

    /**
     *
     * 예약자 추가
     *
     * @param jsonObject
	 *        name
	 *        password
     *        location
     *        team
     *        sex
     *        date
	 *        url
	 *        email
     */
	public void insertUser(JSONObject jsonObject) {
		try {
			String _query = "insert into user values('" + jsonObject.getString("id")+"', '" + jsonObject.getString("name")+"', " + jsonObject.getString("state")+");";
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(_query);
			db.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    /**
     * 예약자 정보 수정
     *
     * @param array
	 *        location
	 *        team
	 *        sex
	 *        date
	 *        url
	 *        email
     */
	public void updateUser(ArrayList<Object> array) {
		String _query = "update user set location='" + array.get(0) + "', team='" + array.get(1) + "', sex='" + array.get(2) + "', date='" + array.get(3)+ "', url='" + array.get(4)  + "', email='" + array.get(5) + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 * 예약자 정보 수정
	 *
	 * @param url
	 */
	public void updateUrl(String url) {
		String _query = "update user set url='" + url + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	public Cursor selectUser() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from `user` ;", null);
	}

	public void removeUser(String id) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from `user` where id='" + id + "';");
		db.close();
	}

}
