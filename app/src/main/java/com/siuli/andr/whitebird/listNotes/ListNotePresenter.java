package com.siuli.andr.whitebird.listNotes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

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

    public ListNotePresenter(ListNoteView view, Context ctx){
        mView = view;
        mCtx = ctx;
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
