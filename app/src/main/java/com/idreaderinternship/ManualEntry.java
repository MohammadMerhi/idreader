package com.idreaderinternship;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.graphics.Color.RED;

public class ManualEntry extends AppCompatActivity {
    Spinner nationalitySpinner, typeOfDocumentSpinner, officesSpinner;
    EditText firstName, lastName, documentNb;
    Button addBtn, saveBtn;
    private String m_Text = "";
    final String PREF_NAME = "My Preferences";
    Gson gson;
    String x = "x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            x = extras.getString("KEY_T");
        }

        if (isNetworkAvailable()) {

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            final String personEmail = acct.getEmail();
            getOffices(personEmail);

            if (x.equals("x")) {
                createDialog();
            } else {
                try {
                    JSONObject jsonObjectResult = new JSONObject(x);
                    String ssn = jsonObjectResult.getString("ssn");
                    checkVisitor(ssn, jsonObjectResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String savesJsonOffices = (shared.getString("Offices", ""));

            officesSpinner = findViewById(R.id.destination);
            Type type = new TypeToken<List<Office>>() {
            }.getType();
            gson = new Gson();
            List<Office> officeList = gson.fromJson(savesJsonOffices, type);

            ArrayAdapter<Office> destinationAdapter = new ArrayAdapter<Office>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    officeList
            );
            officesSpinner.setAdapter(destinationAdapter);
        }


        setAdapters();
        initializeForm();
        addBtn = findViewById(R.id.add_btn);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TYDS = typeOfDocumentSpinner.getSelectedItem().toString();
                String nationality = nationalitySpinner.getSelectedItem().toString();
                Office office = (Office) officesSpinner.getSelectedItem();
                Office toComp = new Office("Choose ");
                if (firstName.getText().toString().trim().equals("")
                        || lastName.getText().toString().trim().equals("")
                        || documentNb.getText().toString().trim().equals("")
                        || TYDS.equals("Choose Document Type")
                        || nationality.equals("Choose Office")
                        || areEqual(office, toComp)) {
                    if (TextUtils.isEmpty(firstName.getText())) {
                        firstName.setError("Required");
                    }
                    if (TextUtils.isEmpty(lastName.getText())) {
                        lastName.setError("Required");
                    }
                    if (TextUtils.isEmpty(documentNb.getText())) {
                        documentNb.setError("Required");
                    }
                    if (TYDS.equals("Choose Document Type")) {
                        TextView req = findViewById(R.id.req_tyds_spinner);
                        req.setText("Required");
                    } else {
                        TextView req = findViewById(R.id.req_tyds_spinner);
                        req.setText("");
                    }
                    if (nationality.equals("Choose Nationality")) {
                        TextView req = findViewById(R.id.req_nationality_spinner);
                        req.setText("Required");
                    } else {
                        TextView req = findViewById(R.id.req_nationality_spinner);
                        req.setText("");
                    }
                } else {
                    doPost();

                }
            }
        });


    }

    public Boolean areEqual(Office x, Office y) {
        if (x.getId() == y.getId()
                && x.getFloorNb() == y.getFloorNb()
                && x.getOfficeName().equals(y.getOfficeName())
                && x.getPremiseOwner().equals(y.getPremiseOwner())) {
            return true;
        }
        return false;
    }

    private void doPost() {

        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/visitor/add";

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int guardID = (shared.getInt("Guard ID", 0));

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();

        initializeForm();

        String TYDS = typeOfDocumentSpinner.getSelectedItem().toString();


        String NS = nationalitySpinner.getSelectedItem().toString();

        officesSpinner = findViewById(R.id.destination);
        Office office = (Office) officesSpinner.getSelectedItem();

        addBtn = findViewById(R.id.add_btn);

        JSONObject object = new JSONObject();
        try {
            object.put("firstName", firstName.getText().toString());
            object.put("lastName", lastName.getText().toString());
            object.put("documentType", TYDS);
            object.put("ssn", documentNb.getText().toString());
            object.put("nationality", NS);
            object.put("officeId", office.getId());
            object.put("guardId", guardID);
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

    private void getOffices(String gmail) {
        final TextView textView = findViewById(R.id.testJSON);
        String ip = getString(R.string.IP);
        String urlGetOffice = "http://" + ip + "/api/office/getOffices/" + gmail;

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(urlGetOffice)
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

    private void stringOfficeArray(JSONArray data) throws JSONException {
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
        gson = new Gson();

        String jsonOffices = gson.toJson(offices);

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("Offices", jsonOffices);
        editor.apply();

        ArrayAdapter<Office> destinationAdapter = new ArrayAdapter<Office>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                offices
        );
        officesSpinner.setAdapter(destinationAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, PINCodeActivity.class);
        intent.putExtra("activityName", "ManualEntry");
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkVisitor(final String ssn, final JSONObject jsonObjectIfFound) {

        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/visitor/check";

//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ssn", ssn)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                                        boolean found = jsonObject.getBoolean("found");
                                        String msg = jsonObject.getString("message");
                                        if (found) {
                                            TextView banner = findViewById(R.id.visitor_response);
                                            banner.setText(msg);
                                            banner.setBackgroundColor(getResources().getColor(R.color.colorVisitorFound));
                                            final JSONObject visitor = jsonObject.getJSONObject("visitor");

                                            initializeForm();
                                            setForm(visitor);
                                            changeBtn();

                                            officesSpinner = findViewById(R.id.destination);

                                            saveBtn = findViewById(R.id.save_btn);
                                            saveBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    try {
                                                        Office office = (Office) officesSpinner.getSelectedItem();
                                                        saveLog(visitor, office);
                                                    } catch (JSONException e) {
                                                        Toast.makeText(ManualEntry.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                        } else {
                                            TextView banner = findViewById(R.id.visitor_response);
                                            banner.setText(msg);
                                            banner.setBackgroundColor(RED);

                                            if (jsonObjectIfFound == null) {
                                                documentNb = findViewById(R.id.document_number);
                                                documentNb.setText(ssn);
                                            } else {
                                                initializeForm();
                                                String type = jsonObjectIfFound.getString("type");
                                                if (type.equals("new")) {

                                                    String firstNameS = jsonObjectIfFound.getString("firstName");
                                                    String lastNameS = jsonObjectIfFound.getString("lastName");

                                                    firstName.setText(firstNameS);
                                                    lastName.setText(lastNameS);
                                                    documentNb.setText(ssn);

                                                } else {
                                                    documentNb.setText(ssn);
                                                }
                                            }
                                        }
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

    private void initializeForm() {
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nationalitySpinner = findViewById(R.id.nationality);
        typeOfDocumentSpinner = findViewById(R.id.document_type);
        documentNb = findViewById(R.id.document_number);
    }

    private void setForm(JSONObject visitor) throws JSONException {
        firstName.setText(visitor.getString("firstName"));
        lastName.setText(visitor.getString("lastName"));
        documentNb.setText(visitor.getString("ssn"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManualEntry.this, R.array.document_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfDocumentSpinner.setAdapter(adapter);
        if (visitor.getString("typeOfDocument") != null) {
            int spinnerPosition = adapter.getPosition(visitor.getString("typeOfDocument"));
            typeOfDocumentSpinner.setSelection(spinnerPosition);
        }

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

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(ManualEntry.this, android.R.layout.simple_spinner_item,
                countries);
        nationalitySpinner.setAdapter(nationalityAdapter);
        if (visitor.getString("nationality") != null) {
            int spinnerPosition = nationalityAdapter.getPosition(visitor.getString("nationality"));
            nationalitySpinner.setSelection(spinnerPosition);
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Enter SSN");


        // Set up the input
        final EditText input = new EditText(this);


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                checkVisitor(m_Text, null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setAdapters() {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        countries.add("Choose Nationality");
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

        nationalitySpinner.setSelection(46);

        typeOfDocumentSpinner = findViewById(R.id.document_type);
        ArrayAdapter<CharSequence> documentTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.document_type, android.R.layout.simple_spinner_item);
        documentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfDocumentSpinner.setAdapter(documentTypeAdapter);

    }


    private void changeBtn() {
        addBtn = findViewById(R.id.add_btn);
        saveBtn = findViewById(R.id.save_btn);

        addBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refreshbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveLog(JSONObject visitor, Office office) throws JSONException {

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int guardID = (shared.getInt("Guard ID", 0));

        String ip = getString(R.string.IP);
        String url = "http://" + ip + "/api/visitor/add/log/" + guardID;

        final OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("visitorId", Integer.toString(visitor.getInt("id")))
                .addFormDataPart("officeId", Integer.toString(office.getId()))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}