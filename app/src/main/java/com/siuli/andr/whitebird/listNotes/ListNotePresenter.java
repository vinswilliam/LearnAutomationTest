package com.siuli.andr.whitebird.listNotes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.data.NoteDBSchema;
import com.siuli.andr.whitebird.data.StatusItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by william on 1/11/2016.
 */
public class ListNotePresenter implements IListNotePresenter {

    private IListNoteView mView;
    private List<Note> mListNote;
    private Context mCtx;
    private AccountManager mAccountManager;
    private Account mConnectedAccount;

    public ListNotePresenter(ListNoteView view, Context ctx){
        mView = view;
        mCtx = ctx;
        mAccountManager = AccountManager.get(ctx);
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
    }

    @Override
    public void getNotes() {
        mListNote = new ArrayList<>();
        ContentResolver contentResolver = mCtx.getContentResolver();
        Cursor cursor = contentResolver.query(NoteContract.Notes.CONTENT_URI, NoteContract.Notes.PROJECTION_ALL, null, null, NoteContract.Notes.SORT_ORDER_DEFAULT);

        if (cursor != null && cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                Note note = new Note(cursor);

                mListNote.add(note);

                cursor.moveToNext();
            }
            cursor.close();
            mView.showNotes(mListNote);
        }
    }

    @Override
    public void syncNotes() {
        if(mConnectedAccount == null){
            Toast.makeText(mCtx, "Please connect first", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("siuli", "syncNotes");
        //do something
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // performing a sync no matter if it's off
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // performing a sync no matter if it's off
        ContentResolver.requestSync(mConnectedAccount, NoteContract.AUTHORITY, bundle);
    }

    @Override
    public void refreshSyncStatus(){
        String status;

        if(ContentResolver.isSyncActive(mConnectedAccount, NoteContract.AUTHORITY)) {
            mView.showIndicator();
            status = "Status: Syncing..";
        } else if (ContentResolver.isSyncPending(mConnectedAccount, NoteContract.AUTHORITY)) {
            mView.hideIndicator();
            status = "Status: Pending..";
        } else {
            mView.hideIndicator();
            status = "Status: idle";
        }
        Log.d("siuli", "status > " + status);
    }

    String authToken;
    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, (Activity) mCtx, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd;
                        try {
                            bnd = future.getResult();
                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            if (authToken != null) {
                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
                            }
                            Log.d("siuli", ((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , null);
    }


    @Override
    public void createNote() {
        ContentValues values = new ContentValues();

        Random randVal = new Random();
        int randInt = randVal.nextInt(1000);

        values.put(NoteDBSchema.COL_TITLE, "title" + randInt);
        values.put(NoteDBSchema.COL_DESCRIPTION, "description" + randInt);

        Calendar currentTime = Calendar.getInstance();

        values.put(NoteDBSchema.COL_NOTE_CREATED, currentTime.getTimeInMillis());
        values.put(NoteDBSchema.COL_NOTE_DATE, currentTime.getTimeInMillis());

        values.put(NoteDBSchema.COL_STATUS, StatusItem.FLAG_ACTIVE);

        ContentResolver resolver = mCtx.getContentResolver();
        Uri uriInsert = resolver.insert(NoteContract.Notes.CONTENT_URI, values);

        Cursor cursor = resolver.query(uriInsert, NoteContract.Notes.PROJECTION_ALL, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            Note note = new Note(cursor);
            cursor.close();
            mView.addNote(note);
        }
    }

    @Override
    public void moveToNoteDetail(View view, int position) {
        mView.detailNote(mListNote.get(position));
    }

    @Override
    public void updateNote(long id, Note note) {

    }

    @Override
    public void deleteNote(Note note) {

    }
}
