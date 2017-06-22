package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameHistory extends AppCompatActivity {
    SQLDatabaseHelper myDb;
    GridView gv;


    ArrayList<String> players = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        myDb = new SQLDatabaseHelper(this);
//        Cursor res = myDb.getAllData();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players);
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

        players.clear();

        Cursor c = myDb.getAllData();
        players.add("Name");
        players.add("Tries");
        players.add("Difficulty");
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
                if (name.toString().isEmpty()) {
                    if (success == 0) {
                        //if (Integer.parseInt(tries.toString()) == 10) {
                        players.add("No name");
                        players.add(tries + "\uD83D\uDE1E");
                        players.add(difficulty);
                    }
                    if (success == 1) {
                        players.add("No name");
                        players.add(tries);
                        players.add(difficulty);
                    }
                } else {
                    if (success == 0) {
                        //if (Integer.parseInt(tries.toString()) == 10) {
                        players.add(name);
                        players.add(tries + " \uD83D\uDE1E");
                        players.add(difficulty);

                    }
                    if (success == 1) {
                        players.add(name);
                        players.add(tries);
                        players.add(difficulty);
                    }
                }


            }

            gv.setAdapter(adapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(), players.get(i), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_history, menu);
        MenuItem item = menu.findItem(R.id.delete);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showMessage("Delete All History", "Are you sure you want to delete ALL your game history?");

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void showMessage(String title, String Message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        //builder.setIcon();
        builder.setMessage(Message);
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
