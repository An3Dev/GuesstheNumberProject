package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameHistory extends AppCompatActivity {
    SQLDatabaseHelper myDb;
    GridView gv;
    GridView cn;
    Random rand = new Random();
    boolean timerStarted;
    CountDownTimer easterEggTimer;
    boolean isNamePressed;
    TextView easterEggTimerText;
    String randItemString;
    long easterEggTime;



    ArrayList<String> players = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    ArrayList<String> columnNamesList = new ArrayList<String>();
    ArrayAdapter<String> columnNamesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        myDb = new SQLDatabaseHelper(this);
//        Cursor res = myDb.getAllData();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players);
        columnNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, columnNamesList);
        easterEggTimerText = (TextView) findViewById(R.id.easterEggTimer);
        //adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players2);
//        if (res.getCount() == 0) {
//            //show message
//            showMessage("All games", "No games found right now");
//            return;
//        }
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("Name ");
//        buffer.append("Tries ");
//        buffer.append("Difficulty " + "\n\n");
//        while (res.moveToNext()) {
//            buffer.append(res.getString(0) + " ");
//            buffer.append(res.getString(1) + " ");
//            buffer.append(res.getString(2)+ "\n\n");
//        }
//        showMessage("All games", buffer.toString());
//        //showMessage("Data", "Come");

        gv = (GridView) findViewById(R.id.gridView);
        cn = (GridView) findViewById(R.id.column_names);
        columnNamesList.clear();
        players.clear();
        columnNamesList.add("Name");
        columnNamesList.add("Tries");
        columnNamesList.add("Difficulty");


        Cursor c = myDb.getAllData();
        if (c.getCount() == 0) {
            cn.setAdapter(columnNamesAdapter);
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
            builder.setCancelable(true);
            builder.setTitle("No game history");
            builder.setMessage("No games were found... so play a game!");
            builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(GameHistory.this, PopupActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNeutralButton("Not right now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } else {
            while (c.moveToNext()) {
                String name = c.getString(0);
                String tries = c.getString(1);
                String difficulty = c.getString(2);
                int success = c.getInt(3);
                //If there is no name
                if (name.toString().isEmpty()) {
                    if (success == 0) {
                        //if (Integer.parseInt(tries.toString()) == 10) {
                        players.add("No name");
                        //adds frown emoji if success is false
                        players.add(tries + "\uD83D\uDE1E");
                        players.add(difficulty);
                    }
                    if (success == 1) {
                        players.add("No name");
                        //adds happy smile emoji if success is true
                        players.add(tries + "\uD83D\uDE03");
                        players.add(difficulty);
                    }
                } else {
                    if (success == 0) {
                        //if (Integer.parseInt(tries.toString()) == 10) {
                        players.add(name);
                        players.add(tries + "\uD83D\uDE1E");
                        players.add(difficulty);

                    }
                    if (success == 1) {
                        players.add(name);
                        players.add(tries + "\uD83D\uDE03");
                        players.add(difficulty);
                    }
                }


            }

            gv.setAdapter(adapter);
            cn.setAdapter(columnNamesAdapter);
            cn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (columnNamesList.get(i).matches("Name")) {
                        if (!isNamePressed) {
                            if (!timerStarted){
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                                builder.setCancelable(false);
                                builder.setTitle("Mini game");
                                builder.setMessage("You just found a mini game! You have 15 seconds to find a secret item on the screen.");
                                builder.setPositiveButton("Start!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        isNamePressed = true;
                                        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                                        fadeIn.setDuration(500);
                                        fadeIn.setFillAfter(true);
                                        easterEggTimerText.setVisibility(View.VISIBLE);
                                        easterEggTimerText.startAnimation(fadeIn);
                                        timerStarted = true;
                                        startEasterEggTimer();

                                    }
                                });
                                builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        timerStarted = false;
                                        isNamePressed = false;
                                    }
                                });
                                builder.create();
                                builder.show();
//                    SharedPreferences defaultNameSharedPrefs = getSharedPreferences("EASTER_EGG_2", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor defaultNameEditor = defaultNameSharedPrefs.edit();
//                    defaultNameEditor.putString("DEFAULT_NAME", "");
//                    defaultNameEditor.commit();
                            }
                        }else if(isNamePressed) {
                            return false;
                        }

                    }
                    return false;
                }
            });
            final int amount = c.getCount() * 3 - 1;
            final int randNum = rand.nextInt(amount) + 1;
            randItemString = players.get(randNum);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    if (timerStarted){
                        if (players.get(i).matches(randItemString)){
                            easterEggTimer.cancel();
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                            builder.setCancelable(false);
                            builder.setTitle("You found the item!");
                            builder.setMessage("Congratulations! You found the secret item with " + easterEggTime + " seconds to spare! The secret item WAS \"" + randItemString + "\"!");
                            builder.setNegativeButton("Awesome!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                                    easterEggTimerText.startAnimation(fadeOut);
                                    fadeOut.setDuration(2000);
                                    fadeOut.setFillAfter(true);
                                    new CountDownTimer(2500, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                        }

                                        public void onFinish() {
                                            timerStarted = false;
                                            isNamePressed = false;
                                            easterEggTimerText.setVisibility(View.GONE);

                                        }

                                    }.start();
                                }
                            });
                            builder.create();
                            builder.show();

                        }
                    }

                }
            });

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_history, menu);
        MenuItem item = menu.findItem(R.id.delete);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showMessage();

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void showMessage() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
        builder.setCancelable(false);
        builder.setTitle("Delete All History");
        //builder.setIcon();
        builder.setMessage("Are you sure you want to delete ALL your game history?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(getApplicationContext(), "Deletion canceled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDb.removeAll();
                Toast.makeText(getApplicationContext(), "History deleted", Toast.LENGTH_SHORT).show();
                players.clear();
                gv.setAdapter(adapter);
                dialogInterface.dismiss();

            }
        });

        builder.create().show();
    }

    public void startEasterEggTimer() {
        easterEggTimerText.setVisibility(View.VISIBLE);
        easterEggTimerText.setTextColor(getResources().getColor(R.color.black));
        easterEggTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                easterEggTime = millisUntilFinished / 1000;
                easterEggTimerText.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                builder.setCancelable(false);
                builder.setTitle("Game Over");
                builder.setMessage("Sorry, I\'m afraid you didn't find the item on time. The item was \"" + randItemString + "\".");
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                        fadeOut.setDuration(2000);
                        fadeOut.setFillAfter(true);
                        easterEggTimerText.startAnimation(fadeOut);
                        new CountDownTimer(2500, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                easterEggTimerText.setText("0");
                                easterEggTimerText.setVisibility(View.GONE);
                                isNamePressed =false;
                                timerStarted = false;
                            }

                        }.start();
                    }
                });
                builder.create();
                builder.show();

            }

        }.start();
    }

}