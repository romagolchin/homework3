package ru.ifmo.droid_2016.homework3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.ifmo.droid_2016.homework3.utils.IOUtils;


/**
 * Created by Roman on 28/11/2016.
 */

public class LoadPictureService extends Service {
    private static final String imageUri =
            "https://upload.wikimedia.org/wikipedia/commons/7/77/Florence_Duomo_from_Michelangelo_hill.jpg";
    private static final int bitmapSize = 20_000_000;

    private static final String LOG_TAG = "LoadPictureService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public Bitmap load() {
        HttpURLConnection connection = null;
        FileOutputStream stream = null;
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUri);
            connection = (HttpURLConnection) url.openConnection();
            Log.d(LOG_TAG, "Performing request: " + connection.getURL());
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                Context context = getApplicationContext();
                stream = context.openFileOutput("Florence.jpg", MODE_PRIVATE);
                byte[] buffer = IOUtils.getIOBuffer();
                byte[] bitmapArray = new byte[bitmapSize];
                int readSize;
                int curBitmapSize = 0;
                while ((readSize = in.read(buffer, 0, buffer.length)) >= 0) {
                    stream.write(buffer, 0, readSize);
                    System.arraycopy(buffer, 0, bitmapArray, curBitmapSize, readSize);
                    curBitmapSize += readSize;
                }
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, curBitmapSize);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "", e);
        } finally {
            if (connection != null)
                connection.disconnect();
            IOUtils.closeSilently(stream);
            IOUtils.closeSilently(in);

        }
        return bitmap;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
