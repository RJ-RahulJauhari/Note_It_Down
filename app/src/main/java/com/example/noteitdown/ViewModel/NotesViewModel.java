package com.example.noteitdown.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.Repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    public NotesRepository repository;
    public LiveData<List<Notes>> getAllNotes;

    public LiveData<List<Notes>> SortByMostRecent;
    public LiveData<List<Notes>> SortAToZ;
    public LiveData<List<Notes>> SortZToA;
    public LiveData<List<Notes>> SortAscending;
    public LiveData<List<Notes>> SortDescending;

    public NotesViewModel(Application application) {
        super(application);

        repository = new NotesRepository(application);
        SortByMostRecent = repository.SortByMostRecent;
        getAllNotes = repository.getAllNotes;
        SortAToZ = repository.SortAToZ;
        SortZToA = repository.SortZToA;
        SortAscending = repository.SortAscending;
        SortDescending = repository.SortDescending;

    }

    public void insertNote(Notes note){
        repository.insertNote(note);
    }

    public void deleteNote(int id){
        repository.deleteNote(id);
    }

    public void updateNote(Notes note){
        repository.updateNote(note);
    }

}
