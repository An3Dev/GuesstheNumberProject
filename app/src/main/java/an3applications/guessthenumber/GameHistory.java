package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameHistory extends AppCompatActivity {
    SQLDatabaseHelper myDb;
    GridView gv;
    GridView cn;
    Random rand = new Random();


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
            players.add("Oops, no games");
            players.add("were found.");
            players.add("So play a game!");
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

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                Cursor c = myDb.getAllData();
                final List<String> randItem = new ArrayList<String>();
                int amount = c.getCount();
                int randNum = rand.nextInt(amount) + 1;

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (c.getCount() == 0) {
                        return;
                    }else{
                        while(c.moveToNext()) {
                            String name = c.getString(0);
                            String tries = c.getString(1);
                            String difficulty = c.getString(2);
                            randItem.add(name);
                            randItem.add(tries);
                            randItem.add(difficulty);
                        }
                    }
                    String randItemString = randItem.get(randNum);
                    Toast.makeText(GameHistory.this, "You have 30 seconds to find a random item that I've chosen. To find it, long click on it.", Toast.LENGTH_LONG).show();
//                    SharedPreferences defaultNameSharedPrefs = getSharedPreferences("EASTER_EGG_2", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor defaultNameEditor = defaultNameSharedPrefs.edit();
//                    defaultNameEditor.putString("DEFAULT_NAME", "");
//                    defaultNameEditor.commit();
                    new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                        }

                    }.start();
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
                players.add("No");
                players.add("History");
                players.add("Here");
                gv.setAdapter(adapter);
                dialogInterface.dismiss();

            }
        });

        builder.create().show();
    }

}