package com.info121.imessenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.info121.imessenger.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // startActivity(new Intent(MainActivity.this, LoginActivity.class));
        startActivity(new Intent(MainActivity.this, ContactsActivity.class));
    }
}