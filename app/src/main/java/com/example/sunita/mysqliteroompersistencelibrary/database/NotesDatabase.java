package com.example.sunita.mysqliteroompersistencelibrary.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sunita.mysqliteroompersistencelibrary.models.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase instance;
    private static final String DATABASE_NAME = "notes_db";

    public abstract NotesDao getNotesDao();

    public static NotesDatabase getInstance(Context context) {
        if (instance == null) {
            try {
                instance = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, DATABASE_NAME).build();
            } catch (Exception ignored) {
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}