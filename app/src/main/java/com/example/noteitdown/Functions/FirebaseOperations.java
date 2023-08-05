package com.example.noteitdown.Functions;



import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteitdown.Activity.MainActivity;
import com.example.noteitdown.Adapter.NotesAdapter;
import com.example.noteitdown.Database.NotesDatabase;
import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.Model.User;
import com.example.noteitdown.ViewModel.NotesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseOperations {

    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public static void SyncNotes(Context context){
        NotesDatabase database = NotesDatabase.getDatabaseInstance(context);
        List<Notes> notesList = database.notesDao().getallNotesAsList();
        if(notesList.size() != 0){
            Toast.makeText(context,"Uploading to Cloud....",Toast.LENGTH_SHORT).show();
            for(int i = 0; i<notesList.size(); i++){
                Notes cur = notesList.get(i);
                FirebaseUser user = auth.getCurrentUser();
                if(User.CloudState){
                    firebaseDatabase.getReference().child("Notes").child(user.getUid()).child(String.valueOf(cur.getId())).setValue(cur);
                }
            }
        }else{
            RetrieveAllNotesOnline(context);
        }
    }

    public static void RetrieveAllNotesOnline(Context context){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        NotesDatabase database = NotesDatabase.getDatabaseInstance(context);
        firebaseDatabase.getReference().child("Notes").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotChildren =  snapshot.getChildren();
                Toast.makeText(context,"Getting your notes from Database",Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap: snapshotChildren){
                    Notes notes = snap.getValue(Notes.class);
                    database.notesDao().insertNote(notes);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void AddNoteOnline(Notes note,Context context,boolean isLooped){
        FirebaseUser user = auth.getCurrentUser();
        if(User.CloudState){
            firebaseDatabase.getReference().child("Notes").child(user.getUid()).child(String.valueOf(note.getId())).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!isLooped) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Notes have been Uploaded to Cloud...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error uploading note....", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public static void UpdateNoteOnline(int ID,Notes notes,Context context){
        if(User.CloudState) {
            firebaseDatabase.getReference().child("Notes").child(auth.getUid()).child(String.valueOf(ID)).setValue(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Note is Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error while updating note...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static void DeleteNoteOnline(int ID,Context context){
        if(User.CloudState){
            firebaseDatabase.getReference().child("Notes").child(auth.getUid()).child(String.valueOf(ID)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Note has been deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error while deleting note...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
