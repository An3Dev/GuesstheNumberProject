package an3enterprises.guessthenumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


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
        easy = (RadioButton) findViewById(R.id.easy);
        medium = (RadioButton) findViewById(R.id.medium);
        hard = (RadioButton) findViewById(R.id.hard);
        veryHard = (RadioButton) findViewById(R.id.veryHard);
        impossible = (RadioButton) findViewById(R.id.impossible);
        guessResponder = (TextView) findViewById(R.id.guess_responder);
        if(easy.isChecked()) {
            MainActivityGame.maxNum = 100;
            difficultyText = getResources().getString(R.string.easy_difficulty);
            Intent intent = new Intent(this, MainActivityGame.class);
            startActivity(intent);
            finish();
        }
        if(medium.isChecked()) {
            MainActivityGame.maxNum = 200;
            difficultyText = getResources().getString(R.string.medium_difficulty);;
            Intent intent = new Intent(this, MainActivityGame.class);
            startActivity(intent);
            finish();
        }
        if(hard.isChecked()) {
            MainActivityGame.maxNum = 500;
            difficultyText = getResources().getString(R.string.hard_difficulty);;
            Intent intent = new Intent(this, MainActivityGame.class);
            startActivity(intent);
            finish();
        }
        if(veryHard.isChecked()) {
            MainActivityGame.maxNum = 1000;
            difficultyText = getResources().getString(R.string.very_hard_difficulty);;
            Intent intent = new Intent(this, MainActivityGame.class);
            startActivity(intent);
            finish();
        }
        if(impossible.isChecked()) {
            MainActivityGame.maxNum = 1000000;
            difficultyText = getResources().getString(R.string.impossible_difficulty);;
            Intent intent = new Intent(this, MainActivityGame.class);
            startActivity(intent);
            finish();
        }
        if (!easy.isChecked() & !medium.isChecked() & !hard.isChecked() & !veryHard.isChecked() & !impossible.isChecked()) {
            Toast.makeText(PopupActivity.this, getResources().getString(R.string.have_to_choose_difficulty), Toast.LENGTH_SHORT).show();
        }

    }
}
