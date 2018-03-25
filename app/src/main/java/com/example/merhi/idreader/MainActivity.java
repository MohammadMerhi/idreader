package com.example.merhi.idreader;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, Settings.class);
                startActivity(settings);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToManualEntry(View view) {
        Intent manualEntry = new Intent(MainActivity.this, ManualEntry.class);
        startActivity(manualEntry);
    }

    public void goToScanID(View view) {
        Intent scanID = new Intent(MainActivity.this, ScanID.class);
        startActivity(scanID);
    }
}
