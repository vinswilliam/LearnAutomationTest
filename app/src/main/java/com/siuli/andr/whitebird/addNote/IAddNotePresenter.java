package com.siuli.andr.whitebird.addNote;

import java.util.List;

/**
 * Created by william on 1/13/2016.
 */
public interface IAddNotePresenter {

    void insertNote(List<String> values);

    void updateNote(List<String> values);

}
