package com.example.ashud.texting;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

public class SMSactivity extends AppCompatActivity {

    Bundle fromIntent;
    TextView textView;
    String message = "";

    SMSReceiver smsReceiver = new SMSReceiver();
    SmsManager smsManager;

    boolean first_message = true, wasJustChanged = false;
    int count = 0;

    enum STATES {GREETING, FLIRTING, GOODBYE, CONFUSED}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsactivity);

        textView = (TextView) findViewById(R.id.textview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, 1);
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new SMSReceiver(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    public class SMSReceiver extends BroadcastReceiver {
        public SMSReceiver() {
            super();
        }

        @Override
        public IBinder peekService(Context myContext, Intent service) {
            return super.peekService(myContext, service);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            fromIntent = intent.getExtras();
            Object[] pdus = (Object[]) fromIntent.get("pdus");

            SmsMessage[] array = new SmsMessage[pdus.length];

            for (int i = 0; i < pdus.length; i++) {
                array[i] = SmsMessage.createFromPdu((byte[]) pdus[i], intent.getStringExtra("format"));
                message = array[i].getMessageBody();
            }
            smsManager = SmsManager.getDefault();

            int x = (int) (Math.random() * 3000) + 5000;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smsManager.sendTextMessage("5554", null, AI(), null, null);
                    wasJustChanged = true;
                }
            }, x);
        }

        public STATES nextState(String message) {
            Log.d("TAG", message);
            if (message.toLowerCase().contains("goodbye") ||
                    message.toLowerCase().contains("bye") ||
                    message.toLowerCase().contains("be gone") ||
                    message.toLowerCase().contains("stop") ||
                    message.toLowerCase().contains("ttyl")) {
                if (first_message || !wasJustChanged) {
                    first_message = false;
                    return STATES.CONFUSED;
                }

                else
                    return STATES.GOODBYE;
            }

            else if (message.toLowerCase().contains("hbu")||
                    message.toLowerCase().contains("nothin")||
                    message.toLowerCase().contains("good")||
                    message.toLowerCase().contains("thanks")||
                    message.toLowerCase().contains("lol")||
                    message.toLowerCase().contains("...")||
                    message.toLowerCase().contains("okay")) {
                Log.d("TAG", "HERE");
                return STATES.FLIRTING;
            }

            else if (message.toLowerCase().contains("hello") ||
                     message.toLowerCase().contains("hi") ||
                     message.toLowerCase().contains("what's up") ||
                     message.toLowerCase().contains("hey")) {
                Log.d("TAG", "WRONG PLACE");
                return STATES.GREETING;
            }

            else
                return STATES.CONFUSED;
        }

        public String AI() {
            if (nextState(message) == STATES.GREETING) {
                textView.setText("Greeting");
                int rand = (int) (Math.random() * 4);
                switch (rand) {
                    case 0:
                        return "how you doinn";
                    case 1:
                        return "what's up";
                    case 2:
                        return "what's good";
                    case 3:
                        return "whats goin on";
                }
            }

            else if (nextState(message) == STATES.CONFUSED) {
                textView.setText("CONFUSED");
                return "???";
            }

            else if (nextState(message) == STATES.FLIRTING) {
                Log.d("TAG", "REACHED HERE");

                int rand = (int) (Math.random() * 6);
                textView.setText("FLIRTING");
                switch (rand) {
                    case 0:
                        return "what's cookin good cookin";
                    case 1:
                        return "on a scale of 1 to 10 ur an 11";
                    case 2:
                        return "are you religious? Cuz you're the answer to all my prayers";
                    case 3:
                        return "I was wondering if you had an extra heart. Mine was just stolen";
                    case 4:
                        return " Did it hurt? When you fell from heaven?";
                    case 5:
                        return "Im not a photographer, but I can picture me and you together";
                }
            }

            int rand = (int) (Math.random() * 3);
            textView.setText("GOODBYE");
            switch (rand) {
                case 0:
                    return "Talk to you later!";
                case 1:
                    return "Later";
                case 2:
                    return "Peace out";
            }
            return new String();
        }


    }

}
