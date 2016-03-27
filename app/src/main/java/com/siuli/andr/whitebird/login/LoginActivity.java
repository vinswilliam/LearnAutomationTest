package com.siuli.andr.whitebird.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.listNotes.ListNoteView;

import java.io.IOException;

/**
 * Created by william on 1/28/2016.
 */
public class LoginActivity extends Activity {

    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_INVALIDATE = "state_invalidate";

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    private AlertDialog mAlertDialog;
    private boolean mInvalidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account1);
        mAccountManager = AccountManager.get(this);

        findViewById(R.id.btnAddAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
            }
        });

        findViewById(R.id.btnGetAuthToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false);
            }
        });

        findViewById(R.id.btnGetAuthTokenConvenient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
            }
        });

        findViewById(R.id.btnInvalidateAuthToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            }
        });

        if(savedInstanceState != null){
            boolean showDialog = savedInstanceState.getBoolean(STATE_DIALOG);
            boolean invalidate = savedInstanceState.getBoolean(STATE_INVALIDATE);
            if(showDialog){
                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, invalidate);
            }
        }

        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mAlertDialog != null && mAlertDialog.isShowing()){
            outState.putBoolean(STATE_DIALOG, true);
            outState.putBoolean(STATE_INVALIDATE, mInvalidate);
        }
    }

    private void addNewAccount(String accountType, String authTokenType){
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");
                    Log.d("siuli", "AddNewAccount Bundle is " + bnd);
                    moveToListNote();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }

    private void showAccountPicker(final String authTokenType, final boolean invalidate){
        mInvalidate = invalidate;
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        if(availableAccounts.length == 0){
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
        } else {
            String name[] = new String[availableAccounts.length];
            for (int i = 0; i < availableAccounts.length; i++) {
                name[i] = availableAccounts[i].name;
            }

            mAlertDialog = new AlertDialog.Builder(this).setTitle("Pick Account")
                    .setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(invalidate)
                        invalidateAuthToken(availableAccounts[which], authTokenType);
                    else
                        getExistingAccountAuthToken(availableAccounts[which], authTokenType);
                }
            }).create();
            mAlertDialog.show();
        }
    }

    private void getExistingAccountAuthToken(Account account, String authTokenType){
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    showMessage((authToken != null) ? "SUCCESS!\ntoken: " + authToken: "FAIL");
                } catch (Exception e){
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }

    private void invalidateAuthToken(final Account account, String authTokenType){
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    showMessage(account.name + " invalidated");
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void moveToListNote(){
        Intent intent = new Intent(getApplicationContext(), ListNoteView.class);
        startActivity(intent);
        finish();
    }

    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType){
        final AccountManagerFuture<Bundle> future = mAccountManager
                .getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                        new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                Bundle bnd = null;
                                try {
                                    bnd = future.getResult();
                                    final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                                    showMessage(((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
                                    Log.d("siuli", "GetTokenForAccount Bundle is " + bnd);
                                    moveToListNote();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showMessage(e.getMessage());
                                }
                            }
                        }, null);
    }

    private void showMessage(final String msg){
        if(TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
