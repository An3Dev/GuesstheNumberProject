package an3enterprises.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

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
//        Games.setViewForPopups(LoadingScreenActivity.mGoogleApiClient, findViewById(R.id.game_history));
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
        columnNamesList.add(getResources().getString(R.string.name_game_history));
        columnNamesList.add(getResources().getString(R.string.tries_game_history));
        columnNamesList.add(getResources().getString(R.string.difficulty_game_history));


        final Cursor c = myDb.getAllData();
        if (c.getCount() == 0) {
            cn.setAdapter(columnNamesAdapter);
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.no_game_history));
            builder.setMessage(getResources().getString(R.string.no_games_found));
            builder.setPositiveButton(getResources().getString(R.string.play), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(GameHistory.this, PopupActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNeutralButton(getResources().getString(R.string.not_right_now), new DialogInterface.OnClickListener() {
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
                        players.add(getResources().getString(R.string.play));
                        //adds frown emoji if success is false
                        players.add(tries + "\uD83D\uDE1E");
                        players.add(difficulty);
                    }
                    if (success == 1) {
                        players.add(getResources().getString(R.string.no_name));
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
                    if (!players.isEmpty()) {
                        if (columnNamesList.get(i).matches("Name")) {
//                            Games.Achievements.unlock(LoadingScreenActivity.mGoogleApiClient, getResources().getString(R.string.achievement_seeker));
                            if (!isNamePressed) {
                                if (!timerStarted) {
                                    AlertDialog.Builder builder;
                                    builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                                    builder.setCancelable(false);
                                    builder.setTitle(getResources().getString(R.string.mini_game));
                                    builder.setMessage(getResources().getString(R.string.found_mini_game));
                                    builder.setPositiveButton(getResources().getString(R.string.start), new DialogInterface.OnClickListener() {
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
                                            final int amount = c.getCount() * 3 - 1;
                                            final int randNum = rand.nextInt(amount) + 1;
                                            randItemString = players.get(randNum);

                                        }
                                    });
                                    builder.setNegativeButton(getResources().getString(R.string.no_thanks), new DialogInterface.OnClickListener() {
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
                            } else if (isNamePressed) {
                                return false;
                            }

                        }
                    }
                    return false;
                }
            });
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    if (timerStarted){
                        if (players.get(i).matches(randItemString)){
                            easterEggTimer.cancel();
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                            builder.setCancelable(false);
                            builder.setTitle(getResources().getString(R.string.you_found_item));
                            builder.setMessage(getResources().getString(R.string.congrats_secret_item_p1) + " "+ easterEggTime + " " + getResources().getString(R.string.congrats_secret_item_p2) + randItemString + "\"!");
                            builder.setNegativeButton(getResources().getString(R.string.awesome), new DialogInterface.OnClickListener() {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_game_history, menu);
//        MenuItem item = menu.findItem(R.id.delete);
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                showMessage();
//
//                return false;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


//    public void showMessage() {
//        final AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
//        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.delete_all_history));
//        //builder.setIcon();
//        builder.setMessage(getResources().getString(R.string.user_sure_delete_history));
//        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.deletion_canceled), Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                myDb.removeAll();
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.history_deleted), Toast.LENGTH_SHORT).show();
//                players.clear();
//                gv.setAdapter(adapter);
//                dialogInterface.dismiss();
//
//            }
//        });
//
//        builder.create().show();
//    }

    public void startEasterEggTimer() {
        easterEggTimerText.setVisibility(View.VISIBLE);
        easterEggTimerText.setTextColor(getResources().getColor(R.color.black));
        easterEggTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                easterEggTime = millisUntilFinished / 1000;
                easterEggTimerText.setText(getResources().getString(R.string.time) + millisUntilFinished / 1000);
            }
            public void onFinish() {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(new ContextThemeWrapper(GameHistory.this, R.style.AlertDialogCustom));
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.game_over));
                builder.setMessage(getResources().getString(R.string.sorry_mini_game)  + randItemString + "\".");
                builder.setNeutralButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
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