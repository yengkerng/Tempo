package com.tempo.presenter;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by bkell on 2/22/2017.
 */

class Account {

    private static Account data;

    GoogleAccountCredential googleCred;
    String userEmail;

    static synchronized Account getInstance() {
        if (data != null) {
            return data;
        }
        data = new Account();
        return data;
    }

}
