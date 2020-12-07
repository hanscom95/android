package com.standingegg.band.service;

import com.standingegg.band.MainActivity.MyReceiver;
import com.standingegg.band.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
 
 
public class NotificationService extends NotificationListenerService {
 
    Context context;
 
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        if(pack.equals("com.tencent.mm")) {//if(pack.equals("com.kakao.talk")) {
            Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
    		intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_WECHAT);
    		sendBroadcast(intent);	
        }else if(pack.equals("com.qq.tencent")) {//if(pack.equals("com.kakao.talk")) {
            Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
            intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_QQ);
            sendBroadcast(intent);
        }
    }
 
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}