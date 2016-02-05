package com.siuli.andr.whitebird.listNotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siuli.andr.whitebird.R;
import com.siuli.andr.whitebird.data.Note;

import java.util.List;

/**
 * Created by william on 1/12/2016.
 */
public class ListNoteRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> mListNote;

    public static class ViewHolderNote extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvNoteDate;
        private TextView mTvNoteId;

        public ViewHolderNote(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvNoteDate = (TextView) itemView.findViewById(R.id.tv_note_date);
            mTvNoteId = (TextView) itemView.findViewById(R.id.tv_note_id);
        }
    }

    public ListNoteRVAdapter(List<Note> listNote){
        mListNote = listNote;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rv_note, parent, false);
        RecyclerView.ViewHolder vh = new ViewHolderNote(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderNote _holder = (ViewHolderNote) holder;
        _holder.mTvTitle.setText(mListNote.get(position).getTitle());
        _holder.mTvNoteDate.setText(mListNote.get(position).getNoteDateString());
        _holder.mTvNoteId.setText(mListNote.get(position).getNoteId());
    }

    @Override
    public int getItemCount() {
        return mListNote.size();
    }

    @Override
    public long getItemId(int position) {
        return mListNote.get(position).getId();
    }

    public void insertItem(Note note){
        mListNote.add(0, note);
        notifyItemInserted(0);
    }
}
