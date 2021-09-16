package com.example.smishingdetectionapplication.receiver;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

public class NotiIntentService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handleIntent( intent );
        return null;
    }

    private void handleIntent( Intent intent ) {
        if( intent != null && intent.getAction() != null ) {

            String action= (String) intent.getExtras().get("Button");

            if(action.equalsIgnoreCase("report")){
                Log.i("Info","report Button clicked");
            }
            else if(action.equalsIgnoreCase("block")){
                Log.i("Info","block Button clicked");
            }
        }
    }


}
