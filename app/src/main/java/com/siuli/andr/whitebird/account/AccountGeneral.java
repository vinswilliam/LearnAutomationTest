package com.siuli.andr.whitebird.account;

/**
 * Created by william on 1/27/2016.
 */
public class AccountGeneral {

    public static final String ACCOUNT_TYPE = "com.siuli.auth";

    public static final String ACCOUNT_NAME = "Siuli";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to a Siuli account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to a  Siuli account";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();
}
