package com.siuli.andr.whitebird.detailNote;

import com.siuli.andr.whitebird.data.Note;

/**
 * Created by william on 1/12/2016.
 */
public interface INoteView {
    void showNote(Note note);
    void deleteNote();
    void editNote(long noteId);
    void finishAct();
}
