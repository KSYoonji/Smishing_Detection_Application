package com.example.smishingdetectionapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.smishingdetectionapplication.FirebaseUpload;
import com.example.smishingdetectionapplication.background.RestartService;
import com.example.smishingdetectionapplication.process.PreProcessing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver---> ";

    public static String sender;
    public static String url;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() 호출");

        Intent i = new Intent(context, RestartService.class);
        context.startService(i);

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);


        if(messages.length > 0){

            i = new Intent(context,RestartService.class);
            i.putExtra("check","11");
            context.startService(i);

            String senderRow = messages[0].getOriginatingAddress();    //발신자 번호
            sender = PhoneNumberUtils.formatNumber(senderRow,"KR");    //전화번호에 hyphen(-)추가
            String content = messages[0].getMessageBody().toString();  //문자 내용
            //Date date = new Date(messages[0].getTimestampMillis());    //받은 시각

            url = PreProcessing.ExtractUrl(content);

            Log.d(TAG, "sender: " + sender);
            Log.d(TAG, "content: " + content);
            Log.d(TAG, "url: " + url);



            if (PreProcessing.preProcess(sender, content, context)>49) {

                Random random = new Random();
                int img = random.nextInt(100);

                //int img = 99;

                SmishingNotification.smishingNoti(context, sender,url,content,String.valueOf(img));
                FirebaseUpload.upload(sender,content,url, String.valueOf(img));

            }

        }
    }


    private SmsMessage[] parseSmsMessage(Bundle bundle){

        Object[] objs = (Object[]) bundle.get("pdus");  // PDU: Protocol Data Units
        SmsMessage[] messages = new SmsMessage[objs.length];

        int smsCount = objs.length;
        for(int i = 0; i < smsCount; i++) {

            //단말 OS 버전 확인: 단말 버전이 마시멜로(M)와 같거나 그 이후 버전이면 실행
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }



}

