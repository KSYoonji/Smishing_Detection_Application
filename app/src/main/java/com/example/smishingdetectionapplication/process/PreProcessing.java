package com.example.smishingdetectionapplication.process;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcessing {

    private static String TAG = "PreProcessing---> ";
    public static String n = "";
    public static int processCheck = 1000;  //스미싱이면 1000, 아니면 0
    static boolean senderExists;

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static int preProcess(String sender, String contents, Context context){

        Log.d(TAG, sender);

        //1. 수신된 번호는 114on db에 존재하는 번호인가?
        if (CompareToDB(sender)){
            processCheck = 0;
        }

        //2. 그렇지 않다면...
        if (processCheck == 1000) {

            //2-1. url은 포함되어 있는가? 그렇다면 AI모델 처리해주기
            if (!ExtractUrl(contents).equals("")){

                processCheck = 1000;       //임시 설정
            }

            //2-2. 문자에 전화번호는 포함되어 있는가? :피해자가 전화를 걸고 개인정보를 말하거나 혹은 입력해야 하기 때문에, 한국번호여야만 함.(해외번호 X)
            else if(IncludePhoneNum(contents)){
                if(CompareToDB(n)){       //문자 내부의 전화번호가 114on db에 존재하는가?
                    processCheck = 0;
                }
                else {
                    processCheck = 50;    // 문자 내부에 (url이 없고) 번호가 114on db에 존재하지 않으면: '주의' 표시
                }
            }

            //2-3. url, 전화번호 모두 포함되지 않았다면 : 스미싱 두 번째 유형(전화번호, url 모두 없음) => 추후 개발. 스미싱 알림 X
            else {
                processCheck = 0;
            }
        }

        int result = processCheck;
        processCheck = 1000;


        Log.d(TAG, "return 값: " + result);

        result = 1000; //잠깐 테스트용으로 바꿔놓음
        return result;
    }


    public static boolean CompareToDB(String phoneNum){

        String[] jsonArr = {"강원","경기","경남","경북","광주","기타","대구","대전","부산","서울","세종","울산",
                "인천","전남","전북","제주","충남","충북"};

        for (String area: jsonArr){
            getData(area, phoneNum);
        }

        return senderExists;

    }

    public static void getData(String area, String phoneNumber){

        DatabaseReference ref = database.getReference(area);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange 내부");

                String n = snapshot.getValue().toString();

                if (n.contains(phoneNumber)) {
                    senderExists = true;
                    Log.d(TAG, "db안에 포함되어 있는 전화번호");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        

    }



    public static boolean IncludePhoneNum(String contents){

        try{
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            PhoneNumberMatch matcher = phoneNumberUtil.findNumbers(contents,"KR").iterator().next();

            n = matcher.rawString();
            Log.d(TAG, "텍스트 속 번호: " + n );


        }catch(NoSuchElementException e) {
            Log.d(TAG, "텍스트 속 번호가 포함되어 있지 않습니다.");
            n="";
        }

        return !n.equals("");

    }


    //메시지에서 url 분리
    public static String ExtractUrl(String str) {
        String url = "";

        StringBuffer sb = new StringBuffer();
        String regex = "[(http(s))?:\\/\\/(www\\.)?a-zA-Z0-9\\.@:%._\\+~#=-]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=\\[\\]\\{\\};,\\.ㄱ-ㅎ가-힣]*)";
        String regexIP = "((http(s)?):\\/\\/(www\\.)?)(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/?" ;

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        Pattern p2 = Pattern.compile(regexIP);
        Matcher m2 = p2.matcher(str);

        if (m.find()) {
            sb.append(m.group(0));
            url =  m.group(0);

            assert url != null;
            if (url.startsWith("("))
                url = url.replaceFirst("\\(", "");

        }
        else if (m2.find()){
            sb.append(m2.group(0));
            url =  m2.group(0);
        }

        return url;
    }

}
