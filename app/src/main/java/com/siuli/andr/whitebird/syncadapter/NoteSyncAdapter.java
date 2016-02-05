package com.siuli.andr.whitebird.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by william on 2/4/2016.
 */
public class NoteSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";

    private final AccountManager mAccountManager;
    private Context mContext;

    public NoteSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, final ContentProviderClient provider, SyncResult syncResult) {

        StringBuilder sb = new StringBuilder();
        if(extras != null){
            for(String key : extras.keySet()){
                sb.append(key + "[" + extras.get(key) + "] ");
            }
        }

        Log.d("siuli", TAG + " > " + "onPerformSync for account[" + account.name + "]. Extras " + sb.toString());

        // TODO create your own logic to sync data between local and remote database

        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            String userObjectId = mAccountManager.getUserData(account, AccountGeneral.USERDATA_USER_OBJ_ID);

            Log.d("siuli", "onSync > " + authToken);

            String secretKey = "NgledzEXdPvNJaRHwZSHIOfgLoWJFeFTrtLuPheZ";
            String URL = "https://luminous-heat-594.firebaseio.com/note.json?auth=" + secretKey;

            //get local item
            ArrayList<Note> localNotes = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = provider.query(NoteContract.Notes.CONTENT_URI, NoteContract.Notes.PROJECTION_ALL, null, null, NoteContract.Notes.SORT_ORDER_DEFAULT);

                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Note note = new Note(cursor);
                        localNotes.add(note);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }

            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            //get from server
            Ion.with(mContext)
            .load(URL)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {

                    if(result != null) {
                        Log.d("siuli", TAG + " > " + result.toString());

                    }
                }
            });

        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }

    }
}
