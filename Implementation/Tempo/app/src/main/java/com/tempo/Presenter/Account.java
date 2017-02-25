package com.tempo.Presenter;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by bkell on 2/22/2017.
 */

public class Account {

    private static Account data;

    GoogleAccountCredential googleCred;

    public static synchronized Account getInstance() {
        if (data != null) {
            return data;
        }
        data = new Account();
        return data;
    }

}
