package com.idreaderinternship;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class Settings extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    final String PREF_NAME = "My Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void goToChangePin(View view) {
        Intent changePinCode = new Intent(Settings.this, ChangePINCode.class);
        startActivity(changePinCode);
    }


    public void logOut(View view) {
        createLogoutDialog();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, PINCodeActivity.class);
        intent.putExtra("activityName", "Settings");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void createLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Are you sure you want to logout");

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("MY_KEY", 0);
                editor.apply();

                Intent logout = new Intent(Settings.this, SignInActivity.class);
                startActivity(logout);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
