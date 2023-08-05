package com.example.noteitdown.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteitdown.Dao.NotesDao;
import com.example.noteitdown.Model.Notes;


/* To create a database we have to extend it from the RoomDatabase class and make it abstract,
   which means this class can not be instantiated.
 */

@Database(entities = {Notes.class}, version = 35,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();

    public static NotesDatabase INSTANCE;


    public static NotesDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "Notes_Database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void DestroyDatabase(){
        INSTANCE = null;
    }

}
