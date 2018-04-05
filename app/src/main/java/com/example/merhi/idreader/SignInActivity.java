package com.example.merhi.idreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    GoogleSignInClient mGoogleSignInClient;
    static final int RC_SIGN_IN = 1;
    int MY_KEY = 0;
    final String PREF_NAME = "My Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent mIntent = getIntent();
        MY_KEY = mIntent.getIntExtra("MY_KEY", 0);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }

            }
        });

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            final String personEmail = acct.getEmail();
//            sendGmailMacAddress(personEmail, getMacAddress());
//
//        }
    }

    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null && MY_KEY == 1) {
            signOut();
            MY_KEY = 0;
        } else if (account != null && MY_KEY == 0) {

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            final String personEmail = acct.getEmail();
            sendGmailMacAddress(personEmail, getMacAddress());

            // FOR Testing purpose only comment it when finish
//            SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//            String pin = (shared.getString("PIN CODE", ""));
//            if (pin.equals("")) {
//                Intent goToPinCodeActivity = new Intent(SignInActivity.this, SetPINActivity.class);
//                startActivity(goToPinCodeActivity);
//                finish();
//            } else {
//                Intent goToPinCodeActivity = new Intent(SignInActivity.this, PINCodeActivity.class);
//                startActivity(goToPinCodeActivity);
//                finish();
//            }


        } else if (account == null && MY_KEY == 2) {
            finish();
            startActivity(getIntent());
            MY_KEY = 0;
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void sendGmailMacAddress(String gmail, String macAddress) {
        final TextView textView = findViewById(R.id.testJSON);
        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/guard/check/" + gmail + "/" + macAddress;

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.v("error", e.getMessage());
                        textView.setText(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                final String jsonData = response.body().string();
                Log.v(TAG, jsonData);

                if (response.isSuccessful()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonData);
                                Boolean s = jsonObject.getBoolean("success");
                                String ss = (String) jsonObject.get("message");
                                Toast.makeText(SignInActivity.this, ss, Toast.LENGTH_SHORT).show();
                                if (!s) {
                                    signOut();
                                    MY_KEY = 2;
                                } else if (s) {
                                    SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                    String pin = (shared.getString("PIN CODE", ""));
                                    if (pin.equals("")) {
                                        Intent goToPinCodeActivity = new Intent(SignInActivity.this, SetPINActivity.class);
                                        startActivity(goToPinCodeActivity);
                                        finish();
                                    } else {
                                        Intent goToPinCodeActivity = new Intent(SignInActivity.this, PINCodeActivity.class);
                                        startActivity(goToPinCodeActivity);
                                        finish();
                                    }
                                }
//                                Log.v("success", s);
//                                textView.setText(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }


            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toLowerCase();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}
