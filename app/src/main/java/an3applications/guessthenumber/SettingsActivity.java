package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView) findViewById(R.id.settings_list_view);


        myDb = new SQLDatabaseHelper(this);

        final ArrayAdapter<String> settingsAdapter;
        final ArrayList<String> settings = new ArrayList<String>(MainActivity.name);
        settingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        lv.setAdapter(settingsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If Default name item in list view is clicked
                if (settings.get(i).matches("\nDefault name\n")) {
                    showMessage();
                }
                if (settings.get(i).matches("\nEaster Egg\n")) {
                    Intent intent = new Intent(SettingsActivity.this, EEActivity.class);
                    startActivity(intent);
                    //Do something
                }
                if (settings.get(i).matches("\nDonate\n")) {
                    Toast.makeText(SettingsActivity.this, "Thanks for donating $100 to me Natalia.", Toast.LENGTH_SHORT).show();
                    //Do something
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settings.get(i).matches("\nDonate\n")){
                    myDb.easterEggWasFound(1);
                    Toast.makeText(SettingsActivity.this, "Easter Egg unlocked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, EEActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });
    }

    public void showMessage() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("New Default Name");
        builder.setMessage("Type a new name .");
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

//    public void showAboutMessage() {
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(SettingsActivity.this);
//        builder.setTitle("About");
//        builder.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec magna dui" +
//                ", congue eu eleifend sed, rutrum blandit tortor. Maecenas egestas mauris a congue " +
//                "eleifend. Vivamus id est pretium, lobortis orci vitae, mollis nulla. Nullam " +
//                "pharetra magna at eros porta, id finibus velit ultricies. Etiam ac maximus nunc, " +
//                "at facilisis ex. Fusce feugiat sed diam vel vulputate. Morbi ac tempor sapien. ");
//
//        builder.create().show();
//    }
}