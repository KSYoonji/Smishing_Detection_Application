package com.example.smishingdetectionapplication.receiver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.example.smishingdetectionapplication.FirebaseUpload;
import com.example.smishingdetectionapplication.background.DataManager;
import com.example.smishingdetectionapplication.process.PreProcessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MMSMonitor extends Thread {

    private boolean isMonitor = false;
    private boolean isRun = true;
    private Context context;
    Handler handler;

    private final static String TAG = "MMSMonitor---> ";
    private String priorMessageBody = ".";

    private ArrayList<String> idList = new ArrayList<String>(); // 프로그램 시작시에 이 리스트에 기존에 받아놓았던 MMS의 키값들을 저장한다. 즉, 새로 들어온 것만 처리한다.


    public MMSMonitor(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

    public void startMonitor() {
        setIdList();    // 프로그램 시작시에 이 리스트에 기존에 받아놓았던 MMS의 id 값들을 저장. 즉, 비교를 통해 새로 들어온 것만 처리
        isMonitor = true;
    }

    public void stopMonitor() {
        isMonitor = false;
    }

    public void destroyMonitor() {
        isRun = false;
    }

    private void setIdList() {
        idList.clear();

        final String[] projection = new String[]{"_id"};
        Uri uri = Uri.parse("content://mms/inbox");
        Cursor query = context.getContentResolver().query(uri, projection, null, null, "date DESC");

        if (query.moveToFirst()) {
            //Log.d(TAG, "setIdList: if문 내부");

            do {
                String mmsId = query.getString(query.getColumnIndex("_id"));
                idList.add(mmsId);
                //Log.d(TAG, "setIdList: mmsId " + mmsId);

            } while (query.moveToNext());
        }
        query.close();
    }

    private String getMmsText(Context context, String id) {

        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = context.getContentResolver().openInputStream(partURI);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String temp = bufferedReader.readLine();

                while (temp != null) {
                    stringBuilder.append(temp);
                    temp = bufferedReader.readLine();
                }
            }

        } catch (IOException e) {
            Log.d(TAG, "getMmsText: 오류 발생1");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.d(TAG, "getMmsText: 오류 발생2");
                }
            }
        }
        return stringBuilder.toString();
    }

    private String getAddressNumber(Context context, int id) {

        String selectionAdd = new String("msg_id=" + id);
        String uriStr = MessageFormat.format("content://mms/{0}/addr", Integer.toString(id)); // id를 형변환해주지 않으면, 천단위 넘어가면 콤마가 붙으므로 오류 발생
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = context.getContentResolver().query(uriAddress, null, selectionAdd, null, null);

        String phoneNum = null;

        if (cAdd.moveToFirst()) {
            String number = cAdd.getString(cAdd.getColumnIndex("address"));
            if (number != null) {
                try {
                    Long.parseLong(number.replace("-", ""));
                    phoneNum = number;
                } catch (NumberFormatException nfe) {
                    if (phoneNum == null) {
                        phoneNum = number;
                    }
                }
            }
        }

        if (cAdd != null) {
            cAdd.close();
        }

        return phoneNum;
    }

    @Override
    public void run() {
        //Log.d(TAG, "run 내부");

        while (true) {

            setIdList();

            try {
                Thread.sleep(1000);  // 1초 단위로 검사하도록 지정.
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            final String[] projection = new String[]{"_id", "date", "read"};

            Uri uri = Uri.parse("content://mms/inbox");
            Cursor queryForId = context.getContentResolver().query(uri, projection, null, null, "date DESC");

            if (queryForId.moveToFirst()) {

                String mmsId = queryForId.getString(queryForId.getColumnIndex("_id"));
                String mmSRead = queryForId.getString(queryForId.getColumnIndex("read"));

                if (!idList.contains(mmsId)) {

                    Log.d(TAG, "run (아이디): " + mmsId);
                    Log.d(TAG, "run (읽었는지 여부): " + mmSRead);

                    //String date = queryForId.getString(queryForId.getColumnIndex("date"));
                    String incomingNum = getAddressNumber(context, Integer.parseInt(mmsId));

                    String messageBody = "";
                    String selectionPart = "mid=" + mmsId;
                    Uri uriPart = Uri.parse("content://mms/part");
                    Cursor queryForMsg = context.getContentResolver().query(uriPart, null, selectionPart, null, null);

                    if (queryForMsg.moveToFirst()) {
                        Log.d(TAG, "run: queryForMsg 내부");

                        String partId = queryForMsg.getString(queryForMsg.getColumnIndex("_id"));
                        String type = queryForMsg.getString(queryForMsg.getColumnIndex("ct"));

                        if ("text/plain".equals(type)) {
                            String data = queryForMsg.getString(queryForMsg.getColumnIndex("_data"));

                            if (data != null)
                                messageBody = getMmsText(context, partId);
                            else
                                messageBody = queryForMsg.getString(queryForMsg.getColumnIndex("text"));

                            if (incomingNum.length() > 0 && messageBody.length() > 0) {
                                idList.add(mmsId);

                                //전화번호에 hyphen(-)추가
                                incomingNum = PhoneNumberUtils.formatNumber(incomingNum,"KR");

                                String url = ExtractUrl(messageBody);

                                DataManager.putDataString(context, "body",messageBody);

                                Log.d(TAG, "run (mms 내용): " + messageBody);
                                Log.d(TAG, "run (mms 보낸 번호): " + incomingNum);
                                Log.d(TAG, "run (url): " + url);

                                if (PreProcessing.preProcess(incomingNum, messageBody, context)>49) {

                                    Random random = new Random();
                                    int img = random.nextInt(100);

                                    FirebaseUpload.upload(incomingNum,messageBody,url,String.valueOf(img));

                                    Bundle bundle = new Bundle();
                                    bundle.putString("mmsSender", incomingNum);
                                    bundle.putString("mmsUrl", url);
                                    bundle.putString("mmsMsg", messageBody);
                                    bundle.putString("mmsImg", String.valueOf(img));

                                    Message msg = new Message();
                                    msg.setData(bundle);

                                    handler.sendMessage(msg);

                                }

                            }

                        }
                        queryForMsg.close();
                    }
                    //queryForId.moveToNext();

                }

            }
            queryForId.close();
        }
    }

    public static String ExtractUrl(String str) {
        String url = "";

        StringBuffer sb = new StringBuffer();
        String regex = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9\\.@:%._\\+~#=-]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=\\[\\]\\{\\};,\\.ㄱ-ㅎ가-힣]*)";
        String regexIP = "((http(s)?):\\/\\/(www\\.)?)(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/?" ;

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        Pattern p2 = Pattern.compile(regexIP);
        Matcher m2 = p2.matcher(str);

        if (m.find()) {
            sb.append(m.group(0));
            url =  m.group(0);
        }
        else if (m2.find()){
            sb.append(m2.group(0));
            url =  m2.group(0);
        }

        return url;
    }

}




