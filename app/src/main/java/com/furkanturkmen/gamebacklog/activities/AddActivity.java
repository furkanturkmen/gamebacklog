package com.furkanturkmen.gamebacklog.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.furkanturkmen.gamebacklog.R;
import com.furkanturkmen.gamebacklog.models.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    Spinner sItems;
    FloatingActionButton fabSaveGame;
    EditText titleGame;
    EditText platformGame;
    EditText notesGame;
    String selectedItemText;
    Intent intent;
    String dateNow = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
    Game newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add new game");

        sItems = findViewById(R.id.spinnerStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);

        titleGame = findViewById(R.id.inputTitle);
        platformGame = findViewById(R.id.inputPlatform);
        notesGame = findViewById(R.id.inputNotes);

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fabSaveGame = findViewById(R.id.saveGameFab);
        fabSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    intent = new Intent();
                    newGame = new Game(titleGame.getText().toString(), platformGame.getText().toString(), notesGame.getText().toString(), selectedItemText, dateNow);
                    intent.putExtra(MainActivity.EXTRA_GAME, newGame);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
            }
        });
    }

}
