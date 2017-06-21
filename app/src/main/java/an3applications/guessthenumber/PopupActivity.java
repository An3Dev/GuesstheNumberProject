package an3applications.guessthenumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {
    static String difficultyText;
    RadioButton easy;
    RadioButton medium;
    RadioButton hard;
    RadioButton veryHard;
    RadioButton impossible;
    TextView guessResponder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int) (width * .8), (int) (height * .6));


    }

    public void launchMainActivityGame(View view) {
        Intent intent = new Intent(this, MainActivityGame.class);
        easy = (RadioButton) findViewById(R.id.easy);
        medium = (RadioButton) findViewById(R.id.medium);
        hard = (RadioButton) findViewById(R.id.hard);
        veryHard = (RadioButton) findViewById(R.id.veryHard);
        impossible = (RadioButton) findViewById(R.id.impossible);
        guessResponder = (TextView) findViewById(R.id.guess_responder);
        if(easy.isChecked()) {
            MainActivityGame.maxNum = 100;
            difficultyText = "Easy";
        }
        if(medium.isChecked()) {
            MainActivityGame.maxNum = 200;
            difficultyText = "Medium";
        }
        if(hard.isChecked()) {
            MainActivityGame.maxNum = 500;
            difficultyText = "Hard";
        }
        if(veryHard.isChecked()) {
            MainActivityGame.maxNum = 1000;
            difficultyText = "Very Hard";
        }
        if(impossible.isChecked()) {
            MainActivityGame.maxNum = 1000000;
            difficultyText = "Impossible";
        }
        Button difficulty = (Button) findViewById(R.id.ok_button_difficulty);
        difficulty.setAllCaps(true);
        startActivity(intent);
    }
}
