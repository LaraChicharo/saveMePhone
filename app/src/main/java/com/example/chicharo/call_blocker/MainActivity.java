package com.example.chicharo.call_blocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    BroadcastReceiver CallBlocker;

    public static final String ACTION_ANSWER = "answer";
    public static final String ACTION_IGNORE = "ignore";

    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getApplicationContext(), IncomingCallListener.class);
            getApplicationContext().startService(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //performOnBackgroundThread(runnable);

    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


}
