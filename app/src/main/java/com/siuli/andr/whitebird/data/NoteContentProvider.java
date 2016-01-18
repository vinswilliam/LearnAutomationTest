package com.siuli.andr.whitebird.data;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.siuli.andr.whitebird.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 1/11/2016.
 */
public class NoteContentProvider extends ContentProvider {

    //helper constant for UriMatcher
    private static final int NOTE_LIST = 1;
    private static final int NOTE_ID = 2;
    private static final int PHOTO_LIST = 11;
    private static final int PHOTO_ID = 12;

    private static final UriMatcher URI_MATCHER;

    private NoteOpenHelper mHelper = null;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<>();

    //prepare UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(NoteContract.AUTHORITY, "notes", NOTE_LIST);
        URI_MATCHER.addURI(NoteContract.AUTHORITY, "notes/#", NOTE_ID);
        URI_MATCHER.addURI(NoteContract.AUTHORITY, "photos", PHOTO_LIST);
        URI_MATCHER.addURI(NoteContract.AUTHORITY, "photos/#", PHOTO_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new NoteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)){
            case NOTE_LIST:
                builder.setTables(NoteDBSchema.TBL_NOTES);
                if(TextUtils.isEmpty(sortOrder)){
                    sortOrder = NoteContract.Notes.SORT_ORDER_DEFAULT;
                }
                break;
            case NOTE_ID:
                builder.setTables(NoteDBSchema.TBL_NOTES);
                builder.appendWhere(NoteContract.Notes._ID + " = " + uri.getLastPathSegment());
                break;
            case PHOTO_LIST:
                builder.setTables(NoteDBSchema.TBL_PHOTOS);
                if(TextUtils.isEmpty(sortOrder)){
                    sortOrder = NoteContract.Photos.SORT_ORDER_DEFAULT;
                }
                break;
            case PHOTO_ID:
                builder.setTables(NoteDBSchema.TBL_PHOTOS);
                builder.appendWhere(NoteContract.Photos._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            logQuery(builder, projection, selection, sortOrder);
        } else {
            logQueryDeprecated(builder, projection, selection, sortOrder);
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void logQuery(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder){
        if(BuildConfig.DEBUG){
            Log.v("cpsample", "query: " + builder.buildQuery(projection, selection, null, null, sortOrder, null));
        }
    }

    @SuppressWarnings("deprecation")
    private void logQueryDeprecated(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder){
        if(BuildConfig.DEBUG){
            Log.v("cpsample", "query: " + builder.buildQuery(projection, selection, null, null, null, sortOrder, null));
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case NOTE_LIST:
                return NoteContract.Notes.CONTENT_TYPE;
            case NOTE_ID:
                return NoteContract.Notes.CONTENT_ITEM_TYPE;
            case PHOTO_LIST:
                return NoteContract.Photos.CONTENT_TYPE;
            case PHOTO_ID:
                return NoteContract.Photos.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        doAnalytics(uri, "insert");
        if(URI_MATCHER.match(uri) != NOTE_LIST
                && URI_MATCHER.match(uri) != PHOTO_LIST){
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(URI_MATCHER.match(uri) == NOTE_LIST){
            long id = db.insert(NoteDBSchema.TBL_NOTES, null, values);
            Log.d("cpsample", "new id: " + id);
            return getUriForId(id, uri);
        } else {
            long id = db.insertWithOnConflict(NoteDBSchema.TBL_PHOTOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return getUriForId(id, uri);
        }
    }

    private Uri getUriForId(long id, Uri uri) {
        if(id > 0){
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if(!isInBatchMode()){
                getContext().getContentResolver().notifyChange(itemUri, null);
            }

            Log.d("samplecp", "uriinsert: " + itemUri);
            return itemUri;
        } else {
            throw new SQLException("Problem while inserting into uri: " + uri);
        }
    }


    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        mIsInBatchMode.set(true);
        db.beginTransaction();
        try{
            final ContentProviderResult[] retResult =  super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(NoteContract.CONTENT_URI, null);
            return retResult;
        } finally {
            mIsInBatchMode.remove();
            db.endTransaction();
        }
    }

    private boolean isInBatchMode(){
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }


    private void doAnalytics(Uri uri, String event){
        if(BuildConfig.DEBUG){
            Log.v("cpsample", event + " -> " + uri);
            Log.v("cpsample", "caller: " +  detectCaller());
        }
    }

    private String detectCaller(){
        int pid = Binder.getCallingPid();
        return getProcessNameFromPid(pid);
    }

    private String getProcessNameFromPid(int givenPid){
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lstAppInfo = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo ai : lstAppInfo){
            if(ai.pid == givenPid ){
                return ai.processName;
            }
        }

        List<ActivityManager.RunningServiceInfo> svcInfo = am.getRunningServices(Integer.MAX_VALUE);
        for(ActivityManager.RunningServiceInfo si : svcInfo){
            if(si.pid == givenPid){
                return si.process;
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        doAnalytics(uri, "delete");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delCount = 0;

        switch(URI_MATCHER.match(uri)){
            case NOTE_LIST:
                delCount = db.delete(NoteDBSchema.TBL_NOTES, selection, selectionArgs);
                break;
            case NOTE_ID:
                String idStr = uri.getLastPathSegment();
                String where = NoteContract.Notes._ID + " = " + idStr;
                if(!TextUtils.isEmpty(selection)){
                    where += " AND " + selection;
                }
                delCount = db.delete(NoteDBSchema.TBL_NOTES, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if(delCount > 0 && !isInBatchMode()){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        doAnalytics(uri, "update");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)){
            case NOTE_LIST:
                updateCount = db.update(NoteDBSchema.TBL_NOTES, values, selection, selectionArgs);
                break;
            case NOTE_ID:

                String idStr = uri.getLastPathSegment();

                Log.d("cpsample", "idSt: " + idStr);
                String where = "cast(" + NoteContract.Notes._ID + " as text) = " + idStr;
                if(!TextUtils.isEmpty(selection)){
                    where += " AND " + selection;
                }
                updateCount = db.update(NoteDBSchema.TBL_NOTES, values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if(updateCount > 0 && !isInBatchMode()){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d("cpsample", "updateCount: " + updateCount);
        return updateCount;
    }
}
