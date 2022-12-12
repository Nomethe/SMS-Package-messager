package com.example.smspackagesender;

import static com.example.smspackagesender.PhoneNumbersPage.checkedSendersOnAllActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smspackagesender.senderPack.Sender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    ArrayList<String> checkedSenders;
    String numbersString;
    String[] tabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
        EditText contacts = findViewById(R.id.the_contact_numbers);
        EditText message = findViewById(R.id.the_message);
        Button send = findViewById(R.id.send_message_button);
        FloatingActionButton addContacts = findViewById(R.id.add_contact);

//
//        if (getIntent().hasExtra("data")) {
//            Intent intent = getIntent();
//            checkedSenders = (ArrayList<String>) intent.getSerializableExtra("data");
//            numbersString = String.join("\n", checkedSenders);
//
//            contacts.setText(numbersString);
//            checkedSenders.clear();
//            tabel = numbersString.split("\n");
//
//        }


        if (checkedSendersOnAllActivity.size() != 0) {
            Intent intent = getIntent();
            checkedSenders = checkedSendersOnAllActivity;
            numbersString = String.join("\n", checkedSenders);

            contacts.setText(numbersString);
            checkedSenders.clear();
            tabel = numbersString.split("\n");
            checkedSendersOnAllActivity.clear();

        }


        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (message.getText().length() < 0 || contacts.getText().length() < 10) {
                    Toast.makeText(MainActivity.this, "Some fiedls is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    checkedSenders.addAll(Arrays.asList(tabel));
                    int c = 0;

                    String msg = message.getText().toString();
                    for (String number : checkedSenders) {
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number, null, msg, null, null);
                            ++c;
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Some fiedls is Empty", Toast.LENGTH_LONG).show();
                        }
                    }
                    if (c == checkedSenders.size()) {
                        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneNumbersPage.class);
                startActivity(intent);
            }
        });
    }
}