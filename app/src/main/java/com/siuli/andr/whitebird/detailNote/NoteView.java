package com.siuli.andr.whitebird.detailNote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.addNote.AddNoteView;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.listNotes.ListNoteView;

/**
 * Created by william on 1/12/2016.
 */
public class NoteView extends AppCompatActivity implements INoteView {

    private TextView mTextViewTitle;
    private TextView mTextViewNoteDate;
    private TextView mTextViewDescription;

    private INotePresenter mPresenter;

    private long noteId;

    private AccountManager mAccountManager;
    private Account mConnectedAccount;

    String authToken;

    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            if (authToken != null) {
                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
                            }
                            Log.d("siuli", ((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
                            Log.d("siuli", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , null);
    }

    private TableObserver observer;
    public class TableObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public TableObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.d("siuli", "onChange ContentObserver " + uri);

            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // performing a sync no matter if it's off
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // performing a sync no matter if it's off
            ContentResolver.requestSync(mConnectedAccount, NoteContract.AUTHORITY, bundle);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteview);

        mTextViewTitle = (TextView) findViewById(R.id.tv_title_detail);
        mTextViewNoteDate = (TextView) findViewById(R.id.tv_note_date_detail);
        mTextViewDescription = (TextView) findViewById(R.id.tv_description_detail);

        //Note note = getIntent().getParcelableExtra("note");
        noteId = getIntent().getLongExtra("noteId", 0);
        //showNote(note);
        mPresenter = new NotePresenter(getApplicationContext(), this, noteId);

        mAccountManager = AccountManager.get(this);
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
        observer = new TableObserver(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(NoteContract.CONTENT_URI, true, observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(observer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailnote, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                editNote(noteId);
                return true;
            case R.id.action_delete:
                deleteNote();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showNote(Note note) {
        mTextViewTitle.setText(note.getTitle());
        mTextViewDescription.setText(note.getDescription());
        mTextViewNoteDate.setText(note.getNoteDateString());
    }

    @Override
    public void deleteNote() {
        new AlertDialog.Builder(this)
                .setTitle("Delete note")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteNote(noteId);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
        .show();
    }

    @Override
    public void editNote(long noteId) {
        mPresenter.updateNote(noteId);
    }

    @Override
    public void finishAct() {
        finish();
    }
}
