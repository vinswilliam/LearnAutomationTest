package com.siuli.andr.whitebird.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by william on 1/11/2016.
 */
public class NoteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = NoteDBSchema.DB_NAME;

    public NoteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteDBSchema.DDL_CREATE_TBL_NOTES);
        db.execSQL(NoteDBSchema.DDL_CREATE_TBL_PHOTOS);
        db.execSQL(NoteDBSchema.DDL_CREATE_TRIGGER_DEL_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not best practice to create and drop data from user

        db.execSQL(NoteDBSchema.DDL_DROP_TBL_NOTES);
        db.execSQL(NoteDBSchema.DDL_DROP_TBL_PHOTOS);
        db.execSQL(NoteDBSchema.DDL_DROP_TRIGGER_DEL_NOTE);
        onCreate(db);
    }
}
