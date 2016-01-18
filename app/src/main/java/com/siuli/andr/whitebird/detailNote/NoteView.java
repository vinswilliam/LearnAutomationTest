package com.siuli.andr.whitebird.detailNote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.addNote.AddNoteView;
import com.siuli.andr.whitebird.data.Note;

/**
 * Created by william on 1/12/2016.
 */
public class NoteView extends AppCompatActivity implements INoteView {

    private TextView mTextViewTitle;
    private TextView mTextViewNoteDate;
    private TextView mTextViewDescription;

    private INotePresenter mPresenter;

    private long noteId;

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

    }

    @Override
    public void editNote(long noteId) {
        Intent addtNoteIntent = new Intent(this, AddNoteView.class);
        addtNoteIntent.putExtra("noteId", noteId);
        startActivity(addtNoteIntent);
    }
}
