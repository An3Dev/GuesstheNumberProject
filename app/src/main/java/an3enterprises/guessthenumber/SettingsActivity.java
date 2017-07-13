package an3enterprises.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;

import java.util.ArrayList;

import static an3enterprises.guessthenumber.MainActivity.name;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_INVITE = 0;
    private static final String TAG = "MainActivity";
    SQLDatabaseHelper myDb;
    ListView lv;
    ArrayList<String> settings;
    String themeString;
    ArrayAdapter<String> settingsAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                    this.runOnUiThread(new Runnable() {
                        public void run() {
//                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_distributor));
                        }
                    });
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
                Toast.makeText(SettingsActivity.this, "Sharing canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        ServiceConnection mServiceConn = new ServiceConnection() {
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                mService = null;
//            }
//
//            @Override
//            public void onServiceConnected(ComponentName name,
//                                           IBinder service) {
//                mService = IInAppBillingService.Stub.asInterface(service);
//            }
//        };
//        if (mServiceConn == null) {
//            Toast.makeText(SettingsActivity.this, "Can't connect", Toast.LENGTH_SHORT).show();
//        }
//        if (mServiceConn != null) {
//            Intent serviceIntent =
//                    new Intent("com.android.vending.billing.InAppBillingService.BIND");
//            serviceIntent.setPackage("com.android.vending");
//            bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
//            Toast.makeText(SettingsActivity.this, "Connected", Toast.LENGTH_SHORT).show();
//        }

//        SharedPreferences themePrefs = getSharedPreferences("Theme", Context.MODE_PRIVATE);
//        final String themeSP = themePrefs.getString("ThemeSP", "No theme");
//        if (themeSP.matches("light")) {
//            getApplication().setTheme(R.style.AppTheme);
//        }
//        if (themeSP.matches("dark")){
//            getApplication().setTheme(R.style.AppThemeDark);
//        }

        lv = (ListView) findViewById(R.id.settings_list_view);


        myDb = new SQLDatabaseHelper(this);

        final String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString = null;
            } else {
                donation();
            }
        }





        name.clear();
        name.add(getResources().getString(R.string.default_name));
        name.add(getResources().getString(R.string.donate));
        name.add(getResources().getString(R.string.share));
//        if (isConnected) {
//            name.add(getResources().getString(R.string.achievements));
//        }
//        if (!isConnected) {
//            name.add(getResources().getString(R.string.connect_to_google));
//        }
//            name.add("\nTheme\n");
            //longPressDonate();


        settings = new ArrayList<String>(MainActivity.name);
        settingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        lv.setAdapter(settingsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If Default name item in list view is clicked
                if (settings.get(i).matches(getResources().getString(R.string.default_name))) {
                    showMessage();
                }
//                if (settings.get(i).matches("\nEaster Egg\n")) {
//                    Intent intent = new Intent(SettingsActivity.this, EasterEggActivity.class);
//                    startActivity(intent);
//                    //Do something
//                }
                if (settings.get(i).matches(getResources().getString(R.string.donate))) {
                    //Donation should open the in-app purchases
                    donation();

                }
                if (settings.get(i).matches(getResources().getString(R.string.share))) {
                    //Donation should open the in-app purchases
                    onInviteClicked();
                }
//                if (settings.get(i).matches("\nGoogle Play Games\n")) {
//                    if (isConnected) {
//                        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1234);
//                    }
//                }
//                if (settings.get(i).matches("\nConnect to Google\n")) {
//                    if (!isConnected) {
//                        name.add("\nAchievements\n");
//                        lv.setAdapter(settingsAdapter);
//                        mGoogleApiClient.connect();
//                    }
//                }
//                if (settings.get(i).matches("\nTheme\n")) {
//                    //Do something
//
//                    createAlertDialogWithRadioButtonGroup();
//                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        name.clear();
        name.add(getResources().getString(R.string.default_name));
        name.add(getResources().getString(R.string.donate));
        name.add(getResources().getString(R.string.share));
//        if (isConnected) {
//            name.add("\nAchievements\n");
//        }
//        if (!isConnected) {
//            name.add("\nConnect to Google\n");
//        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If Default name item in list view is clicked
                if (settings.get(i).matches(getResources().getString(R.string.default_name))) {
                    showMessage();
                }
//                if (settings.get(i).matches("\nEaster Egg\n")) {
//                    Intent intent = new Intent(SettingsActivity.this, EasterEggActivity.class);
//                    startActivity(intent);
//                    //Do something
//                }
                if (settings.get(i).matches(getResources().getString(R.string.donate))) {
                    //Donation should open the in-app purchases
                    donation();

                }
                if (settings.get(i).matches(getResources().getString(R.string.share))) {
                    //Donation should open the in-app purchases
                    onInviteClicked();
                }
//                if (settings.get(i).matches("\nAchievements\n")) {
//                    if (isConnected) {
//                        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1234);
//                    }
//                }
//                if (settings.get(i).matches("\nConnect to Google\n")) {
//                    if (!isConnected) {
//                        name.add("\nAchievements\n");
//                        lv.setAdapter(settingsAdapter);
//                        mGoogleApiClient.connect();
//                    }
//                }
//                if (settings.get(i).matches("\nTheme\n")) {
//                    //Do something
//
//                    createAlertDialogWithRadioButtonGroup();
//                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void showMessage() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(R.string.new_default_name);
        builder.setMessage(R.string.type_default_name);
        builder.setCancelable(false);
        final EditText input = new EditText(SettingsActivity.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                    Toast.makeText(SettingsActivity.this, getResources().getString(R.string.never_been_a_name), Toast.LENGTH_LONG).show();
                }
                if (input.getText().toString().length() <= 10 & input.getText().toString().length() > 0) {
                    //insert shared preferences
                    SharedPreferences defaultNameSharedPrefs = getSharedPreferences("defaultName", Context.MODE_PRIVATE);
                    SharedPreferences.Editor defaultNameEditor = defaultNameSharedPrefs.edit();
                    defaultNameEditor.putString("DEFAULT_NAME", input.getText().toString());
                    defaultNameEditor.commit();
                    Toast.makeText(SettingsActivity.this, getResources().getString(R.string.default_name_changed) + "  " + input.getText().toString(), Toast.LENGTH_SHORT).show();
                    wantToCloseDialog = true;
                } if (input.getText().toString().length() > 10) {

                Toast.makeText(SettingsActivity.this, R.string.name_too_long, Toast.LENGTH_LONG).show();

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

//    public void longPressDonate() {
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (settings.get(i).matches("\nDonate\n")){
//                    myDb.insertEasterEggFoundData("true");
//                    Toast.makeText(SettingsActivity.this, "You just found an \"Easter Egg\" and its not even available yet! Nice!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SettingsActivity.this, EasterEggActivity.class);
//                    startActivity(intent);
//                }
//                return false;
//            }
//        });
//    }

//    public void createAlertDialogWithRadioButtonGroup(){
//
//        final AlertDialog alertDialog1;
//        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SettingsActivity.this, R.style.AlertDialogCustom));
//        builder.setTitle("Select your theme");
//        final CharSequence [] values = {" Light", " Dark "};
//        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (item == 0) {
//                    themeString = "light";
//                }
//                else {
//                    themeString = "dark";
//                }
//
//
//            }
//        });
//
//        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //needs to save chosen theme in SQLite
//                if (themeString .matches("dark")) {
//                    Toast.makeText(SettingsActivity.this, "Theme was changed to Dark", Toast.LENGTH_SHORT).show();
//                    SharedPreferences theme = getSharedPreferences("Theme", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor themeEditor = theme.edit();
//                    themeEditor.putString("ThemeSP", "dark");
//                    themeEditor.commit();
//                    dialogInterface.dismiss();
//                }
//                else {
//                    Toast.makeText(SettingsActivity.this, "The theme was changed to Light", Toast.LENGTH_SHORT).show();
//                    SharedPreferences theme = getSharedPreferences("Theme", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor themeEditor = theme.edit();
//                    themeEditor.putString("ThemeSP", "light");
//                    themeEditor.commit();
//                }
//            }
//        });
//        alertDialog1 = builder.create();
//        alertDialog1.show();
//
//    }

    public void donation() {
        // This should open the in-app purchases.
        Intent intent = new Intent(SettingsActivity.this, DonationActivity.class);
        startActivity(intent);

    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
//                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

}