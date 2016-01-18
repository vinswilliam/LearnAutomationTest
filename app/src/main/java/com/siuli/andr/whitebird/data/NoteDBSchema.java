package com.siuli.andr.whitebird.data;

import android.provider.BaseColumns;

/**
 * Created by william on 1/11/2016.
 */
public interface NoteDBSchema {

    String DB_NAME = "whitebird.db";

    String TBL_NOTES = "notes";
    String TBL_PHOTOS = "photos";

    String COL_ID = BaseColumns._ID;
    String COL_TITLE = "title";
    String COL_DESCRIPTION = "description";
    String COL_NOTE_DATE = "note_date";
    String COL_NOTE_CREATED = "note_created";
    String COL_STATUS = "status";

    String DDL_CREATE_TBL_NOTES =
            "CREATE TABLE " + TBL_NOTES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_NOTE_DATE + " LONG, " +
                COL_NOTE_CREATED + " LONG, " +
                COL_STATUS + " LONG" +
                ")";

    String COL_URI = "uri";
    String COL_NOTE = "note";

    String DDL_CREATE_TBL_PHOTOS =
            "CREATE TABLE " + TBL_PHOTOS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_URI + " TEXT, " +
                    COL_NOTE + " TEXT " +
                    ")";

    String DDL_CREATE_TRIGGER_DEL_NOTE =
            "CREATE TRIGGER delete_notes DELETE ON " + TBL_NOTES + " " +
                    "BEGIN " +
                    "DELETE FROM " + TBL_PHOTOS + " WHERE " + COL_NOTE + " = " + "old." + COL_ID + ";" +
                    "end";

    String DDL_DROP_TBL_NOTES =
            "DROP TABLE IF EXISTS " + TBL_NOTES;

    String DDL_DROP_TBL_PHOTOS =
            "DROP TABLE IF EXISTS " + TBL_PHOTOS;

    String DDL_DROP_TRIGGER_DEL_NOTE =
            "DROP TRIGGER IF EXISTS delete_notes";
}
