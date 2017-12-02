package com.abvrtridentroom.roomconfiguration.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.abvrtridentroom.roomconfiguration.R;

import org.json.JSONObject;

public class SplashActivity extends Activity {

    private final static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        WaitThread waitThread = new WaitThread();
        waitThread.execute();
    }

    class WaitThread extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
