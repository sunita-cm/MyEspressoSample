package com.example.sunita.mysqliteroompersistencelibrary.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunita.mysqliteroompersistencelibrary.R;
import com.example.sunita.mysqliteroompersistencelibrary.adapters.RecyclerAdapter;
import com.example.sunita.mysqliteroompersistencelibrary.adapters.RecyclerItemTouchHelper;
import com.example.sunita.mysqliteroompersistencelibrary.database.NotesDatabase;
import com.example.sunita.mysqliteroompersistencelibrary.models.Note;
import com.example.sunita.mysqliteroompersistencelibrary.utils.Helper;
import com.example.sunita.mysqliteroompersistencelibrary.utils.IMyLongPress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, IMyLongPress {

    @BindView(R.id.rv_notes)
    RecyclerView rv_notes;
    @BindView(R.id.tv_nothing)
    TextView tv_nothing;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindString(R.string.enternote)
    String enterNote;
    @BindString(R.string.exit_text)
    String exitText;

    @BindString(R.string.note_deleted)
    String noteDeleted;
    @BindString(R.string.note_added)
    String noteAdded;

    private NotesDatabase notesDatabase;
    private RecyclerAdapter recyclerAdapter;
    private Dialog dialog;
    private AlertDialog.Builder builder;
    private ArrayList<Note> noteArrayList;
    private boolean doubleBackToExitPressedOnce = false;

    public void setUpRecyclerView() {
        noteArrayList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter();
        rv_notes.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rv_notes.setLayoutManager(linearLayoutManager);
        recyclerAdapter.setLongClick(this);
        rv_notes.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setUpRecyclerView();
        notesDatabase = NotesDatabase.getInstance(MainActivity.this);
        new DatabaseAsync().execute(1);
        recyclerAdapter.addData(noteArrayList);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_notes);
    }

    @OnClick(R.id.fab)
    public void showNoteDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.notes_dialog, null);
        final EditText et_notes = view.findViewById(R.id.et_note);
        TextView tv_save = view.findViewById(R.id.tv_save);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);

        tv_save.setOnClickListener(v -> {
            String notes = et_notes.getText().toString();
            if (!TextUtils.isEmpty(notes)) {
                dialog.dismiss();
                final Note note = new Note();
                note.setNotes(notes);
                note.setTime(Helper.formatDate());
                new DatabaseAsync(note).execute(2);
                recyclerAdapter.addSingleData(note);
                Toast.makeText(this, noteAdded, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, enterNote, Toast.LENGTH_SHORT).show();
            }
        });

        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        if (builder != null) {
            builder.setView(view);
            dialog = builder.create();
        }
        if (dialog != null) {
            dialog.show();
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setMessage(exitText)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> finish())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel())
                    .show();
        }
        doubleBackToExitPressedOnce = true;
    }

    @Override
    protected void onDestroy() {
        NotesDatabase.destroyInstance();
        super.onDestroy();
    }

    private void toggleEmptyNotes() {
        if (!noteArrayList.isEmpty()) {
            tv_nothing.setVisibility(View.GONE);
        } else {
            tv_nothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerAdapter.MyViewHolder) {
            Note note = new Note(noteArrayList.get(viewHolder.getAdapterPosition()).getNotes(), noteArrayList.get(viewHolder.getAdapterPosition()).getId(), noteArrayList.get(viewHolder.getAdapterPosition()).getTime());
            new DatabaseAsync(note).execute(3);
            recyclerAdapter.removeNote(position);
            Toast.makeText(this, noteDeleted, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLongPress(Note note) {
        showUpdateDialog(note);
    }

    /**
     * Database Async class to fetch the data from the database.
     * It gets all the notes.
     * returns List of all the notes
     *
     * @author Sunita
     * @since 05/15/2018
     */
    @SuppressLint("StaticFieldLeak")
    private class DatabaseAsync extends AsyncTask<Integer, Void, List<Note>> {
        private Note note;
        private int a;

        DatabaseAsync() {
        }

        DatabaseAsync(Note note) {
            this.note = note;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Note> doInBackground(Integer... voids) {
            a = voids[0];
            switch (a) {
                case 1:
                    return notesDatabase.getNotesDao().getAllNotes();
                case 2:
                    notesDatabase.getNotesDao().insertNotes(note);
                    break;
                case 3:
                    notesDatabase.getNotesDao().deleteNotes(note);
                    break;
                case 4:
                    notesDatabase.getNotesDao().updateNotes(note);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Note> aVoid) {
            super.onPostExecute(aVoid);
            if (a == 1) {
                noteArrayList.addAll(aVoid);
            }
            progressBar.setVisibility(View.GONE);
            toggleEmptyNotes();
        }

    }

    private void showUpdateDialog(Note note) {
        builder = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.updatenotes_dialog, null);
        final EditText et_updateNote = view.findViewById(R.id.et_updatenote);
        TextView tv_update = view.findViewById(R.id.tv_update);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);

        et_updateNote.setText(note.getNotes());
        et_updateNote.selectAll();

        tv_update.setOnClickListener(v -> {
            String notes = et_updateNote.getText().toString();
            if (!TextUtils.isEmpty(notes)) {
                if (dialog != null)
                    dialog.dismiss();
                note.setNotes(notes);
                note.setTime(Helper.formatDate());
                new DatabaseAsync(note).execute(4);
                recyclerAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, enterNote, Toast.LENGTH_SHORT).show();
            }
        });

        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        if (builder != null) {
            builder.setView(view);
            dialog = builder.create();
        }
        if (dialog != null) {
            dialog.show();
            dialog.setCancelable(false);
        }
    }
}