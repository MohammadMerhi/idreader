package com.example.merhi.idreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ManualEntry extends AppCompatActivity {
    Spinner nationalitySpinner, typeOfDocumentSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        nationalitySpinner = (Spinner) findViewById(R.id.nationality);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> nationalityAdapter = ArrayAdapter.createFromResource(this,
                R.array.nationality, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        nationalitySpinner.setAdapter(nationalityAdapter);

        typeOfDocumentSpinner = (Spinner) findViewById(R.id.document_type);
        ArrayAdapter<CharSequence> documentTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.document_type, android.R.layout.simple_spinner_item);
        documentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfDocumentSpinner.setAdapter(documentTypeAdapter);

    }
}
