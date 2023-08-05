package com.example.noteitdown.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.noteitdown.Dao.NotesDao;
import com.example.noteitdown.Database.NotesDatabase;
import com.example.noteitdown.Model.Notes;

import java.util.List;

public class NotesRepository {


    // Repository is basically used to get all the field of our database in the form of LiveData:
    public NotesDao notesDao;
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> SortByMostRecent;
    public LiveData<List<Notes>> SortAToZ;
    public LiveData<List<Notes>> SortZToA;
    public LiveData<List<Notes>> SortAscending;
    public LiveData<List<Notes>> SortDescending;

    public NotesRepository(Application application){

        // First we create a an instance of our data base:
        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        // Second we get the Data Access Object from the database and will be using it to access fields of the the database:
        notesDao = database.notesDao();
        // Third now using a "LiveData" object we are storing the LiveData retrieved from the database by the function getAllNotes() in this variable:

        SortByMostRecent = notesDao.SortByMostRecent();
        getAllNotes = notesDao.getAllNotes();
        SortAToZ = notesDao.SortFromAToZ();
        SortZToA = notesDao.SortFromZToA();
        SortAscending = notesDao.SortFromLowToHigh();
        SortDescending = notesDao.SortFromHighToLowPriority();
    }

    public void insertNote(Notes note){
        notesDao.insertNote(note);
    }

    public void deleteNote(int id){
        notesDao.deleteNote(id);
    }

    public void updateNote(Notes note){
        notesDao.updateNote(note);
    }




}
