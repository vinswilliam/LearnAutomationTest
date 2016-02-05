package com.siuli.andr.whitebird.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by william on 1/11/2016.
 */
public final class NoteContract {

    public static final String AUTHORITY = "com.siuli.andr.whitebird";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String SELECTION_ID_BASED = BaseColumns._ID + " = ?";

    public static final class Notes implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(NoteContract.CONTENT_URI, "notes");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.siuli.andr.whitebird.notes";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.siuli.andr.whitebird.notes";

        public static final String[] PROJECTION_ALL = new String[]{
                NoteDBSchema.COL_ID,
                NoteDBSchema.COL_NOTE_ID,
                NoteDBSchema.COL_TITLE,
                NoteDBSchema.COL_DESCRIPTION,
                NoteDBSchema.COL_NOTE_DATE,
                NoteDBSchema.COL_NOTE_CREATED,
                NoteDBSchema.COL_STATUS
        };

        public static final String SORT_ORDER_DEFAULT = NoteDBSchema.COL_NOTE_DATE  + " DESC";
    }

    public static final class Photos implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(NoteContract.CONTENT_URI, "photos");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/photos";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/photos";

        public static final String[] PROJECTION_ALL = new String[]{
                NoteDBSchema.COL_ID,
                NoteDBSchema.COL_URI,
                NoteDBSchema.COL_NOTE
        };

        public static final String SORT_ORDER_DEFAULT = NoteDBSchema.COL_ID + " DESC";

    }
}
