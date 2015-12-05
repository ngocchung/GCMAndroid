package com.example.gcmandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final Context mContext = this;
    private final String SENDER_ID = "..."; // Project Number at https://console.developers.google.com/project/xxxxxxx
    private final String SHARD_PREF = "com.example.gcmclient_preferences";
    private final String GCM_TOKEN = "gcmtoken";
    private final String LOG_TAG = "GCM_Android";
    public static TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);
        String token = appPrefs.getString(GCM_TOKEN, "");
        if (token.isEmpty()) {
            try {
                getGCMToken(); // this token should be provided to server-side app
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mTextView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getGCMToken() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InstanceID instanceID = InstanceID.getInstance(mContext);
                    String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    if (token != null && !token.isEmpty()) {
                        SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = appPrefs.edit();
                        prefsEditor.putString(GCM_TOKEN, token);
                        prefsEditor.apply();
                    }
                    Log.i(LOG_TAG, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
