package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView) findViewById(R.id.settings_list_view);

        final ArrayAdapter<String> settingsAdapter;
        String[] name = {"Default name"};
        final ArrayList<String> settings = new ArrayList<String>(Arrays.asList(name));
        settingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        myDb = new SQLDatabaseHelper(this);

        lv.setAdapter(settingsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If Default name item in list view is clicked
                if (settings.get(i).matches("Default name")) {
                    showMessage();
                }
            }
        });
    }

    public void showMessage() {
        AlertDialog.Builder builder;
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
                myDb.updateDefaultNameData(input.getText().toString());
                Toast.makeText(SettingsActivity.this, "Your default name was changed to " + input.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }


}
