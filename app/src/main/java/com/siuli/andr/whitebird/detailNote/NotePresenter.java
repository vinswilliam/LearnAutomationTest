package com.siuli.andr.whitebird.detailNote;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;

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

    }

    @Override
    public void updateNote(long noteId) {

    }
}
