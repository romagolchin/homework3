package ru.ifmo.droid_2016.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ru.ifmo.droid_2016.homework3.utils.IOUtils;

public class PictureActivity extends AppCompatActivity {
    public static final String intentAction= "ru.ifmo.droid_2016.homework3.LoadFinished";
    private boolean loaded = false;
    private ImageView imageView;
    private TextView textView;
    private BroadcastReceiver receiver;
    private static final String fileName = "Florence.jpg";
    private static final String LOG_TAG = PictureActivity.class.getSimpleName();
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        imageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.error);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loaded) {
            receiver = new FinishedLoadReceiver();
            IntentFilter intentFilter = new IntentFilter(intentAction);
            registerReceiver(receiver, intentFilter);
        } else
            DisplayImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!loaded)
            unregisterReceiver(receiver);
    }

    private void DisplayImage() {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            Log.e(LOG_TAG, "failed to get bitmap");
        }
    }

    class AsyncProcessBitmap extends AsyncTask<Context, Void, Bitmap> {
        @Override
        @Nullable
        protected Bitmap doInBackground(Context... contexts) {
            if (bitmap == null) {
                InputStream is = null;
                Bitmap tempBitmap = null;
                try {
                    is = openFileInput(fileName);
                    tempBitmap = BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException e) {
                    Log.e(LOG_TAG, "", e);
                } finally {
                    IOUtils.closeSilently(is);
                }
                bitmap = tempBitmap;
                return tempBitmap;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            DisplayImage();
        }
    }

    class FinishedLoadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loaded = true;
            new AsyncProcessBitmap().execute();
        }

    }
}
