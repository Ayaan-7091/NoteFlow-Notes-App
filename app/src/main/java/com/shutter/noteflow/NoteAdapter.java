package com.shutter.noteflow;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.content.Context;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteViewHolder> {
Context context;
Utility utility = new Utility();

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {

        holder.title_text_view.setText(note.noteTitle);
        holder.content_text_view.setText(note.noteContent);
        holder.timestamp_text_view.setText(utility.timestampToString(note.timestamp));
        //timestamp to string called form utility to convert the timestamp

        //now to update the note we have to send all this content through intent
        holder.itemView.setOnClickListener( (v)->{

                Intent intent = new Intent(context,NoteDetailsActivity.class);
                intent.putExtra("title",note.noteTitle);
                intent.putExtra("content",note.noteContent);
                String docId = this.getSnapshots().getSnapshot(position).getId();
                 intent.putExtra("docId",docId);
                context.startActivity(intent);


        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);

        return new NoteViewHolder(view);
    }











    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView title_text_view,content_text_view,timestamp_text_view;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title_text_view = itemView.findViewById(R.id.note_title_textview);
            content_text_view = itemView.findViewById(R.id.note_content_textview);
            timestamp_text_view = itemView.findViewById(R.id.note_timestamp_textview);
        }
    }
}
