package com.idreaderinternship;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BarcodeUISettings;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanID extends Activity {
    private static final int MY_REQUEST_CODE = 1337;

    /**
     * Barcode recognizer that will perform recognition of images
     */
    private BarcodeRecognizer mBarcodeRecognizer;

    /**
     * Recognizer bundle that will wrap the barcode recognizer in order for recognition to be performed
     */
    private RecognizerBundle mRecognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_id);

        // You have to enable recognizers and barcode types you want to support
        // Don't enable what you don't need, it will significantly decrease scanning performance
        mBarcodeRecognizer = new BarcodeRecognizer();
        mBarcodeRecognizer.setScanPDF417(true);
        mBarcodeRecognizer.setScanQRCode(true);

        mRecognizerBundle = new RecognizerBundle(mBarcodeRecognizer);

        // start default barcode scanning activity
        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        uiSettings.setBeepSoundResourceID(R.raw.beep);
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, uiSettings);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, PINCodeActivity.class);
        intent.putExtra("activityName", "ScanID");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                handleScanResultIntent(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleScanResultIntent(Intent data) throws JSONException {
        // updates bundled recognizers with results that have arrived
        mRecognizerBundle.loadFromIntent(data);
        // after calling mRecognizerBundle.loadFromIntent, results are stored in mBarcodeRecognizer
        BarcodeRecognizer.Result result = mBarcodeRecognizer.getResult();

        shareScanResult(result);
    }


    private void shareScanResult(BarcodeRecognizer.Result result) throws JSONException {

        String x = getResult(result.getRawData());

        Intent i = new Intent();
        i.setAction("com.idreaderinternship");
        i.setType("text/plain");
        i.putExtra("KEY_T", x);
        startActivity(i);
        finish();

    }

    private String getResult(byte[] rawData) throws JSONException {
        String data = new String(rawData);
        String[] splitData = data.split(",");

        JSONObject jsonObject = new JSONObject();


        if (splitData[0].length() > 2) {
            jsonObject.put("type", "old");
            jsonObject.put("ssn", splitData[0]);
        } else {
            if (splitData[0].equals("2")) {
                jsonObject.put("type", "middle");
                jsonObject.put("ssn", splitData[1]);
            } else {
                jsonObject.put("type", "new");
                jsonObject.put("ssn", splitData[1]);
                jsonObject.put("firstName", splitData[6]);
                jsonObject.put("lastName", splitData[7]);
            }
        }

        return jsonObject.toString();
    }
}
