package com.example.noteitdown.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// This is the entity of our database where we define all our columns and define the Primary Key of our database

@Entity(tableName = "Notes_Database")
public class Notes {


    public Notes() {
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "note_title")
    private String Note_Title;


    @ColumnInfo(name = "note_subtitle")
    private String NoteSubtitle;

    @ColumnInfo(name = "note_date")
    private String NoteDate;

    @ColumnInfo(name = "note_priority")
    private String NotePriority;

    @ColumnInfo(name = "note")
    private String Note;

    @Ignore
    public Notes(String note_Title, String noteSubtitle, String noteDate, String notePriority, String note) {
        Note_Title = note_Title;
        NoteSubtitle = noteSubtitle;
        NoteDate = noteDate;
        NotePriority = notePriority;
        Note = note;
    }

    public Notes(int id, String note_Title, String noteSubtitle, String noteDate, String notePriority, String note) {
        this.id = id;
        Note_Title = note_Title;
        NoteSubtitle = noteSubtitle;
        NoteDate = noteDate;
        NotePriority = notePriority;
        Note = note;
    }

    @Ignore
    public Notes(String note_Title, String noteSubtitle, String note) {
        Note_Title = note_Title;
        NoteSubtitle = noteSubtitle;
        Note = note;
    }

    @Ignore
    public Notes(String note_Title, String noteSubtitle, String noteDate, String note) {
        Note_Title = note_Title;
        NoteSubtitle = noteSubtitle;
        NoteDate = noteDate;
        Note = note;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote_Title() {
        return Note_Title;
    }

    public void setNote_Title(String note_Title) {
        Note_Title = note_Title;
    }

    public String getNoteSubtitle() {
        return NoteSubtitle;
    }

    public void setNoteSubtitle(String noteSubtitle) {
        NoteSubtitle = noteSubtitle;
    }

    public String getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(String noteDate) {
        NoteDate = noteDate;
    }

    public String getNotePriority() {
        return NotePriority;
    }

    public void setNotePriority(String notePriority) {
        NotePriority = notePriority;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
