package com.example.merhi.idreader;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Settings extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;

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
        Intent logout = new Intent(this, SignInActivity.class);
        logout.putExtra("MY_KEY", 1);
        startActivity(logout);
        finish();
    }
}
