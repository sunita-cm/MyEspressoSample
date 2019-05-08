package com.example.sunita.mysqliteroompersistencelibrary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.sunita.mysqliteroompersistencelibrary.models.Note;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void insertNotes(Note note);

    @Update
    void updateNotes(Note note);

    @Delete
    void deleteNotes(Note note);

    @Query("select * from note_table order by note_time desc")
    List<Note> getAllNotes();
}