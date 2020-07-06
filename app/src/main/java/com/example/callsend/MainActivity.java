package com.example.callsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText phone;
    private EditText message;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        phone = findViewById(R.id.et_phone);
        message = findViewById(R.id.et_message);
    }

    public void goToCall(View view) {
        final String number = phone.getText().toString();
        if (number.length() > 0) {
            callByNumber();
        } else {
            Toast.makeText(MainActivity.this, R.string.enter_phone_number, Toast.LENGTH_SHORT).show();
        }
    }

    private void callByNumber() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            String number = phone.getText().toString();
            Intent openPhone = new Intent(Intent.ACTION_CALL);
            Uri uri = Uri.parse("tel:" + number);
            openPhone.setData(uri);
            startActivity(openPhone);
        }
    }

    public void goToSend(View view) {
        final String number = phone.getText().toString();
        final String text = message.getText().toString();
        if (text.length() > 0 && number.length() > 0) {
            sendMessage();
        } else {
            Toast.makeText(MainActivity.this, R.string.enter_text, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            final String number = phone.getText().toString();
            final String text = message.getText().toString();
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(number, null, text, null, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callByNumber();
            } else {
                finish();
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                finish();
            }
        }
    }
}