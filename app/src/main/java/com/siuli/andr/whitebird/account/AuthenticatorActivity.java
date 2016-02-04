package com.siuli.andr.whitebird.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.siuli.andr.whitebird.R;

/**
 * Created by william on 1/28/2016.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.act_login);

        //setup firebase
        Firebase.setAndroidContext(this);

        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if(mAuthTokenType == null){
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
        }

        if(accountName != null){
            ((TextView) findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getBaseContext(), SignUpActivity.class);
                signUp.putExtras(getIntent().getExtras());
                startActivityForResult(signUp, REQ_SIGNUP);
            }
        });
    }

    public void submit(){
        final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        AccountGeneral.sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType, new ServerAuthenticate.FirebaseListener() {

            //String authToken = null;
            Bundle data = new Bundle();

            @Override
            public void authTokenListener(String token) {
                data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                data.putString(AccountManager.KEY_AUTHTOKEN, token);
                data.putString(PARAM_USER_PASS, userPass);

                final Intent res = new Intent();
                res.putExtras(data);

                finishLogin(res);
            }

            @Override
            public void errorTokenListener(String errorMessage) {

                //data.putString(KEY_ERROR_MESSAGE, errorMessage);
                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


//        new AsyncTask<String, Void, Intent>(){
//            @Override
//            protected Intent doInBackground(String... params) {
//                Log.d("siuli", TAG + "> Started authenticating");
//
//                String authToken = null;
//                Bundle data = new Bundle();
//
//                try{
//                    authToken = AccountGeneral.sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);
//
//                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
//                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//                    data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
//                    data.putString(PARAM_USER_PASS, userPass);
//                } catch (Exception e){
//                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
//                }
//                final Intent res = new Intent();
//                res.putExtras(data);
//                return res;
//            }
//
//            @Override
//            protected void onPostExecute(Intent intent) {
//                if(intent.hasExtra(KEY_ERROR_MESSAGE)){
//                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
//                } else {
//                    finishLogin(intent);
//                }
//            }
//        }.execute();
    }

    private void finishLogin(Intent intent){
        Log.d("siuli", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if(getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)){
            Log.d("siuli", TAG + "> finishLogin > addAccountExplicitly");
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = mAuthTokenType;

            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            Log.d("siuli", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
