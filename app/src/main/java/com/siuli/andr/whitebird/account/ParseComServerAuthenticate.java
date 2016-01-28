package com.siuli.andr.whitebird.account;

/**
 * Created by william on 1/27/2016.
 */
public class ParseComServerAuthenticate implements ServerAuthenticate {

    private static final String FAKE_TOKEN = "abcde12345";

    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        return FAKE_TOKEN;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        return FAKE_TOKEN;
    }
}
