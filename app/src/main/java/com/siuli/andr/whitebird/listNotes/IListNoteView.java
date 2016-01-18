package com.siuli.andr.whitebird.listNotes;

import com.siuli.andr.whitebird.data.Note;

import java.util.List;

/**
 * Created by william on 1/11/2016.
 */
public interface IListNoteView {

    void showIndicator();

    void hideIndicator();

    void refresh();

    void addNote(Note note);

    void addNote();

    void showNotes(List<Note> listNote);

    void detailNote(Note note);
}
