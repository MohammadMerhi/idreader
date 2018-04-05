package com.example.merhi.idreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManualEntry extends AppCompatActivity {
    Spinner nationalitySpinner, typeOfDocumentSpinner, officesSpinner;
    EditText firstName, middleName, lastName, documentNb;
    Button submit;
    // String[] off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        String ip = getString(R.string.IP);
        final String url = "http://" + ip + "/api/visitor/add";

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        final String personEmail = acct.getEmail();
        getOffices(personEmail);


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        nationalitySpinner = findViewById(R.id.nationality);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                countries);

        // Apply the adapter to the spinner
        nationalitySpinner.setAdapter(nationalityAdapter);

        typeOfDocumentSpinner = findViewById(R.id.document_type);
        ArrayAdapter<CharSequence> documentTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.document_type, android.R.layout.simple_spinner_item);
        documentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfDocumentSpinner.setAdapter(documentTypeAdapter);


        firstName = findViewById(R.id.first_name);
        middleName = findViewById(R.id.middle_name);
        lastName = findViewById(R.id.last_name);
        documentNb = findViewById(R.id.document_number);
        submit = findViewById(R.id.submit_btn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().trim().equals("")
                        || middleName.getText().toString().trim().equals("")
                        || lastName.getText().toString().trim().equals("")
                        || documentNb.getText().toString().trim().equals("")) {
                    if (TextUtils.isEmpty(firstName.getText())) {
                        firstName.setError("Required");
                    }
                    if (TextUtils.isEmpty(middleName.getText())) {
                        middleName.setError("Required");
                    }
                    if (TextUtils.isEmpty(lastName.getText())) {
                        lastName.setError("Required");
                    }
                    if (TextUtils.isEmpty(documentNb.getText())) {
                        documentNb.setError("Required");
                    }
                } else {
                    doPost(url);

                }
            }
        });


    }

    private void doPost(String url) {

        Log.d("POST", "doPost() called.");

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();


        typeOfDocumentSpinner = findViewById(R.id.document_type);
        String TYDS = typeOfDocumentSpinner.getSelectedItem().toString();

        nationalitySpinner = findViewById(R.id.nationality);
        String NS = nationalitySpinner.getSelectedItem().toString();

        officesSpinner = findViewById(R.id.destination);
        Office office = (Office) officesSpinner.getSelectedItem();


        firstName = findViewById(R.id.first_name);
        middleName = findViewById(R.id.middle_name);
        lastName = findViewById(R.id.last_name);
        documentNb = findViewById(R.id.document_number);
        submit = findViewById(R.id.submit_btn);

        JSONObject object = new JSONObject();
        try {
            object.put("firstName", firstName.getText().toString());
            object.put("middleName", middleName.getText().toString());
            object.put("lastName", lastName.getText().toString());
            object.put("documentType", TYDS);
            object.put("ssn", documentNb.getText().toString());
            object.put("nationality", NS);
            object.put("officeId", office.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(JSON, object.toString());
        Log.d("POST", "RequestBody() created.");

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ManualEntry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                                    Toast.makeText(ManualEntry.this, jsonObject.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(ManualEntry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ManualEntry.this, "Error: " + jsonObject.getString("message"),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ManualEntry.this, jsonObject.getString("message"),
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ManualEntry.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ManualEntry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ManualEntry.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    public void getOffices(String gmail) {
        final TextView textView = findViewById(R.id.testJSON);
        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/office/getOffices/" + gmail;

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
                        textView.setText(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                try {
                    final String jsonData = response.body().string();

                    if (response.isSuccessful()) {

                        final JSONObject jsonObject = new JSONObject(jsonData);
                        final Boolean s = jsonObject.getBoolean("success");
                        final JSONArray offices = jsonObject.getJSONArray("offices");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (s) {
                                    try {
                                        stringOfficeArray(offices);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("offices", jsonData);
                                    Toast.makeText(ManualEntry.this, "GOOD WORK", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ManualEntry.this, "BAD WORK", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void stringOfficeArray(JSONArray data) throws JSONException {
        officesSpinner = findViewById(R.id.destination);
        ArrayList<Office> offices = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            JSONObject officeJSON = data.getJSONObject(i);
            Office office = new Office();

            office.setId(officeJSON.getInt("id"));
            office.setOfficeName(officeJSON.getString("officeNb"));
            office.setFloorNb(officeJSON.getInt("floorNb"));
            office.setPremiseOwner(officeJSON.getString("premiseOwner"));

            offices.add(office);
        }

        ArrayAdapter<Office> destinationAdapter = new ArrayAdapter<Office>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                offices
        );
        officesSpinner.setAdapter(destinationAdapter);
    }

}