package com.example.merhi.idreader;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScanID extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_id);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
