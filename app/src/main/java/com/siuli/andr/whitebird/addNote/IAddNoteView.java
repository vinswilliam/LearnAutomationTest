package com.siuli.andr.whitebird.addNote;

import android.net.Uri;

import com.siuli.andr.whitebird.data.Note;

/**
 * Created by william on 1/13/2016.
 */
public interface IAddNoteView {

    void backToDetailNote(Uri uri);

    void showUpdateFailed();

    void setDefaultDateTime(long dateTimeInMillis);

    void backToListNote();

    void displayDatePicker();

    void reset();

    void saveNote();

    void init(Note note);
}
