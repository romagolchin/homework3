package ru.ifmo.droid_2016.homework3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.ifmo.droid_2016.homework3.utils.IOUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Roman on 30/11/2016.
 */

public class AsyncImageDownloader extends AsyncTask<Context, Void, Void> {
    private static final String imageUri =
            "https://upload.wikimedia.org/wikipedia/commons/7/77/Florence_Duomo_from_Michelangelo_hill.jpg";
    private static final String LOG_TAG = "LoadPictureService";
    @Override
    protected Void doInBackground(Context... contexts) {
        HttpURLConnection connection = null;
        FileOutputStream stream = null;
        InputStream in = null;
        Context context = contexts[0];
        try {
            URL url = new URL(imageUri);
            connection = (HttpURLConnection) url.openConnection();
            Log.d(LOG_TAG, "Performing request: " + connection.getURL());
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                stream = context.openFileOutput("Florence.jpg", MODE_PRIVATE);
                byte[] buffer = IOUtils.getIOBuffer();
                int readSize;
                while ((readSize = in.read(buffer, 0, buffer.length)) >= 0) {
                    stream.write(buffer, 0, readSize);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "", e);
        } finally {
            if (connection != null)
                connection.disconnect();
            IOUtils.closeSilently(stream);
            IOUtils.closeSilently(in);

        }
        Intent mIntent = new Intent(PictureActivity.intentAction);
        context.sendBroadcast(mIntent);
        return null;
    }

}
