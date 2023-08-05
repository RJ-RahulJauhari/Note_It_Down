package com.example.noteitdown.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.noteitdown.Model.Notes;

import java.util.List;

@androidx.room.Dao
public interface NotesDao {

    // Where every we change the schema of our DAO or database we have to increment the database version no. by some value in the
    // NotesDatabase class.....

    @Query("SELECT * FROM Notes_Database")
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM Notes_Database")
    List<Notes> getallNotesAsList();

    @Query("Delete FROM Notes_Database")
    void DeleteWholeTable();

    @Insert
    void insertNote(Notes... notes);

    @Query("DELETE FROM Notes_Database WHERE id =:id")
    void deleteNote(int id);

    @Update
    void updateNote(Notes notes);

    @Query("SELECT * FROM Notes_Database ORDER BY note_priority DESC")
    LiveData<List<Notes>> SortFromHighToLowPriority();

    @Query("SELECT * FROM Notes_Database ORDER BY note_priority ASC")
    LiveData<List<Notes>> SortFromLowToHigh();


    @Query("SELECT * FROM Notes_Database ORDER BY note_title ASC")
    LiveData<List<Notes>> SortFromAToZ();


    @Query("SELECT * FROM Notes_Database ORDER BY note_title DESC")
    LiveData<List<Notes>> SortFromZToA();

    @Query("SELECT * FROM Notes_Database ORDER BY id DESC")
    LiveData<List<Notes>> SortByMostRecent();

//    @Query("Select * FROM Notes_Database WHERE note_cloud_id NOT IN ")
//    LiveData<List<Notes>>

}
