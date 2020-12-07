package se.com.band.utility;

public class Constants {
	// Service handler message key
	public static final String SERVICE_HANDLER_MSG_KEY_DEVICE_NAME = "device_name";
	public static final String SERVICE_HANDLER_MSG_KEY_DEVICE_ADDRESS = "device_address";
	public static final String SERVICE_HANDLER_MSG_KEY_TOAST = "toast";
	
	//extra key
	public static final String JOIN_FLAG = "join_flag";
	public static final String USER_NAME = "user_name";
	public static final String USER_ID = "user_id";
	public static final String UID = "uid";
	public static final String USER_PW = "user_pw";
	public static final String USER_BIRTH = "user_birth";
	public static final String USER_GENDER = "user_gender";
	public static final String USER_TALL = "user_tall";
	public static final String USER_WEIGHT = "user_weight";
	public static final String USER_TALL_UNIT = "user_tall_unit";
	public static final String USER_WEIGHT_UNIT = "user_weight_unit";
	public static final String USER_DEVICE_ADDRESS = "user_device_address";
	public static final String USER_ACTIVITY_GOAL = "user_activity_goal";
	public static final String USER_SLEEP_GOAL = "user_sleep_goal";
	public static final String EXTRA_DEVICE_ADDRESS = "device_address";
	public static final String EXTRA_DEVICE_NAME = "device_name";
	public static final String BROADCAST_RECEIVER = "broadcast_recevier";
	public static final String USER_HAND = "user_hand";
	public static final String USER_AGE = "user_age";
	
	// Preference
	public static final String PREFERENCE_NAME_TODAY = "standingeggs_today";
	public static final String PREFERENCE_NAME = "standingeggs";
	public static final String PREFERENCE_KEY_BG_SERVICE = "BackgroundService";
	public static final String PREFERENCE_CONN_INFO_ADDRESS = "device_address";
	public static final String PREFERENCE_CONN_INFO_NAME = "device_name";
	public static final String PREFERENCE_VERSION_OF_API = "version_of_api";
	public static final String LAST_USE_DATE = "last_use_date";
	public static final String LAST_MONTH = "last_month";
	public static final String LAST_DAY = "last_day";
	public static final String LAST_HOUR = "last_hour";
	
	public static final String PREFERENCE_USER_ID = "user_id";
	public static final String PREFERENCE_UID = "uid";
	public static final String PREFERENCE_USER_NM = "user_nm";
	public static final String PREFERENCE_USER_PW = "user_pw";
	public static final String PREFERENCE_USER_BIRTH = "user_birth";
	public static final String PREFERENCE_USER_GENDER = "user_gender";
	public static final String PREFERENCE_USER_TALL = "user_tall";
	public static final String PREFERENCE_USER_WEIGHT = "user_weight";
	public static final String PREFERENCE_USER_TALL_UNIT = "user_tall_unit";
	public static final String PREFERENCE_USER_WEIGHT_UNIT = "user_weight_unit";
	public static final String PREFERENCE_USER_DEVICEADDRESS = "user_deviceaddress";
	public static final String PREFERENCE_USER_DEVICENAME = "user_devicename";
	public static final String PREFERENCE_USER_ACTIVITY_GOAL = "user_activity_goal";
	public static final String PREFERENCE_USER_SLEEP_GOAL = "user_sleep_goal";
	public static final String PREFERENCE_USING_DATE = "preference_using_date";
	public static final String PREFERENCE_DIS_UNIT = "preference_dis_unit";
	public static final String PREFERENCE_USER_HAND = "user_hand";
	public static final String PREFERENCE_USER_AGE = "user_age";

	public static final String PREFERENCE_CALL_NOTI = "setting_call_noti";
	public static final String PREFERENCE_SMS_NOTI = "setting_sms_noti";
	public static final String PREFERENCE_SNS_NOTI = "setting_sns_noti";
	public static final String PREFERENCE_MOVE_NOTI = "setting_move_noti";
	public static final String PREFERENCE_DEVICE_CONN = "setting_device_conn";

	public static final String PREFERENCE_CALL_LED_COLOR = "setting_call_led_color";
	public static final String PREFERENCE_SMS_LED_COLOR = "setting_sms_led_color";
	public static final String PREFERENCE_KAKAO_LED_COLOR = "setting_kakao_led_color";
	public static final String PREFERENCE_WECHAT_LED_COLOR = "setting_wechat_led_color";
	public static final String PREFERENCE_QQ_LED_COLOR = "setting_qq_led_color";
	
	public static final String PREFERENCE_ALARM_TIME_TEXT= "alarm_time_text";
	public static final String PREFERENCE_ALARM_ONOFF= "alarm_onoff";

	public static final String PREFERENCE_ALERT_EVERY_TEXT= "alert_every_text";
	public static final String PREFERENCE_ALERT_START_TEXT = "alert_start_text";
	public static final String PREFERENCE_ALERT_END_TEXT= "alert_end_text";

	public static final String PREFERENCE_MOVE_EVERY_TEXT= "alert_every_text";
	public static final String PREFERENCE_MOVE_START_TEXT = "alert_start_text";
	public static final String PREFERENCE_MOVE_END_TEXT= "alert_end_text";

	public static final String PREFERENCE_SNS_FIRST_TEXT= "sns_first_text";
	public static final String PREFERENCE_SNS_SECOND_TEXT = "sns_second_text";
	public static final String PREFERENCE_SNS_THIRD_TEXT= "sns_third_text";

	public static final String PREFERENCE_LTE_4G_FLAG = "preference_lte_4g_flag";
	
	public static final String PREFERENCE_ACT_GOAL_CNT = "preference_act_goal_cnt";

	public static final String PREFERENCE_DAILY_TOTAL_STEP = "daily_total_step";
	public static final String PREFERENCE_DAILY_TOTAL_DISTANCE = "daily_total_distance";
	public static final String PREFERENCE_DAILY_TOTAL_KCAL = "daily_total_kcal";
	public static final String PREFERENCE_DAILY_TOTAL_TIME = "daily_total_time";

	public static final String PREFERENCE_WEEKLY_TOTAL_STEP = "weekly_total_step";
	public static final String PREFERENCE_WEEKLY_TOTAL_DISTANCE = "weekly_total_distance";
	public static final String PREFERENCE_WEEKLY_TOTAL_KCAL = "weekly_total_kcal";
	public static final String PREFERENCE_WEEKLY_TOTAL_TIME = "weekly_total_time";

	public static final String PREFERENCE_MONTHLY_TOTAL_STEP = "monthly_total_step";
	public static final String PREFERENCE_MONTHLY_TOTAL_DISTANCE = "monthly_total_distance";
	public static final String PREFERENCE_MONTHLY_TOTAL_KCAL = "monthly_total_kcal";
	public static final String PREFERENCE_MONTHLY_TOTAL_TIME = "monthly_total_time";

	public static final String PREFERENCE_YEARLY_TOTAL_STEP = "yearly_total_step";
	public static final String PREFERENCE_YEARLY_TOTAL_DISTANCE = "yearly_total_distance";
	public static final String PREFERENCE_YEARLY_TOTAL_KCAL = "yearly_total_kcal";
	public static final String PREFERENCE_YEARLY_TOTAL_TIME = "yearly_total_time";


	public static final String DAY_UNIT = "day_unit";

	public static final String RECEIVER_BATTERY = "battery_receiver";


	// Message types sent from Service to Activity
	public static final int MESSAGE_CMD_ERROR_NOT_CONNECTED = -50;

	public static final int MESSAGE_BT_SCAN_STARTED = 111;
	public static final int MESSAGE_BT_NEW_DEVICE = 112;
	public static final int MESSAGE_BT_SCAN_FINISHED = 113;

	public static final int MESSAGE_BT_STATE_INITIALIZED = 1;
	public static final int MESSAGE_BT_STATE_LISTENING = 2;
	public static final int MESSAGE_BT_STATE_CONNECTING = 3;
	public static final int MESSAGE_BT_STATE_CONNECTED = 4;
	public static final int MESSAGE_BT_STATE_DISCONNECTED = 5;

	public static final int CONNECT_LOST = 6;

	public static final int MESSAGE_BT_STATE_ERROR = 10;

	public static final int MESSAGE_READ_CHAT_DATA = 201;
	public static final int MESSAGE_WRITE_CHAT_DATA = 202;

	// Intent request codes
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
	public static final int REQUEST_LOGIN = 3;
	public static final int ACTIVITY_SLEEP_FLAG = 4;
	public static final int REQUEST_SIGN_OUT = 5;
	public static final int REQUEST_SUPPORT_FIRMWARE = 6;
	public static final int RESULT_FIRM_WARE = 888;

	public static final int ALARM_ADD = 10;
	public static final int MODIFY_USER = 20;


	public static final int DAILY = 101;
	public static final int WEEKLY = 102;
	public static final int MONTHLY = 103;
	
	// BroadCast request codes
	public static final int BROADCAST_BAT = 1;
	public static final int BROADCAST_FIND = 2;
	public static final int BROADCAST_ALARM_SET = 3;
	public static final int BROADCAST_KAKAOTALK = 4;
	public static final int BROADCAST_VERSION = 5;
	public static final int BROADCAST_WECHAT = 6;
	public static final int BROADCAST_QQ = 7;
	
	//server Request code
	public static final int REQUEST_OK = 200;
	public static final int REQUEST_DUPLICATE_USER_ID = 222;	//ID 중복
	public static final int REQUEST_DUPLICATE_DEVICE_ADDRESS = 223;	//address 중복
	public static final int REQUEST_INVAILD_VALUE = 400;
	public static final int REQUEST_INVALID_DATA_VALUE = 417;   //birth 형식 0000-00-00
	public static final int REQUEST_DEVICE_ADDRESS = 480;   //birth 형식 0000-00-00
	public static final int REQUEST_DB_ERROR = 500; //비밀번호 틀림
	
	public static final int REQUEST_USER_IS_NOT = 460;  //아이디 X
	public static final int REQUEST_PW_NOT_MATCH = 470; //비밀번호 틀림
	public static final int REQUEST_DEVICE_ADDRESS_DOES_NOT_MATCHED = 471; //디바이스 주소 다름
	public static final int REQUEST_INVALID_DEVICE_ADDRESS = 480; //유효하지 않은 device address
	
	public static final int REQUEST_METHOD_NOT_ALLOWED = 405; //METHOD NOT ALLOWED


	public static final int ACTIVITY_FLAG = 1;
	public static final int MOTION_FLAG = 2;


	//motion code
	public static final int MOTION_STANDING = 0;
	public static final int MOTION_SLOW_WALKING = 1;
	public static final int MOTION_WALKING = 2;
	public static final int MOTION_RUNNING = 3;
	public static final int MOTION_SITTING = 4;
	public static final int MOTION_JUMPING = 5;
	public static final int MOTION_PUSH_UP = 6;
	public static final int MOTION_SIT_UP = 7;
	public static final int MOTION_BUTTERFLY = 8;
	public static final int MOTION_BICEPS_CURL = 9;
	public static final int MOTION_SHOULDER_PRESS = 10;
	public static final int MOTION_CRUNCH = 11;
	public static final int MOTION_UPRIGHT_ROW = 12;
	public static final int MOTION_TOE_RAISE = 13;
	public static final int MOTION_PEC_FLY = 14;
	public static final int MOTION_CHEST_PRESS = 15;
	public static final int MOTION_LATERAL_RAISE = 17;
	public static final int MOTION_KETTLEBELL_SWING = 30;
	public static final int MOTION_SEATED_CABLE_ROW = 32;
	public static final int MOTION_DUMBELL_TRICEPS_PRESS = 35;
	public static final int MOTION_BENCH_KICKBACK = 37;
	public static final int MOTION_SQUAT = 38;
	public static final int MOTION_ALTERNATE_DELTOID_RAISE = 39;




	// Message types sent from the BluetoothManager to Handler
	public static final int MESSAGE_DATE_TIME_REQUEST = 10;
	public static final int MESSAGE_DATE_TIME_CONTENT = 11;
	public static final int MESSAGE_DATE_TIME_RECEIVED = 12;

	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_REQUEST = 13;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_ACCEPT = 14;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_CONTENT = 15;
	public static final int MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED = 16;

	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_REQUEST = 19;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_ACCEPT = 20;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT = 21;
	public static final int MESSAGE_SAVED_ACTIVITY_REPORT_RECEIVED = 22;

	public static final int MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT = 23;

	public static final int MESSAGE_DATA_COMPUTATION_HOLD_REQUEST = 24;
	public static final int MESSAGE_DATA_COMPUTATION_HOLD_ACCEPT = 25;

	public static final int MESSAGE_DATA_COMPUTATION_RESUME_REQUEST = 26;
	public static final int MESSAGE_DATA_COMPUTATION_RESUME_ACCEPT = 27;

	public static final int MESSAGE_VIBRATION_REQUEST = 28;
	public static final int MESSAGE_VIBRATION_ACCEPT = 29;

	public static final int MESSAGE_HEART_RATE_REQUEST = 43;
	public static final int MESSAGE_HEART_RATE_ACCEPT = 44;

	public static final int MESSAGE_BAND_MODE_REQUEST = 45;
	public static final int MESSAGE_BAND_MODE_ACCEPT = 46;

	public static final int MESSAGE_SLEEP_STATUS_REQUEST = 47;
	public static final int MESSAGE_SLEEP_STATUS_CONTENT = 48;
	public static final int MESSAGE_SLEEP_STATUS_RECEIVED = 49;

	public static final int MESSAGE_REACHED_STEP_GOAL_REQUEST = 50;
	public static final int MESSAGE_REACHED_STEP_GOAL_RECEIVED = 51;

	public static final int MESSAGE_REACHED_CALORIE_GOAL_REQUEST = 52;
	public static final int MESSAGE_REACHED_CALORIE_GOAL_RECEIVED = 53;

	public static final int MESSAGE_HEART_ALARM_REQUEST = 54;
	public static final int MESSAGE_HEART_ALARM_RECEIVED = 55;

	public static final int MESSAGE_SELECT_EXERCISE_REQUEST = 56;
	public static final int MESSAGE_SELECT_EXERCISE_ACCEPT = 57;

	public static final int MESSAGE_APP_NOTIFICATION_REQUEST = 58;
	public static final int MESSAGE_APP_NOTIFICATION_ACCEPT = 59;
	public static final int MESSAGE_APP_NOTIFICATION_CONTENT = 60;
	public static final int MESSAGE_APP_NOTIFICATION_RECEIVED = 61;

//	public static final int MESSAGE_BATTERY_REQUEST = 49;
//	public static final int MESSAGE_BATTERY_CONTENT = 50;


	//sleep status
	public static final int SLEEP_STATUS_SLEEP = 0;
	public static final int SLEEP_STATUS_WAKE_UP = 1;
	public static final int SLEEP_STATUS_TAKEN_OFF = 2;
	public static final int SLEEP_STATUS_READY = 3;
	public static final int SLEEP_STATUS_DSLEEP = 4;



}
