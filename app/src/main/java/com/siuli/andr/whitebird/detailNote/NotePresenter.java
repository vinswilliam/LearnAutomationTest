package com.siuli.andr.whitebird.detailNote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.addNote.AddNoteView;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.listNotes.ListNoteView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by william on 1/13/2016.
 */
public class NotePresenter implements INotePresenter {

    INoteView mView;
    Note mNote;
    Context mCtx;

    public NotePresenter(Context ctx, INoteView view, long noteId){
        mView = view;
        mCtx = ctx;
        getNote(noteId);
    }

    @Override
    public void displayNote(Note note) {

    }

    @Override
    public void getNote(long noteId) {
        Uri uri = Uri.withAppendedPath(NoteContract.Notes.CONTENT_URI, String.valueOf(noteId));
        Cursor cursor = mCtx.getContentResolver().query(uri, NoteContract.Notes.PROJECTION_ALL, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            mNote = new Note(cursor);
            mView.showNote(mNote);
        }
    }

    @Override
    public void deleteNote(long noteId) {
        Uri uri = Uri.withAppendedPath(NoteContract.Notes.CONTENT_URI, String.valueOf(noteId));
        int delResult = mCtx.getContentResolver().delete(uri, null, null);
        if(delResult > 0) {
            mView.finishAct();
        } else {
            Toast.makeText(mCtx, "Delete failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateNote(long noteId) {
        Intent addNoteIntent = new Intent(mCtx, AddNoteView.class);
        addNoteIntent.putExtra("noteId", noteId);
        mCtx.startActivity(addNoteIntent);
        mView.finishAct();
    }
}
