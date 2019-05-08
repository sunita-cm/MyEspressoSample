package com.example.sunita.mysqliteroompersistencelibrary.utils;

import android.text.format.DateFormat;

import com.example.sunita.mysqliteroompersistencelibrary.database.NotesDatabase;
import com.example.sunita.mysqliteroompersistencelibrary.models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String formatDate() {
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", new Date());
        return (String) s;
    }
}