package an3enterprises.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.Random;

import static an3enterprises.guessthenumber.LoadingScreenActivity.isConnected;
import static an3enterprises.guessthenumber.LoadingScreenActivity.mGoogleApiClient;
import static an3enterprises.guessthenumber.PopupActivity.difficultyText;

public class MainActivityGame extends AppCompatActivity {

    static int maxNum;
    static int success;
    int triesTaken = 0;
    SQLDatabaseHelper myDb;
    Random rand = new Random();
    String defaultName;
    int randNum;
    int maxTries = 10;
    int remainingTries = maxTries;
    EditText userInputNumber;
    Button guessButton;
    EditText userName;
    Button submitName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main_game);
        if (isConnected) {
            Games.setViewForPopups(LoadingScreenActivity.mGoogleApiClient, findViewById(R.id.main_game));
        }
        userInputNumber = (EditText) findViewById(R.id.user_input_number);
        if (userInputNumber != null) {
            userInputNumber.setOnTouchListener(new OnSwipeTouchListener(MainActivityGame.this) {
                public void onSwipeTop() {

                }

                public void onSwipeRight() {
                }

                public void onSwipeLeft() {
                }

                public void onSwipeBottom() {
                    View view = MainActivityGame.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });
        }

        guessButton = (Button) findViewById(R.id.guess_button);

        randNum = rand.nextInt(maxNum) + 1;

        userInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!userInputNumber.getText().toString().matches("0") & !userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(true);

                    userInputNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if (userInputNumber.getText().toString().matches("") || userInputNumber.getText().toString().matches("0") || Integer.parseInt(userInputNumber.getText().toString()) > 1000000) {
                                return false;
                            }
                            getGuess(guessButton);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(userInputNumber, InputMethodManager.SHOW_IMPLICIT);
                            return false;
                        }
                    });

                }
                if (userInputNumber.getText().toString().matches("") || userInputNumber.getText().toString().matches("0") || Integer.parseInt(userInputNumber.getText().toString()) > 1000000 || Integer.parseInt(userInputNumber.getText().toString()) + 1 == 1) {
                    guessButton.setEnabled(false);
                    guessButton.setBackgroundColor(getResources().getColor(R.color.lightGray));
                }else{
                    guessButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        TextView guessResponder = (TextView) findViewById(R.id.guess_responder);
        guessResponder.setText(getResources().getString(R.string.guess_a_num) + " " + maxNum);

        myDb = new SQLDatabaseHelper(this);
        success = 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        final View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (userInputNumber != null) {
            userInputNumber.setOnTouchListener(new OnSwipeTouchListener(MainActivityGame.this) {
                public void onSwipeTop() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(userInputNumber, InputMethodManager.SHOW_IMPLICIT);
                }

                public void onSwipeRight() {
                }

                public void onSwipeLeft() {
                }

                public void onSwipeBottom() {
                    View view = MainActivityGame.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                }

            });
        }

        decorView.setSystemUiVisibility(uiOptions);

        userInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!userInputNumber.getText().toString().matches("0") & !userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(true);

                    userInputNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if (userInputNumber.getText().toString().matches("") || userInputNumber.getText().toString().matches("0") || Integer.parseInt(userInputNumber.getText().toString()) > 1000000) {
                                return false;
                            }
                            getGuess(guessButton);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(userInputNumber, InputMethodManager.SHOW_IMPLICIT);
                            return false;
                        }
                    });

                }
                if (userInputNumber.getText().toString().matches("") || userInputNumber.getText().toString().matches("0") || Integer.parseInt(userInputNumber.getText().toString()) > 1000000 || Integer.parseInt(userInputNumber.getText().toString()) + 1 == 1) {
                    guessButton.setEnabled(false);
                    guessButton.setBackgroundColor(getResources().getColor(R.color.lightGray));
                }else{
                    guessButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void makeNumberAnimation() {
        SharedPreferences defaultNameSharedPrefs = getSharedPreferences("defaultName", Context.MODE_PRIVATE);
        final String defaultNameSP = defaultNameSharedPrefs.getString("DEFAULT_NAME", "");
        //Button homeBtn = (Button) findViewById(R.id.home_btn);
        TextView finalNumber = (TextView) findViewById(R.id.final_number);
        AlphaAnimation fadeInNum = new AlphaAnimation(0.0f, 2.0f);
        finalNumber.setVisibility(View.VISIBLE);
        finalNumber.setText("");
        finalNumber.setText("" + randNum);
        fadeInNum.setStartOffset(5500);
        fadeInNum.setDuration(500);
        finalNumber.startAnimation(fadeInNum);
        fadeInNum.setFillAfter(true);
        Handler Handler = new Handler();
        Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Cursor c = myDb.getDefaultNameData();
//                c.moveToFirst();
//                if (c.getCount() != 0) {
//                    Toast.makeText(MainActivityGame.this, "Default name found", Toast.LENGTH_SHORT).show();
//                    defaultName = c.getString(0);
//                }
                if (defaultNameSP == ""){
                    Toast.makeText(MainActivityGame.this, getResources().getString(R.string.default_name_not_detected), Toast.LENGTH_SHORT).show();
                    defaultNameNotDetected();
                }
                if (defaultNameSP != "") {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivityGame.this, R.style.AlertDialogCustom));
                    builder.setTitle(getResources().getString(R.string.name_game_history));
                    builder.setMessage(getResources().getString(R.string.are_you)  + " "+ defaultNameSP + "?");
                    builder.setCancelable(false);
                    builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Submit default name, go home
                            boolean isInserted = myDb.insertData(defaultNameSP, triesTaken, difficultyText, success);
                            if (isInserted) {
                                Toast.makeText(MainActivityGame.this, getResources().getString(R.string.your_name_was_submitted), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivityGame.this, getResources().getString(R.string.error_name_not_submitted), Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(MainActivityGame.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userName.setVisibility(View.VISIBLE);
                            submitName.setVisibility(View.VISIBLE);
                            submitName.setEnabled(true);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        }, 5850);


    }

    public void getGuess(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //get edit text number guess, convert to string.
        userName = (EditText) findViewById(R.id.user_name);
        submitName = (Button) findViewById(R.id.submitName);
        final EditText userInputNumber = (EditText) findViewById(R.id.user_input_number);
        final Button guessButton = (Button) findViewById(R.id.guess_button);
        final TextView guessResponder = (TextView) findViewById(R.id.guess_responder);
        int userInputInt = Integer.parseInt(userInputNumber.getText().toString());
        TextView remainingTriesText = (TextView) findViewById(R.id.remaining_tries);
        //Button nextButton = (Button) findViewById(R.id.next_button);
        View decorView = getWindow().getDecorView();
        // Clear text
        userInputNumber.setText("");
        String userInputNumberString = userInputNumber.getText().toString();
        //cheat = (TextView) findViewById(R.id.cheat);
        //cheat.setText("" + randNum);
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        // Check if no view has focus:
//        View keyboard = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
        guessButton.setEnabled(false);
        userInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!userInputNumber.getText().toString().matches("0") & !userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(true);
                }
                if (userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(true);
                }
                if (userInputNumber.getText().toString().matches("0")) {
                    guessButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (triesTaken != maxTries) {
            if (!userInputNumberString.matches("") || !userInputNumberString.matches("0")) {
                if (userInputInt < randNum) {
                    guessResponder.setText("");
                    guessResponder.setText(userInputInt +  " " + getResources().getString(R.string.is_too_low));
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    guessResponder.startAnimation(fadeIn);
                    fadeIn.setFillAfter(true);
                    fadeIn.setDuration(2000);
                    triesTaken += 1;
                    remainingTries -= 1;
                    remainingTriesText.setText(getResources().getString(R.string.remaining_tries_java) +  " " + remainingTries);
                    guessButton.setAllCaps(false);
                }
                if (userInputInt > randNum) {
                    guessResponder.setText("");
                    guessResponder.setText(userInputInt + " " + getResources().getString(R.string.is_too_high));
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    guessResponder.startAnimation(fadeIn);
                    fadeIn.setFillAfter(true);
                    fadeIn.setDuration(2000);
                    triesTaken += 1;
                    remainingTries -= 1;
                    remainingTriesText.setText(getResources().getString(R.string.remaining_tries_java) + " " + remainingTries);
                    guessButton.setAllCaps(false);
                }

                //User guesses the number
                if (userInputInt == randNum) {
                    if (difficultyText.matches("Impossible")) {
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_master));
                    }
//                    InputMethodSession.EventCallback eventCallback = new InputMethodSession.EventCallback() {
//                        @Override
//                        public void finishedEvent(int i, boolean b) {
//
//                        }
//                    };
                    Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_least_tries_), triesTaken);
                    SharedPreferences defaultNameSharedPrefs = getSharedPreferences("defaultName", Context.MODE_PRIVATE);
                    final String defaultNameSP = defaultNameSharedPrefs.getString("DEFAULT_NAME", "");
                    if (isConnected) {
                        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_newbie), 1);
                        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_beginner), 1);
                        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_skilled), 1);
                        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_experienced), 1);
                        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_expert), 1);
                        submitEvent(getResources().getString(R.string.event_won_game));
                    }

                    success = 1;
                    triesTaken += 1;
                    remainingTries -= 1;
                    // Check if no view has focus:
                    View keyboard = this.getCurrentFocus();
                    if (view != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    remainingTriesText.setVisibility(View.GONE);
                    guessResponder.setText("");
                    guessResponder.setText(getResources().getString(R.string.congrats_main_game_p1) + " " + triesTaken + " " + getResources().getString(R.string.congrats_main_game_p2) + " " + randNum + "!");
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    fadeIn.setDuration(2000);
                    guessResponder.startAnimation(fadeIn);
                    fadeIn.setFillAfter(true);
                    guessButton.setVisibility(View.GONE);
                    //nextButton.setVisibility(View.VISIBLE);
                    userInputNumber.setVisibility(View.GONE);
                    guessButton.setAllCaps(false);
                    remainingTriesText.setVisibility(View.GONE);
                    Handler Handler = new Handler();
                    Handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (defaultNameSP == ""){
                                Toast.makeText(MainActivityGame.this, getResources().getString(R.string.default_name_not_detected), Toast.LENGTH_SHORT).show();
                                defaultNameNotDetected();
                            }
                            if (defaultNameSP != "") {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivityGame.this, R.style.AlertDialogCustom));
                                builder.setTitle(getResources().getString(R.string.name_game_history));
                                builder.setMessage(getResources().getString(R.string.are_you) + " " + defaultNameSP + "?");
                                builder.setCancelable(false);
                                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Submit default name, go home
                                        boolean isInserted = myDb.insertData(defaultNameSP, triesTaken, difficultyText, success);
                                        if (isInserted) {
                                            Toast.makeText(MainActivityGame.this, getResources().getString(R.string.your_name_was_submitted), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivityGame.this, getResources().getString(R.string.error_name_not_submitted), Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent(MainActivityGame.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        userName.setVisibility(View.VISIBLE);
                                        submitName.setVisibility(View.VISIBLE);
                                        submitName.setEnabled(true);
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                        }
                    }, 3500);

                }
            }

        }
        // Tries run out, user doesn't guess the number

        if (triesTaken == maxTries & success == 0) {
            submitEvent(getResources().getString(R.string.event_lost_game));
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


            if (isConnected) {
                Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_least_tries_), triesTaken);
            }
            //Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_most_games_played_), );
            guessButton.setAllCaps(false);
            submitName = (Button) findViewById(R.id.submitName);
            userName = (EditText) findViewById(R.id.user_name);
            remainingTriesText.setVisibility(View.GONE);
            userInputNumber.setVisibility(View.GONE);
            guessButton.setVisibility(View.GONE);
            guessResponder.setText("");
            guessResponder.setText(getResources().getString(R.string.sorry_i_am_afraid));
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            guessResponder.startAnimation(fadeIn);
            guessResponder.startAnimation(fadeOut);
            fadeIn.setDuration(2000);
            fadeIn.setFillAfter(true);
            fadeOut.setDuration(5000);
            fadeOut.setFillAfter(true);
            makeNumberAnimation();
        }
    }
    // Make code below able to be translated.
    public void next(View view){
        submitName = (Button) findViewById(R.id.submitName);
        userName = (EditText) findViewById(R.id.user_name);
        if (userName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityGame.this, getResources().getString(R.string.never_been_a_name), Toast.LENGTH_LONG).show();
        }
        if (userName.getText().toString().length() <= 10 & userName.getText().toString().length() > 0) {
            boolean isInserted = myDb.insertData(userName.getText().toString(), triesTaken, difficultyText, success);
            if (isInserted) {
                Toast.makeText(MainActivityGame.this, getResources().getString(R.string.your_name_was_submitted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivityGame.this, getResources().getString(R.string.error_name_not_submitted), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(MainActivityGame.this, MainActivity.class);
            startActivity(intent);
            finish();
        } if (userName.getText().toString().length() > 10) {
            Toast.makeText(MainActivityGame.this, getResources().getString(R.string.name_too_long), Toast.LENGTH_LONG).show();
        }
    }

    public void defaultNameNotDetected() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivityGame.this, R.style.AlertDialogCustom));
        builder.setTitle(getResources().getString(R.string.name_game_history));
        if (defaultName == null) {
            builder.setMessage(getResources().getString(R.string.forgot_default_name));
            builder.setCancelable(false);
            builder.setNeutralButton(R.string.type_name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userName.setVisibility(View.VISIBLE);
                    submitName.setVisibility(View.VISIBLE);
                    submitName.setEnabled(true);
                    dialogInterface.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    }

    public void submitEvent(String eventId) {
        // eventId is taken from the developer console
        String myEventId = eventId;

        // increment the event counter
        Games.Events.increment(mGoogleApiClient, myEventId, 1);
    }

//    class EventCallback implements ResultCallback {
//
//        public void onResult(com.google.android.gms.common.api.Result result) {
//            Events.LoadEventsResult r = (Events.LoadEventsResult)result;
//            com.google.android.gms.games.event.EventBuffer eb = r.getEvents();
//
//            for (int i=0; i < eb.getCount(); i++) {
//                Toast.makeText(MainActivityGame.this, result.toString(), Toast.LENGTH_SHORT).show();
//            }
//            eb.close();
//        }
//    }

}