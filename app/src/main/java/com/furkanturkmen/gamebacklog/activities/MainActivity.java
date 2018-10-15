package com.furkanturkmen.gamebacklog.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.furkanturkmen.gamebacklog.R;
import com.furkanturkmen.gamebacklog.adapters.GameAdapter;
import com.furkanturkmen.gamebacklog.models.Game;
import com.furkanturkmen.gamebacklog.utils.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameAdapter.GameClickListener {


    //Constants used when calling the update activity
    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;
    private static final int EDIT_REQUEST_CODE = 1546;
    private static final int ADD_REQUEST_CODE = 1413;
    static AppDatabase db;

    public static final int REQUEST_CODE = 4123;
    public static final String EXTRA_GAME = "GAME";


    private List<Game> gameList;
    private RecyclerView rView;
    private GameAdapter gameAdapter;
    public CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("My games");

        gameList = new ArrayList<>();
        cardView = findViewById(R.id.cardView);
        rView = findViewById(R.id.rView);
        rView.setLayoutManager(new GridLayoutManager(this, 1));
        db = AppDatabase.getInstance(this);
        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();

        FloatingActionButton fab = findViewById(R.id.addGameFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameAsyncTask(TASK_DELETE_GAME).execute(gameList.get(position));
                        gameList.remove(position);
                        updateUI();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rView);

        updateUI();
    }


    private void updateUI() {
        if (gameAdapter == null) {
            gameAdapter = new GameAdapter(gameList, this);
            rView.setAdapter(gameAdapter);
        } else {
            gameAdapter.swapList(gameList);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Game game = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                new GameAsyncTask(TASK_INSERT_GAME).execute(game);
            }
        } else if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Game game = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                new GameAsyncTask(TASK_UPDATE_GAME).execute(game);
            }
        }
    }

    public void onGameDbUpdated(List list) {
        gameList = list;
        updateUI();
    }

    @Override
    public void GameOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(EXTRA_GAME, gameList.get(i));
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    public class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {
                case TASK_DELETE_GAME:
                    db.gameDao().deleteGames(games[0]);
                    break;

                case TASK_UPDATE_GAME:
                    db.gameDao().updateGames(games[0]);
                    break;

                case TASK_INSERT_GAME:
                    db.gameDao().insertGames(games[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }
    }
}
