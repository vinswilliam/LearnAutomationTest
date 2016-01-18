package com.siuli.andr.whitebird.addNote;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.addNote.DatePickerFragment.OnDateSetListener;
import com.siuli.andr.whitebird.data.Note;
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

    }

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
//        DatePickerDialog
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
