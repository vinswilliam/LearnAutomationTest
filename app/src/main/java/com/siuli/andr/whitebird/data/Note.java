package com.siuli.andr.whitebird.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.siuli.andr.whitebird.utilities.Util;

/**
 * Created by william on 1/11/2016.
 */
public class Note implements Parcelable{

    private long id;
    private String title;
    private String description;
    private long noteDate;
    private long createdDate;
    private short status;

    public Note() {

    }

    public Note(Cursor cursor){
        id = (cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_ID)));
        title = (cursor.getString(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_TITLE)));
        description = (cursor.getString(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_DESCRIPTION)));
        createdDate = (cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_NOTE_CREATED)));
        noteDate = (cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_NOTE_DATE)));
        status = (cursor.getShort(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_STATUS)));
    }

    protected Note(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        noteDate = in.readLong();
        createdDate = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteDateString(){
        return Util.parsingLongDateTime(getNoteDate(), "dd/MM/yyyy hh:mm");
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    private Note parseCursorToNote(Cursor cursor){
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_DESCRIPTION)));
        note.setCreatedDate(cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_NOTE_CREATED)));
        note.setNoteDate(cursor.getLong(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_NOTE_CREATED)));
        note.setStatus(cursor.getShort(cursor.getColumnIndexOrThrow(NoteDBSchema.COL_STATUS)));
        return note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(noteDate);
        dest.writeLong(createdDate);
    }
}
