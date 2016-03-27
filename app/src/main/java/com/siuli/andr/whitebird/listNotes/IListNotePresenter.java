package com.siuli.andr.whitebird.listNotes;

import android.view.View;

import com.siuli.andr.whitebird.data.Note;

/**
 * Created by william on 1/11/2016.
 */
public interface IListNotePresenter {

    void getNotes();
    void syncNotes();
    void refreshSyncStatus();
    void createNote();
    void moveToNoteDetail(View view, int position);
    void updateNote(long id, Note note);
    void deleteNote(Note note);

}
