package com.siuli.andr.whitebird.listNotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.addNote.AddNoteView;
import com.siuli.andr.whitebird.data.Note;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listnote);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //do something
                hideIndicator();
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

    @Override
    protected void onResume() {
        super.onResume();
        mNotePresenter.getNotes();
    }

    @Override
    protected void onPause() {
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
