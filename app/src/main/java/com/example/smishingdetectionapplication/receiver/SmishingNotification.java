package com.example.smishingdetectionapplication.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.smishingdetectionapplication.activity.MainActivity;
import com.example.smishingdetectionapplication.R;

public class SmishingNotification {

    private static final String TAG = "SmishingNoti---> ";
    public static final String NOTIFICATION_CHANNEL_ID = "1234";


    public static void smishingNoti(Context context, String sender, String url, String msg, String img){   //, Intent intent

        //커스텀 화면 만들기
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.title, "스미싱이 의심되는 문자입니다!");

        Intent intentMain = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntentMain = PendingIntent.getActivity(context.getApplicationContext(), 333, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);

        //확장되는 커스텀 화면 만들기
        RemoteViews expendedRemoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_noti_expended);

        Intent intentBlock = new Intent(context.getApplicationContext(), NotiIntentService.class);
        //intentBlock.setAction("block");
        intentBlock.putExtra("Button", "block");
        PendingIntent pendingIntentBlock = PendingIntent.getService(context.getApplicationContext(), 111, intentBlock, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentReport = new Intent(context.getApplicationContext(), NotiIntentService.class);
        //intentReport.setAction("report");
        intentBlock.putExtra("Button", "report");
        PendingIntent pendingIntentReport = PendingIntent.getActivity(context.getApplicationContext(), 222, intentReport, PendingIntent.FLAG_UPDATE_CURRENT);

        if (url == "")
            url = "url을 포함하고 있지 않습니다";

        expendedRemoteViews.setTextViewText(R.id.caution, "스미싱이 의심되는 문자입니다!");
        expendedRemoteViews.setImageViewResource(R.id.divider1, R.drawable.divider);
        expendedRemoteViews.setTextViewText(R.id.phone_number, sender);
        expendedRemoteViews.setImageViewResource(R.id.divider2, R.drawable.divider);
        expendedRemoteViews.setTextViewText(R.id.url, url);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "1234");

        assert notificationManager != null;

        CreateNotificationChannel(builder, notificationManager);

        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntentMain)
                .setCustomBigContentView(expendedRemoteViews)
//                .setContentIntent(pendingIntentReport)
//                .setContentIntent(pendingIntentBlock)

                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(2000)    //모델 탑재 후 자연스럽게 딜레이 될 수 있으니 우선 그대로 놔둠.System.currentTimeMillis()
                .setAutoCancel(false)
                .setOngoing(false);

        notificationManager.notify(1234, builder.build());
    }

    //OREO API 26 이상에서는 채널 필요
    private static void CreateNotificationChannel(NotificationCompat.Builder builder, NotificationManager notificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.logo); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남. ic_launcher_foreground

            CharSequence channelName = "스미싱탐정";
            String description = "API 26인 경우 채널 필요";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

    }


}
