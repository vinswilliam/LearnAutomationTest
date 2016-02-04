package com.siuli.andr.whitebird.listNotes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.account.AccountGeneral;
import com.siuli.andr.whitebird.addNote.AddNoteView;
import com.siuli.andr.whitebird.data.Note;
import com.siuli.andr.whitebird.data.NoteContract;
import com.siuli.andr.whitebird.detailNote.NoteView;

import java.util.List;

/**
 * Created by william on 1/11/2016.
 */
public class ListNoteView extends AppCompatActivity implements IListNoteView {

    private IListNotePresenter mNotePresenter;
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerViewNote;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListNoteRVAdapter mListNoteRVAdapter;

    private FloatingActionButton mFabAddNote;

    private AccountManager mAccountManager;
    private Account mConnectedAccount;

    SyncStatusObserver syncObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshSyncStatus();
                }
            });
        }
    };

    private void refreshSyncStatus(){
        String status;

        if(ContentResolver.isSyncActive(mConnectedAccount, NoteContract.AUTHORITY)) {
            showIndicator();
            status = "Status: Syncing..";
        } else if (ContentResolver.isSyncPending(mConnectedAccount, NoteContract.AUTHORITY)) {

            status = "Status: Pending..";
        } else {
            hideIndicator();
            status = "Status: idle";
        }

        Log.d("siuli", "Sync > " + status);
    }

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
//                                initButtonsAfterConnect();
                            }
                            Log.d("siuli", ((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
                            Log.d("siuli", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
//                            showMessage(e.getMessage());
                        }
                    }
                }
                , null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listnote);
        mAccountManager = AccountManager.get(this);
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(mConnectedAccount == null){
                    Toast.makeText(getApplicationContext(), "Please connect first", Toast.LENGTH_SHORT).show();
                    return;
                }
                //do something
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // performing a sync no matter if it's off
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // performing a sync no matter if it's off
                ContentResolver.requestSync(mConnectedAccount, NoteContract.AUTHORITY, bundle);

            }
        });

        mRecyclerViewNote = (RecyclerView) findViewById(R.id.rv_note);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewNote.setLayoutManager(mLayoutManager);
        mRecyclerViewNote.addOnItemTouchListener(new RecycleItemClickListener(getApplicationContext(),
                new RecycleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition) {
                mNotePresenter.moveToNoteDetail(view, itemPosition);
            }
        }));

        mFabAddNote = (FloatingActionButton) findViewById(R.id.fab_add_note);
        mFabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        mNotePresenter = new ListNotePresenter(this, getApplicationContext());
    }

    Object handleSyncServer;
    @Override
    protected void onResume() {
        super.onResume();
        mNotePresenter.getNotes();
        handleSyncServer = ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE
            | ContentResolver.SYNC_OBSERVER_TYPE_PENDING, syncObserver);
    }

    @Override
    protected void onPause() {
        if(handleSyncServer != null){
            ContentResolver.removeStatusChangeListener(handleSyncServer);
        }

        super.onPause();
    }

    @Override
    public void showIndicator() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideIndicator() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void addNote(Note note) {
        Intent addNoteIntent = new Intent(this, AddNoteView.class);
        startActivity(addNoteIntent);
    }

    @Override
    public void addNote() {
        addNote(null);
    }

    @Override
    public void showNotes(List<Note> listNote) {
        mListNoteRVAdapter = new ListNoteRVAdapter(listNote);
        mRecyclerViewNote.setAdapter(mListNoteRVAdapter);
    }

    @Override
    public void detailNote(Note note) {
        Intent noteDetailIntent = new Intent(this, NoteView.class);
        noteDetailIntent.putExtra("note", note);
        noteDetailIntent.putExtra("noteId", note.getId());
        startActivity(noteDetailIntent);
    }
}
