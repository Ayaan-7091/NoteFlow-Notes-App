package com.shutter.noteflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    Utility utility;
    EditText note_title_text,note_content_text;

    TextView page_title,delete_note_btn;
    ImageButton add_note_btn;

    boolean isEditMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        utility = new Utility();
        note_title_text = findViewById(R.id.note_title_text);
        note_content_text = findViewById(R.id.note_content_text);

        add_note_btn = findViewById(R.id.add_note_btn);
        delete_note_btn = findViewById(R.id.delete_note_btn);


        //for updation
        page_title = findViewById(R.id.page_title);
        String title,content,docId;
        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        docId = intent.getStringExtra("docId");

        if(docId!=null&&!docId.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            page_title.setText("Edit Your Note");
            note_title_text.setText(title);
            note_content_text.setText(content);
            delete_note_btn.setVisibility(View.VISIBLE);

        }




        //for adding new note

        add_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note_title =  note_title_text.getText().toString();
                String note_content = note_content_text.getText().toString();

                if( note_title.isEmpty()){
                    note_title_text.setError("Title can't be empty");
                    return;
                }

                Note note = new Note();
                note.setNoteTitle(note_title);
                note.setNoteContent(note_content);
                note.setTimestamp(Timestamp.now());
                
                saveNoteToFirebase(note);
//                Toast.makeText(NoteDetailsActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();


            }

            void saveNoteToFirebase(Note note) {

                DocumentReference documentReference;

                if(isEditMode) {
                    //if edit mode then it will use hte doc id
                    documentReference = utility.getCollectionReferenceForNotes().document(docId);
                }
                else{
                    //else it will create new
                    documentReference = utility.getCollectionReferenceForNotes().document();
                }

                documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NoteDetailsActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(NoteDetailsActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //to delete note
        delete_note_btn.setOnClickListener((v)->{

            DocumentReference documentReference;
            documentReference = utility.getCollectionReferenceForNotes().document(docId);
            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(NoteDetailsActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(NoteDetailsActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });
    }




}