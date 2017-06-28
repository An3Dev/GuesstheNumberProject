package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    ListView lv;
    int numOfTapsEE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView) findViewById(R.id.settings_list_view);

        List<String> name = new ArrayList<>();
        myDb = new SQLDatabaseHelper(this);
        final Cursor c = myDb.getIsEasterEggFound();
        //The code below causes crash.
//        if (c.getInt(0) == 0) {
//
//            name.add("Default name\n");
//            name.add("\nAbout\n");
//        }
//        if (c.getInt(0) == 1){
//            name.add("Default name\n");
//            name.add("\nAbout\n");
//            name.add("\nEaster egg\n");
//        }
        //Code above causes crash
        final ArrayAdapter<String> settingsAdapter;
        final ArrayList<String> settings = new ArrayList<String>(name);
        settingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        lv.setAdapter(settingsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If Default name item in list view is clicked
                if (settings.get(i).matches("Default name\n")) {
                    showMessage();
                }
                if (settings.get(i).matches("\nAbout\n")) {

                    c.moveToFirst();
                    numOfTapsEE += 1;
                    if (numOfTapsEE != 6) {
                        showAboutMessage();
                    }
                    if(numOfTapsEE == 6) {
                        // // TODO: 6/26/17 Make another column in SQLite and when this is unlocked another listview item will be added called Easter Egg so the user accesses it there.

//                        if (c.getInt(0) == 0){
//
//                        }
                        myDb.easterEggWasFound();
                        Toast.makeText(SettingsActivity.this, "Easter Egg unlocked!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this, EE.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void showMessage() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("New Default Name");
        builder.setMessage("Type a new name");
        builder.setCancelable(false);
        final EditText input = new EditText(SettingsActivity.this);
        ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "There's never been a name \"\", lets keep it that way. If your name is \"\" then email me.", Toast.LENGTH_LONG).show();
                }
                if (input.getText().toString().length() <= 10 & input.getText().toString().length() > 0) {
                    myDb.updateDefaultNameData(input.getText().toString());
                    Toast.makeText(SettingsActivity.this, "Your default name was changed to " + input.getText().toString(), Toast.LENGTH_SHORT).show();
                    wantToCloseDialog = true;
                } if (input.getText().toString().length() > 10) {
                Toast.makeText(SettingsActivity.this, "Your name is too long. It has to be under 10 characters.", Toast.LENGTH_LONG).show();

            }
                if(wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }

    public void showAboutMessage() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("About");
        builder.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec magna dui" +
                ", congue eu eleifend sed, rutrum blandit tortor. Maecenas egestas mauris a congue " +
                "eleifend. Vivamus id est pretium, lobortis orci vitae, mollis nulla. Nullam " +
                "pharetra magna at eros porta, id finibus velit ultricies. Etiam ac maximus nunc, " +
                "at facilisis ex. Fusce feugiat sed diam vel vulputate. Morbi ac tempor sapien. ");

        builder.create().show();
    }
}