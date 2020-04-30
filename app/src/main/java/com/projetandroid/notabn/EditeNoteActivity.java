package com.projetandroid.notabn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.projetandroid.notabn.database.NotesDB;
import com.projetandroid.notabn.database.NotesDao;
import androidx.appcompat.widget.Toolbar;
import java.util.Date;

public class EditeNoteActivity extends AppCompatActivity {


        private EditText inputNote;
        private NotesDao dao;
        private Note temp;
        private Toolbar toolbar;
        public static final String NOTE_EXTRA_Key = "note_id";


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            // set theme
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
            setTheme(theme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edite_note);
            toolbar = findViewById(R.id.edit_note_activity_toolbar);
            setSupportActionBar(toolbar);

            inputNote = findViewById(R.id.input_note);
            dao = NotesDB.getInstance(this).notesDao();
            if (getIntent().getExtras() != null) {
                int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
                temp = dao.getNoteById(id);
                inputNote.setText(temp.getNoteText());
            } else inputNote.setFocusable(true);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.edite_note_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.save_note)
                onSaveNote();
            return super.onOptionsItemSelected(item);
        }

        private void onSaveNote() {


            String text = inputNote.getText().toString();
            if (!text.isEmpty()) {
                long date = new Date().getTime(); // get  system time
                // if  exist update els crete new
                if (temp == null) {
                    temp = new Note(text, date);
                    dao.insertNote(temp); // create new note and inserted to database
                } else {
                    temp.setNoteText(text);
                    temp.setNoteData(date);
                    dao.updateNote(temp); // change text and date and update note on database
                }

                finish(); // return to the MainActivity
            }

        }



}
