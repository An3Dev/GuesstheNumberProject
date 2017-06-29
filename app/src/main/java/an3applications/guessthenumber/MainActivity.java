package an3applications.guessthenumber;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    Button play;
    Button allGames;
    Button settingsBtn;
    long isFinished;
    static CountDownTimer withinSeconds;
    static List<String> name = new ArrayList<String>();
    static boolean isMoveToFirstTriggered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)

        setContentView(R.layout.activity_main);


        allGames = (Button) findViewById(R.id.all_games);
        settingsBtn = (Button) findViewById(R.id.settings_button);
        play = (Button) findViewById(R.id.play_button);

        withinSeconds = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isFinished = millisUntilFinished;
            }

            @Override
            public void onFinish() {

            }
        };

        //Always include myDb = new SQLDatabaseHelper(this);. It gave me problems for hours for not having it.
        myDb = new SQLDatabaseHelper(this);
        gameActivity();
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



    public void gameActivity(){
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pop = new Intent(MainActivity.this, PopupActivity.class);
                play = (Button) findViewById(R.id.play_button);
                play.setAllCaps(true);
                startActivity(pop);
            }
        });

//        play.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch(motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        withinSeconds.start();
//
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        withinSeconds.cancel();
//                        if(isFinished < 750){
//                            Toast.makeText(MainActivity.this, "Easter Egg unlocked!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, EEActivity.class);
//                            startActivity(intent);
//                        }
//
//                        if(isFinished > 750){
//
//                            return true;
//                        }
//
//                }
//                return false;
//            }
//        });
    }

    public void goToGameHistory(View view) {
        allGames.setAllCaps(true);
        Intent intent = new Intent(MainActivity.this, GameHistory.class);
        startActivity(intent);
    }

    public void goToSettings(View view) {
        Cursor easterEggCursor = myDb.getIsEasterEggFound();
        isMoveToFirstTriggered = false;
        if (easterEggCursor.moveToFirst()) {
            isMoveToFirstTriggered = true;
            Toast.makeText(MainActivity.this, "easter egg cursor moved", Toast.LENGTH_SHORT).show();
            final boolean isEasterEggFound = easterEggCursor.getInt(0) == 1;
            if (easterEggCursor.getInt(0) != 1) {
                Toast.makeText(MainActivity.this, "Easter egg isn't found", Toast.LENGTH_SHORT).show();
                name.clear();

            } else if (isEasterEggFound) {
                Toast.makeText(MainActivity.this, "Easter egg was found", Toast.LENGTH_SHORT).show();
                name.clear();
                name.add("\nDefault name\n");
                name.add("\nDonate\n");
                name.add("\nEaster Egg\n");
            }
            //Toast.makeText(MainActivity.this, "" + isEasterEggFound, Toast.LENGTH_SHORT).show();
//        }else if(!easterEggCursor.moveToFirst()){
//            Toast.makeText(MainActivity.this, "else if run", Toast.LENGTH_SHORT).show();
//            if (easterEggCursor.getInt(0) != 1) {
//                Toast.makeText(MainActivity.this, "Easter egg cursor isn't found", Toast.LENGTH_SHORT).show();
//                name.clear();
//                name.add("\nDefault name\n");
//                name.add("\nDonate\n");
//            }
//        }

        }
        settingsBtn.setAllCaps(true);
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}

