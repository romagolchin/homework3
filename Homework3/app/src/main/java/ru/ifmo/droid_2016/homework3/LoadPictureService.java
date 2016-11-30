package ru.ifmo.droid_2016.homework3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by Roman on 28/11/2016.
 */

public class LoadPictureService extends Service {
    private boolean started = false;
    private static final String imageUri =
            "https://upload.wikimedia.org/wikipedia/commons/7/77/Florence_Duomo_from_Michelangelo_hill.jpg";
    private static final String LOG_TAG = "LoadPictureService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started) {
            // in case that action was performed several times before load was finished
            started = true;
            Context context = getApplicationContext();
            new AsyncImageDownloader().execute(context);
        }
//        Intent mIntent = new Intent("ru.ifmo.droid_2016.homework3.LoadFinished");
//        sendBroadcast(mIntent);
        return START_STICKY;
    }
}
