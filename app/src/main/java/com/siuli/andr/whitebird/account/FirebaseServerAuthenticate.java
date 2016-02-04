package com.siuli.andr.whitebird.account;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by william on 2/4/2016.
 */
public class FirebaseServerAuthenticate implements ServerAuthenticate {

    private Firebase firebaseRef;

    public FirebaseServerAuthenticate(){
        firebaseRef = new Firebase("https://luminous-heat-594.firebaseio.com/");
    }

    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        return null;
    }

    @Override
    public String userSignIn(String user, String pass, final String authType) throws Exception {

        firebaseRef.authWithPassword(user, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("siuli", "token > " + authData.getToken());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.d("siuli", "error > " + firebaseError.getMessage());
            }
        });
        return null;
    }

    @Override
    public String userSignIn(String user, String pass, String authType, final FirebaseListener tokenListener){
        firebaseRef.authWithPassword(user, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("siuli", "token > " + authData.getToken());
                tokenListener.authTokenListener(authData.getToken());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.d("siuli", "error > " + firebaseError.getMessage());
                tokenListener.errorTokenListener(firebaseError.getMessage());
            }
        });
        return null;
    }

}
