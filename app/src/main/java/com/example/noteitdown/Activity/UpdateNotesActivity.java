package com.example.noteitdown.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.noteitdown.Functions.FirebaseOperations;
import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.R;
import com.example.noteitdown.ViewModel.NotesViewModel;
import com.example.noteitdown.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;


    String priority;
    NotesViewModel notesViewModel;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        priority = getIntent().getStringExtra("Priority");
        ID = getIntent().getIntExtra("ID",0);

        switch (priority){
            case "1":
                binding.greenLowPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
                break;
            case "2":
                binding.yellowMediumPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
                break;
            case "3":
                binding.redHighPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
                break;
        }

        binding.TitleUpdateNotes.setText(getIntent().getStringExtra("Title"));
        binding.SubtitleUpdateNotes.setText(getIntent().getStringExtra("Subtitle"));
        binding.NoteUpdateNotes.setText(getIntent().getStringExtra("Note"));


        binding.greenLowPriorityOptionUpdateNotes.setOnClickListener(v ->{
            priority  = "1";
            binding.redHighPriorityOptionUpdateNotes.setImageResource(0);
            binding.yellowMediumPriorityOptionUpdateNotes.setImageResource(0);
            binding.greenLowPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
            Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
        });


        binding.yellowMediumPriorityOptionUpdateNotes.setOnClickListener(v ->{
            priority  = "2";
            binding.greenLowPriorityOptionUpdateNotes.setImageResource(0);
            binding.redHighPriorityOptionUpdateNotes.setImageResource(0);
            binding.yellowMediumPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
            Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
        });


        binding.redHighPriorityOptionUpdateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = "3";
                binding.greenLowPriorityOptionUpdateNotes.setImageResource(0);
                binding.yellowMediumPriorityOptionUpdateNotes.setImageResource(0);
                binding.redHighPriorityOptionUpdateNotes.setImageResource(R.drawable.done_icon);
                Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
            }
        });


        binding.UpdateButtonUpdateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = binding.TitleUpdateNotes.getText().toString();
                String SubTitle = binding.SubtitleUpdateNotes.getText().toString();
                String Note = binding.NoteUpdateNotes.getText().toString();


                UpdateANote(ID,Title,SubTitle,Note,priority);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            private void UpdateANote(int ID,String title, String subTitle, String note, String priority) {

                Date date = new Date();
                CharSequence sequence  = DateFormat.getTimeInstance().format(date);

                Notes UpdatedNote = new Notes(ID,title,subTitle,sequence.toString(),priority,note);
                notesViewModel.updateNote(UpdatedNote);
                FirebaseOperations.UpdateNoteOnline(ID,UpdatedNote,getApplicationContext());
            }
        });


        binding.DeleteButtonUpdateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UpdateNotesActivity.this);
                View view = LayoutInflater.from(UpdateNotesActivity.this).inflate(R.layout.delete_note_bottom_sheet, findViewById(R.id.bottom_sheet_design));
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

                Button yes = view.findViewById(R.id.yes_delete_button);
                Button no = view.findViewById(R.id.no_delete_button);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notesViewModel.deleteNote(ID);
                        FirebaseOperations.DeleteNoteOnline(ID,getApplicationContext());
                        bottomSheetDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });



    }
}