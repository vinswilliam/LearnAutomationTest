package com.siuli.andr.whitebird.addNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.data.NoteDBSchema;

import java.util.List;
import java.util.UUID;

/**
 * Created by william on 1/13/2016.
 */
public class AddNotePresenter implements IAddNotePresenter {

    IAddNoteView mView;
    Context mCtx;
    Note mNote;

    public AddNotePresenter(Context ctx, IAddNoteView view, long mNoteId){
        mCtx = ctx;
        mView = view;

        if(mNoteId > 0) {

            Uri uri = Uri.withAppendedPath(NoteContract.Notes.CONTENT_URI, String.valueOf(mNoteId));
            Cursor cursor = mCtx.getContentResolver().query(uri, NoteContract.Notes.PROJECTION_ALL, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                mNote = new Note(cursor);

                mView.init(mNote);
                mView.setDefaultDateTime(mNote.getNoteDate());
                cursor.close();
            }
        } else {
            mView.setDefaultDateTime(0);
        }
    }

    @Override
    public void insertNote(List<String> values) {
        ContentValues contentValues = new ContentValues();


        long noteId = Long.valueOf(values.get(0));
        Uri uri = noteId > 0 ? Uri.withAppendedPath(NoteContract.Notes.CONTENT_URI, values.get(0))
                : NoteContract.Notes.CONTENT_URI;

        Log.d("cpsample", "noteId: " + noteId);

        //update
        if (noteId > 0) {
            contentValues.put(NoteDBSchema.COL_ID, values.get(0));
            contentValues.put(NoteDBSchema.COL_TITLE,values.get(1));
            contentValues.put(NoteDBSchema.COL_DESCRIPTION, values.get(2));
            contentValues.put(NoteDBSchema.COL_NOTE_CREATED, values.get(3));
            contentValues.put(NoteDBSchema.COL_NOTE_DATE, values.get(4));
            contentValues.put(NoteDBSchema.COL_STATUS, values.get(5));
            int updateCount = mCtx.getContentResolver().update(uri, contentValues, null, null);

            if(updateCount > 0) {
                mView.backToDetailNote(null);
            } else {
                //show error on update
                mView.showUpdateFailed();
            }
        }
        // insert
        else {
//            contentValues.put(NoteDBSchema.COL_ID, values.get(0));
            contentValues.put(NoteDBSchema.COL_TITLE,values.get(1));
            contentValues.put(NoteDBSchema.COL_DESCRIPTION, values.get(2));
            contentValues.put(NoteDBSchema.COL_NOTE_CREATED, values.get(3));
            contentValues.put(NoteDBSchema.COL_NOTE_DATE, values.get(4));
            contentValues.put(NoteDBSchema.COL_STATUS, values.get(5));
            contentValues.put(NoteDBSchema.COL_NOTE_ID, UUID.randomUUID().toString());
            Uri newUri = mCtx.getContentResolver().insert(uri, contentValues);
            mView.backToDetailNote(newUri);
        }

    }

    @Override
    public void updateNote(List<String> values) {

    }


}
