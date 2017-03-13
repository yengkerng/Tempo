package com.tempo.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseLoginActivity extends Activity {

    private static final int RC_SIGN_IN = 9001;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        signInToGoogle();

    }

    private void signInToGoogle() {

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("759612061162-ia80btk9jn7fv7rk8j36fil2sdd6hk53.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        });

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                new FirebaseLoginTask(account).execute();
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }

    }

    private class FirebaseLoginTask extends AsyncTask<Void, Void, Void> {

        GoogleSignInAccount account;

        FirebaseLoginTask(GoogleSignInAccount account) {
            this.account = account;
        }

        @Override
        protected Void doInBackground(Void... params) {

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

            auth.signInWithCredential(credential)
                    .addOnCompleteListener(FirebaseLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Intent calendarIntent = new Intent(FirebaseLoginActivity.this, MyCalendarActivity.class);
                                Bundle userInfo = new Bundle();
                                userInfo.putString("userEmail", account.getEmail());
                                userInfo.putString("username", account.getDisplayName());
                                calendarIntent.putExtras(userInfo);
                                /*if (account.getDisplayName() != null && account.getEmail() != null) {
                                    calendarIntent.putExtra("userEmail", account.getEmail());
                                    calendarIntent.putExtra("username", account.getDisplayName());
                                }*/
                                startActivity(calendarIntent);
                            }
                            else {
                                task.getException().printStackTrace();
                                /* You should probably report this. */
                            }

                        }
                    });

            return null;

        }

    }

}
