package an3enterprises.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

//import static an3enterprises.guessthenumber.LoadingScreenActivity.isConnected;
//import static an3enterprises.guessthenumber.LoadingScreenActivity.mGoogleApiClient;

public class MainActivity extends AppCompatActivity {
    //static boolean mainActivityActive;
    static List<String> name = new ArrayList<String>();
    SQLDatabaseHelper myDb;
    Button play;
    Button allGames;
    Button settingsBtn;
    private FirebaseAnalytics mFirebaseAnalytics;
    //private AdView mAdView;
//    ColorDrawable[] redToBlackBackground = {
//            new ColorDrawable(Color.parseColor("#f01c00")),
//            new ColorDrawable(Color.parseColor("#000000"))
//    };
//    ColorDrawable[] blackToRedBackground = {
//            new ColorDrawable(Color.parseColor("#000000")),
//            new ColorDrawable(Color.parseColor("#f01c00"))
//    };
//
//    TransitionDrawable redToBlack;
//    TransitionDrawable blackToRed;

//    @Override
//    public void onStart(){
//        super.onStart();
//        mainActivityActive = true;
//        startColorAnimation();
//
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_SHORT).show();
//        mainActivityActive = false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getUserName();
//        redToBlack = new TransitionDrawable(redToBlackBackground);
//        blackToRed = new TransitionDrawable(blackToRedBackground);

        //View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //       | View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)

        setContentView(R.layout.activity_main);



        //ads here
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        allGames = (Button) findViewById(R.id.all_games);
        settingsBtn = (Button) findViewById(R.id.settings_button);
        play = (Button) findViewById(R.id.play_button);
//        final AnimationDrawable drawable = new AnimationDrawable();
//        final Handler handler = new Handler();
//
//        drawable.addFrame(new ColorDrawable(Color.RED), 800);
//        drawable.addFrame(new ColorDrawable(Color.BLACK), 800);
//        drawable.setOneShot(false);
//
//        play.setBackgroundDrawable(drawable);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                drawable.start();
//            }
//        }, 100);
        //Always include myDb = new SQLDatabaseHelper(this);. It gave me problems for hours for not having it.
        myDb = new SQLDatabaseHelper(this);
        gameActivity();

        SharedPreferences timesOn;
        timesOn = getSharedPreferences("timesUserHasBeenOn", Context.MODE_PRIVATE);
        int pastTimesOn = timesOn.getInt("thisNum", 0);
        SharedPreferences.Editor timesOnEditor = timesOn.edit();
        timesOnEditor.putInt("thisNum",pastTimesOn + 1);
        timesOnEditor.commit();
        int presentTimesOn = timesOn.getInt("thisNum", 1);
        if (presentTimesOn == 10 || presentTimesOn % 10 == 0) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(new android.view.ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
            builder.setTitle(R.string.donation);
            builder.setMessage(R.string.please_donate);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton(R.string.sure_donate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Button donationBtn = (Button) findViewById(R.id.donate_button);
                    donation(donationBtn);
                }
            });
            builder.show();
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        play =  (Button) findViewById(R.id.play_button);
        play.setAllCaps(false);
        allGames.setAllCaps(false);
        settingsBtn.setAllCaps(false);
        Button playedGames = (Button) findViewById(R.id.all_games);
        playedGames.setAllCaps(false);
    }

    public void donation(View view) {
        // This should open the in-app purchases.
        Intent intent = new Intent(MainActivity.this, DonationActivity.class);
        startActivity(intent);

    }



    public void gameActivity(){
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pop = new Intent(MainActivity.this, PopupActivity.class);
                play = (Button) findViewById(R.id.play_button);
                startActivity(pop);
            }
        });

    }

    public void goToGameHistory(View view) {
        Intent intent = new Intent(MainActivity.this, GameHistory.class);
        startActivity(intent);
    }

    public void goToSettings(View view) {
//        Cursor easterEggCursor = myDb.getEasterEggFoundData();
//        //isMoveToFirstTriggered = false;
//        if (easterEggCursor.moveToFirst()) {
//            isMoveToFirstTriggered = true;
//            if (easterEggCursor.getString(0) == null) {
//                name.clear();
//
//            } else if (easterEggCursor.getString(0).matches("true")) {
//                name.clear();
//                name.add(getResources().getString(R.string.default_name));
//                name.add(getResources().getString(R.string.donate));
////                name.add("\nTheme\n");
//                name.add("\nEaster Egg\n");
//            }
//
//        }
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }



//    public void startColorAnimation(){
//        while (mainActivityActive){
//            Toast.makeText(MainActivity.this, "onStart", Toast.LENGTH_SHORT).show();
//        }
//        play.setBackground(redToBlack);
//        redToBlack.startTransition(1000);
//        final CountDownTimer cdn = new CountDownTimer(1000, 250) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//                play.setBackground(blackToRed);
//                blackToRed.startTransition(1000);
//            }
//        };
//        cdn.start();
//    }

//    public void openLeaderboard(View view) {
//        if (isConnected) {
//            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
//                    getResources().getString(R.string.leaderboard_least_tries_)), 12345);
//        } else {
//            Button btn = (Button) findViewById(R.id.leaderboard_button);
//            btn.setEnabled(false);
//            btn.setBackgroundColor(getResources().getColor(R.color.lightGray));
//            mGoogleApiClient.connect();
//        }
//    }

//    public void getUserName() {
//        SharedPreferences defaultNameSharedPrefs = getSharedPreferences("defaultName", Context.MODE_PRIVATE);
//        final String defaultNameSP = defaultNameSharedPrefs.getString("DEFAULT_NAME", "");
//        if (!defaultNameSP.matches("")) {
//            final String[] projection = new String[]
//                    {ContactsContract.Profile.DISPLAY_NAME};
//            String name = null;
//            final Uri dataUri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
//            final ContentResolver contentResolver = getContentResolver();
//            final Cursor c = contentResolver.query(dataUri, projection, null, null, null);
//
//            try {
//                assert c != null;
//                if (c.moveToFirst()) {
//                    name = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
//                    SharedPreferences.Editor defaultNameEditor = defaultNameSharedPrefs.edit();
//                    defaultNameEditor.putString("DEFAULT_NAME", name);
//                    defaultNameEditor.commit();
//                }
//            } finally {
//                assert c != null;
//                c.close();
//            }
//
//        }
//    }
}
