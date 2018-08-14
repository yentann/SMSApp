package com.example.a17045679.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Register the receiver
    BroadcastReceiver br = new MessageReceiver();



    EditText to;
    EditText content;
    Button send;
    Button viasend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermission();


        to = findViewById(R.id.editText);
        content = findViewById(R.id.editText2);
        send = findViewById(R.id.button);
        viasend = findViewById(R.id.button2);


        //Button send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(to.getText().toString(), null, content.getText().toString(), null, null);
                // Display in Toast
                Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();

                content.setText("");

            }
        });



        //Button via
        viasend.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
        // Creating an intent to initiate view action

            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
            smsIntent.setType("vnd.android-dir/sms");
            smsIntent.setData(Uri.parse("sms:" + to.getText().toString()));
            smsIntent.putExtra("sms_body", content.getText().toString());
            startActivity(smsIntent);
            }
        });






        //Register the receiver
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

    }

    //Unregister the receiver
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }



    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }





}

