package com.idreaderinternship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManualEntryCheckOut extends AppCompatActivity {
    EditText documentNumber;
    String mState;
    final String PREF_NAME = "My Preferences";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry_check_out);


//        documentNumber = findViewById(R.id.document_number);
//
//        if (savedInstanceState != null && (savedInstanceState.getString("EditText")) != null) {
//            documentNumber = savedInstanceState.getString("EditText");
//        }


    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, PINCodeActivity.class);
        intent.putExtra("activityName", "ManualEntryCheckOut");
        startActivity(intent);
        finish();
    }

    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.check_out_btn:
                documentNumber = findViewById(R.id.document_number);
                if (documentNumber.getText().toString().trim().equals("")) {
                    if (TextUtils.isEmpty(documentNumber.getText())) {
                        documentNumber.setError("Required");
                    }
                } else {
                    spinner = findViewById(R.id.progressBar);
                    spinner.setVisibility(View.VISIBLE);
                    checkOut(documentNumber.getText().toString().trim());
                }
                break;
        }
    }

    private void checkOut(String documentNb) {
        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int guardID = (shared.getInt("Guard ID", 0));

        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/visitor/update/log/checkOut/" + guardID;

        final OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ssn", documentNb)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                call.cancel();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final String jsonData = response.body().string();
                    final JSONObject jsonObject = new JSONObject(jsonData);

                    if (!response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(ManualEntryCheckOut.this, jsonObject.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(ManualEntryCheckOut.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                final boolean success;
                                try {
                                    success = jsonObject.getBoolean("success");
                                    if (!success) {
                                        Toast.makeText(ManualEntryCheckOut.this, "Error: " + jsonObject.getString("message"),
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        spinner = findViewById(R.id.progressBar);
                                        spinner.setVisibility(View.GONE);
                                        Toast.makeText(ManualEntryCheckOut.this, jsonObject.getString("message"),
                                                Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(ManualEntryCheckOut.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ManualEntryCheckOut.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                } catch (final IOException | JSONException e) {
                    Log.d("ERROR", e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String s = e.getMessage();
                            Toast.makeText(ManualEntryCheckOut.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

    //    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        documentNumber.setText(savedInstanceState.getString("Edit_Text"));
//    }
//
//
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EditText", documentNumber.getText().toString());

    }
}
