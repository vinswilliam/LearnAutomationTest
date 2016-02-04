package com.siuli.andr.whitebird.account;

/**
 * Created by william on 1/27/2016.
 */
public interface ServerAuthenticate {

    public String userSignUp(final String name, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;

    public String userSignIn(final String user, final String pass, String authType, FirebaseListener tokenListener);

    public interface FirebaseListener {
        public void authTokenListener(String token);
        public void errorTokenListener(String errorMessage);
    }
}
