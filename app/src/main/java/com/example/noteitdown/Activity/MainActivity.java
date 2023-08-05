package com.example.noteitdown.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteitdown.Adapter.NotesAdapter;
import com.example.noteitdown.Database.NotesDatabase;
import com.example.noteitdown.Functions.FirebaseOperations;
import com.example.noteitdown.Functions.UserFunctions;
import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.Model.User;
import com.example.noteitdown.R;
import com.example.noteitdown.ViewModel.NotesViewModel;
import com.example.noteitdown.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotesViewModel notesViewModel;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if(auth.getCurrentUser() != null){
            User.CloudState = true;
        }

        ImageView SyncButton = findViewById(R.id.SyncButton);
        SyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                if(User.CloudState){
                    dialog.setTitle("Sync");
                    dialog.setMessage("Do you want to Sync Notes to Cloud?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseOperations.SyncNotes(getApplicationContext());
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }else{
                    dialog.setTitle("Login/Register");
                    dialog.setCancelable(false);
                    dialog.setMessage("Login or Create an account to save your notes to cloud");
                    dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();
                        }
                    });

                    dialog.setNegativeButton("Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                            finish();
                        }
                    });
                }
                dialog.show();
            }
        });


        ImageView SignOut = findViewById(R.id.SignoutButton);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                if (auth.getCurrentUser() != null) {
                    dialog.setTitle("Logout");
                    dialog.setMessage("Do you want to Logout?");
                    dialog.setIcon(R.drawable.logout_icon);
                    dialog.setCancelable(true);
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotesDatabase.INSTANCE.notesDao().DeleteWholeTable();
                            NotesDatabase.DestroyDatabase();
                            dialog.dismiss();
                            User.CloudState = false;
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    dialog.show();
                }else{
                    dialog.setTitle("Exit");
                    dialog.setMessage("Do you want to Exit?");
                    dialog.setIcon(R.drawable.logout_icon);
                    dialog.setCancelable(true);
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    dialog.show();
                }
            }
        });


        recyclerView = findViewById(R.id.NotesRecyclerView);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);


        // Floating Action Button to add a new note:
        FloatingActionButton addNote = findViewById(R.id.AddNotesActionButton);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNotesActivity.class));
            }
        });


        notesViewModel.getAllNotes.observe(this,notes -> {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            adapter = new NotesAdapter(getApplicationContext(),notes);
            recyclerView.setAdapter(adapter);
        });



        // The Following Mechanism is for selecting Filters from the filter bar:
        TextView MostRecent = findViewById(R.id.MostRecent);
        TextView NoFilter = findViewById(R.id.NoFilter);
        TextView AToZFilter = findViewById(R.id.FilterAtoZ);
        TextView ZToAFilter = findViewById(R.id.FilterZtoA);
        TextView AscendingPriority =  findViewById(R.id.FilterLowToHigh);
        TextView DescendingPriority = findViewById(R.id.FilterHighToLow);

        HashMap<TextView, Boolean> StateContainers = new HashMap<>();
        StateContainers.put(MostRecent,false);
        StateContainers.put(NoFilter,false);
        StateContainers.put(AToZFilter,false);
        StateContainers.put(ZToAFilter,false);
        StateContainers.put(AscendingPriority,false);
        StateContainers.put(DescendingPriority,false);

        for(TextView key: StateContainers.keySet()){
            key.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    SelectMechanism(v,StateContainers);
                    DeselectAllExcept((TextView) v,StateContainers);
                    return false;
                }
            });
        }

    }


    public void SelectMechanism(View v, HashMap<TextView, Boolean> StateContainers){
        if(StateContainers.get(v)){
            StateContainers.put((TextView) v,false);
            NotSelectedBackground((TextView) v);
            notesViewModel.getAllNotes.observe(this,notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(),notes);
                recyclerView.setAdapter(adapter);
            });
        }else{
            StateContainers.put((TextView) v,true);
            SelectedBackground((TextView) v);
            FilteredDisplay((TextView) v);
        }
    }
    public void SelectedBackground(TextView view){
            view.setBackgroundResource(R.drawable.item_selected);
            view.setTextColor(getColor(R.color.BackgroundColour));
    }

    public void NotSelectedBackground(TextView view){

        view.setBackgroundResource(R.drawable.item_not_selected);
        view.setTextColor(getColor(R.color.IconColor));
    }

    public void DeselectAllExcept(TextView view,HashMap<TextView, Boolean> StateContainer){
        for(TextView key: StateContainer.keySet()){
            if(key != view){
                StateContainer.put((TextView) key,false);
                NotSelectedBackground((TextView) key);
            }
        }
    }

    public void FilteredDisplay(TextView view) {
        if (findViewById(R.id.NoFilter).equals(view)) {
            notesViewModel.getAllNotes.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        } else if (findViewById(R.id.FilterAtoZ).equals(view)) {
            notesViewModel.SortAToZ.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        } else if (findViewById(R.id.FilterZtoA).equals(view)) {
            notesViewModel.SortZToA.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        } else if (findViewById(R.id.FilterLowToHigh).equals(view)) {
            notesViewModel.SortAscending.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        } else if (findViewById(R.id.MostRecent).equals(view)) {
            notesViewModel.SortByMostRecent.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        } else {
            notesViewModel.SortDescending.observe(this, notes -> {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(getApplicationContext(), notes);
                recyclerView.setAdapter(adapter);
            });
        }
    }


}