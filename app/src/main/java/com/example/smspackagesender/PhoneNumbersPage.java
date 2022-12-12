package com.example.smspackagesender;

import static com.example.smspackagesender.RecyclerAdapter.unchecked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.example.smspackagesender.senderPack.Sender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PhoneNumbersPage extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<Sender> senders = new ArrayList<>();
    Set<String> nonduplcatedlist;
    ArrayList<String> checkedSenders = new ArrayList<>() ;
    public static  ArrayList<String> checkedSendersOnAllActivity = new ArrayList<>() ;
    List<Integer> posi;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers_page);

        RecyclerView recyclerView = findViewById(R.id.contact_list);
        FloatingActionButton sendData = findViewById(R.id.data_sender);
        CheckBox checkAll = findViewById(R.id.allcheckd);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor.getCount()>=0){
            while (cursor.moveToNext()) {

               @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
               @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                senders.add(new Sender(name,number));

            }
        }

        recyclerAdapter = new RecyclerAdapter(this,senders);
        recyclerView.setItemViewCacheSize(senders.size());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll.isChecked()){
                    recyclerAdapter.setAllChecked(true);
                }else {
                    recyclerAdapter.setAllChecked(false);
                }
            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi=new ArrayList<>();
                posi=recyclerAdapter.getPositins();
                for (int i:posi) {
                    checkedSenders.add(senders.get(i).getNumber());
                }

                nonduplcatedlist = new TreeSet<>(checkedSenders);
                checkedSenders.clear();
                checkedSenders.addAll(nonduplcatedlist);
                checkedSendersOnAllActivity.addAll(checkedSenders);





                Intent intent = new Intent(PhoneNumbersPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (unchecked == 1){
                    checkAll.setChecked(false);
                }else if (unchecked == 0){
                    checkAll.setChecked(true);
                }

                handler.postDelayed(this, 200);
            }
        },1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.searchin_item);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search People");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Back Home");
                builder.setMessage("Are you sure you want to cancel the operation");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PhoneNumbersPage.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerAdapter.filter(newText);
        return true;
    }
}