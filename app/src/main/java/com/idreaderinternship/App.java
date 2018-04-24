package com.idreaderinternship;

import android.app.Application;

import com.microblink.MicroblinkSDK;
import com.microblink.intent.IntentDataTransferMode;

/**
 * Created by Merhi on 4/9/2018.
 */

public class App extends Application {
    private static boolean sFirstRun = false;

    public static boolean fetchFirstRun() {
        boolean old = sFirstRun;
        sFirstRun = false;
        return old;
    }

    //--called when app process is created--
    @Override
    public void onCreate() {
        super.onCreate();

        // obtain your licence at http://microblink.com/login or
        // contact us at http://help.microblink.com
        MicroblinkSDK.setLicenseFile("MB_com.idreaderinternship_Pdf417Mobi_Android_2018-05-12.mblic", this);

        // use optimised way for transferring RecognizerBundle between activities, while ensuring
        // data does not get lost when Android restarts the scanning activity
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);

        sFirstRun = true;
    }

}
