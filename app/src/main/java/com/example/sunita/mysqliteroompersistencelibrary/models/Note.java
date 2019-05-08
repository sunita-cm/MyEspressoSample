package com.example.sunita.mysqliteroompersistencelibrary.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int id;

    @ColumnInfo(name = "note_name")
    private String notes;

    @ColumnInfo(name = "note_time")
    private String time;

    @ColumnInfo(name = "user_id")
    private int userId;

    @Ignore
    public Note() {
    }

    public Note(String notes, int userId, String time) {
        this.userId = userId;
        this.notes = notes;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}