package com.example.smishingdetectionapplication.background;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smishingdetectionapplication.receiver.MMSMonitor;
import com.example.smishingdetectionapplication.receiver.SMSReceiver;
import com.example.smishingdetectionapplication.receiver.SmishingNotification;

import java.util.Calendar;

public class RestartService extends Service {

    public static Intent serviceIntent = null;
    String BACKGROUND_CHANNEL_ID = "5678";
    private static final String TAG = "RestartService---> ";
    Context context = this;

    SMSReceiver mSmsReceiver;

    private static String check;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;

        try{
            check = intent.getStringExtra("check");

            mSmsReceiver = new SMSReceiver();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

            MmsHandler mmsHandler = new MmsHandler();
            MMSMonitor mmsMonitor = new MMSMonitor(this, mmsHandler);
            mmsMonitor.start();

            //브로드캐스트,스레드 등
//            if (check == "11"){
//                mSmsReceiver = new SMSReceiver();
//                IntentFilter mIntentFilter = new IntentFilter();
//                mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//
//                registerReceiver(mSmsReceiver, mIntentFilter);
//
//            } else{
//                MmsHandler mmsHandler = new MmsHandler();
//                MMSMonitor mmsMonitor = new MMSMonitor(this, mmsHandler);
//                mmsMonitor.start();
//            }

        }catch (RuntimeException e){
            Log.d(TAG, "예외: " + e);
            initializeNotification();
        }

        initializeNotification();

        return START_STICKY;
    }


    public void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BACKGROUND_CHANNEL_ID);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "스미싱탐정 background";
            String description = "API 26인 경우 채널 필요 + for bg";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(BACKGROUND_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록. 오레오 버전부터는 채널을 안만들면 알림 받을 수 x
            notificationManager.createNotificationChannel(channel);
        }

        builder.setContentText(null)
                .setContentTitle(null)
                .setWhen(0)
                .setShowWhen(false);

        startForeground(5678, builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {      //강제종료
        super.onTaskRemoved(rootIntent);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    public class MmsHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            String mmsSender = msg.getData().getString("mmsSender");
            String mmsUrl = msg.getData().getString("mmsUrl");
            String mmsMsg = msg.getData().getString("mmsMsg");
            String mmsImg = msg.getData().getString("mmsImg");

            SmishingNotification.smishingNoti(context, mmsSender, mmsUrl,mmsMsg,mmsImg);

        }
    }

}
