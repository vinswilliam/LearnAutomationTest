package com.siuli.andr.whitebird.detailNote;

import com.siuli.andr.whitebird.data.Note;

/**
 * Created by william on 1/12/2016.
 */
public interface INotePresenter {
    void displayNote(Note note);
    void getNote(long noteId);
    void deleteNote(long noteId);
    void updateNote(long noteId);
}
