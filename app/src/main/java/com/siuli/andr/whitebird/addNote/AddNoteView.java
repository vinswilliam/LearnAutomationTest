package com.siuli.andr.whitebird.addNote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.addNote.DatePickerFragment.OnDateSetListener;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.data.StatusItem;
import com.siuli.andr.whitebird.detailNote.NoteView;
import com.siuli.andr.whitebird.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by william on 1/13/2016.
 */
public class AddNoteView extends AppCompatActivity implements IAddNoteView, OnDateSetListener, TimePickerFragment.OnTimeSetListener {

    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String TIME_FORMAT = "hh:mm a";
    
    private IAddNotePresenter mPresenter;

    private ViewGroup viewParent;
    private EditText etTitle;
    private EditText etDesc;
    private Button btnDate;
    private Button btnTime;

    private long mNoteId;
    private Calendar calendarNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        viewParent = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etTitle = (EditText) findViewById(R.id.et_note_title);
        etDesc = (EditText) findViewById(R.id.et_note_desc);

        btnDate = (Button) findViewById(R.id.btn_note_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment();
                dpf.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnTime = (Button) findViewById(R.id.btn_note_time);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment tpf = new TimePickerFragment();
                tpf.show(getSupportFragmentManager(), "timePicker");
            }
        });

        mNoteId = getIntent().getLongExtra("noteId", 0);

        mPresenter = new AddNotePresenter(getApplicationContext(), this, mNoteId);

//        mAccountManager = AccountManager.get(this);
//        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
//        observer = new TableObserver(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getContentResolver().registerContentObserver(NoteContract.CONTENT_URI, true, observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        getContentResolver().unregisterContentObserver(observer);
    }

//    private AccountManager mAccountManager;
//    private Account mConnectedAccount;

//    String authToken;
//
//    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
//        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
//                new AccountManagerCallback<Bundle>() {
//                    @Override
//                    public void run(AccountManagerFuture<Bundle> future) {
//                        Bundle bnd = null;
//                        try {
//                            bnd = future.getResult();
//                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                            if (authToken != null) {
//                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
//                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
//                            }
//                            Log.d("siuli", ((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
//                            Log.d("siuli", "GetTokenForAccount Bundle is " + bnd);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                , null);
//    }

//    private TableObserver observer;
//    public class TableObserver extends ContentObserver {
//
//        /**
//         * Creates a content observer.
//         *
//         * @param handler The handler to run {@link #onChange} on, or null if none.
//         */
//        public TableObserver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//        }
//
//        @Override
//        public void onChange(boolean selfChange, Uri uri) {
//            Log.d("siuli", "onChange ContentObserver " + uri);
//
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // performing a sync no matter if it's off
//            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // performing a sync no matter if it's off
//            ContentResolver.requestSync(mConnectedAccount, NoteContract.AUTHORITY, bundle);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addnote, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:

                List<String> values = new ArrayList<>();

                String untitled = getResources().getString(R.string.untitled);

                String _title = etTitle.getText().length() == 0 ? untitled : etTitle.getText().toString();
                String _desc = etDesc.getText().length() == 0 ? "" : etDesc.getText().toString();
                String _note_created = String.valueOf(Calendar.getInstance().getTimeInMillis());
                String _note_date = String.valueOf(calendarNote.getTimeInMillis());

                values.add(String.valueOf(mNoteId));
                values.add(_title);
                values.add(_desc);
                values.add(_note_created);
                values.add(_note_date);
                values.add(String.valueOf(StatusItem.FLAG_ACTIVE));

                if(mNoteId > 0) {
                    mPresenter.insertNote(values);
                } else {
                    mPresenter.insertNote(values);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void backToDetailNote(Uri uri) {

        long _noteId = uri == null ?  mNoteId : Long.valueOf(uri.getLastPathSegment());

        Intent intentDetailNote = new Intent(this, NoteView.class);
        intentDetailNote.putExtra("noteId", _noteId);
        startActivity(intentDetailNote);

        finish();

    }

    @Override
    public void showUpdateFailed() {
        String updateFailedString = getResources().getString(R.string.update_failed);
        Snackbar.make(viewParent, updateFailedString, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setDefaultDateTime(long dateTimeInMillis) {
        calendarNote = Calendar.getInstance();
        if(dateTimeInMillis != 0){
            calendarNote.setTimeInMillis(dateTimeInMillis);
        }
        btnDate.setText(Util.parsingLongDateTime(calendarNote.getTimeInMillis(), DATE_FORMAT));
        btnTime.setText(Util.parsingLongDateTime(calendarNote.getTimeInMillis(), TIME_FORMAT));
    }

    @Override
    public void backToListNote() {
    }

    @Override
    public void displayDatePicker() {
    }

    @Override
    public void reset() {

    }

    @Override
    public void saveNote() {

    }

    @Override
    public void init(Note note) {
        etTitle.setText(note.getTitle());
        etDesc.setText(note.getDescription());
    }

    @Override
    public void dateSet(int year, int monthOfYear, int dayOfMonth) {
        calendarNote.set(year, monthOfYear, dayOfMonth);
        String dateSelectedString = Util.parsingLongDateTime(calendarNote.getTimeInMillis(), DATE_FORMAT);
        btnDate.setText(dateSelectedString);
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        calendarNote.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarNote.set(Calendar.MINUTE, minute);
        String timeSelectedString = Util.parsingLongDateTime(calendarNote.getTimeInMillis(), TIME_FORMAT);
        btnTime.setText(timeSelectedString);
    }
}
