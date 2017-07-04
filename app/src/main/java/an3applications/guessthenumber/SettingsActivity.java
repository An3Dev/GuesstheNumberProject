package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static an3applications.guessthenumber.MainActivity.isMoveToFirstTriggered;
import static an3applications.guessthenumber.MainActivity.name;

public class SettingsActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    ListView lv;
    ArrayList<String> settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView) findViewById(R.id.settings_list_view);


        myDb = new SQLDatabaseHelper(this);



        if(!isMoveToFirstTriggered) {
            name.clear();
            name.add("\nDefault name\n");
            name.add("\nDonate\n");
            name.add("\nTheme\n");
            longPressDonate();
        }

        final ArrayAdapter<String> settingsAdapter;
        settings = new ArrayList<String>(MainActivity.name);
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
                    Intent intent = new Intent(SettingsActivity.this, EasterEggActivity.class);
                    startActivity(intent);
                    //Do something
                }
                if (settings.get(i).matches("\nDonate\n")) {
                    //Do something
                    Toast.makeText(SettingsActivity.this, "Sorry, this is not available yet.", Toast.LENGTH_SHORT).show();
                }
                if (settings.get(i).matches("\nTheme\n")) {
                    //Do something
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(new ContextThemeWrapper(SettingsActivity.this, R.style.AlertDialogCustom));
                    builder.setCancelable(false);
                    builder.setTitle("Select your theme");
                    RadioGroup themeRG = new RadioGroup(SettingsActivity.this);
                    ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT);
                    themeRG.setLayoutParams(lp);
                    RadioButton lightThemeRadio = new RadioButton(SettingsActivity.this);
                    ListView.LayoutParams lPLight= new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT);
                    lightThemeRadio.setLayoutParams(lPLight);
                    lightThemeRadio.setText("Light");
                    builder.setMessage("Many to choose from!");
                    builder.setView(lightThemeRadio);
                    RadioButton darkThemeRadio = new RadioButton(SettingsActivity.this);
                    ListView.LayoutParams lPDark= new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT);
                    darkThemeRadio.setLayoutParams(lPDark);
                    lightThemeRadio.setText("Dark");
                    lightThemeRadio.setTextColor(getResources().getColor(R.color.black));
                    builder.setMessage("Many to choose from!");
                    builder.setView(themeRG);
                    builder.setView(lightThemeRadio);
                    builder.setView(darkThemeRadio);
                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //needs to save chosen theme in SQLite
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void showMessage() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("New Default Name");
        builder.setMessage("Type in your first name or nickname.");
        builder.setCancelable(false);
        final EditText input = new EditText(SettingsActivity.this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
                    //insert shared preferences
                    SharedPreferences defaultNameSharedPrefs = getSharedPreferences("defaultName", Context.MODE_PRIVATE);
                    SharedPreferences.Editor defaultNameEditor = defaultNameSharedPrefs.edit();
                    defaultNameEditor.putString("DEFAULT_NAME", input.getText().toString());
                    defaultNameEditor.commit();
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

    public void longPressDonate() {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settings.get(i).matches("\nDonate\n")){
                    myDb.insertEasterEggFoundData("true");
                    Toast.makeText(SettingsActivity.this, "You just found an \"Easter Egg\" and its not even available yet! Nice!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, EasterEggActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

}