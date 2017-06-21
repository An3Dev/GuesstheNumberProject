package an3applications.guessthenumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SQLDatabaseHelper myDb;
    Button play;
    Button allGames;
    Button settingsBtn;

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
        //Always include myDb = new SQLDatabaseHelper(this);. It gave me problems for hours for not having it.
        myDb = new SQLDatabaseHelper(this);
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

    public void gameActivity(View v){
        Intent pop = new Intent(MainActivity.this, PopupActivity.class);
        play = (Button) findViewById(R.id.play_button);
        play.setAllCaps(true);
        startActivity(pop);
    }

    public void goToGameHistory(View view) {
        allGames.setAllCaps(true);
        Intent intent = new Intent(MainActivity.this, GameHistory.class);
        startActivity(intent);
    }

    public void goToSettings(View view) {
        settingsBtn.setAllCaps(true);
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
