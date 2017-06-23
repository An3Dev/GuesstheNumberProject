package an3applications.guessthenumber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static an3applications.guessthenumber.PopupActivity.difficultyText;
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

        userInputNumber = (EditText) findViewById(R.id.user_input_number);
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
                }
                if (userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(false);

                }
                if (userInputNumber.getText().toString().matches("0")) {
                    guessButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        TextView guessResponder = (TextView) findViewById(R.id.guess_responder);
        guessResponder.setText("Choose a number between\n1 and " + maxNum);

        myDb = new SQLDatabaseHelper(this);
        success = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        userInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!userInputNumber.getText().toString().matches("0") & !userInputNumber.getText().toString().matches("")) {
                    guessButton.setEnabled(true);
                    guessButton.setAllCaps(false);
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
    }


    public void makeNumberAnimation() {
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
                Cursor c = myDb.getDefaultNameData();
                c.moveToFirst();
                if (c.getCount() != 0) {
                    defaultName = c.getString(0);
                }
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivityGame.this, R.style.AlertDialogCustom));
                builder.setTitle("Name");
                if (defaultName == null) {
                    builder.setMessage("You haven't set up your default name\nAfter submitting your name go to Settings/Default name.");
                    builder.setCancelable(false);
                    builder.setNeutralButton("Type name", new DialogInterface.OnClickListener() {
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
                if (defaultName != null) {
                    builder.setMessage("Are you " + defaultName + "?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Submit default name, go home
                            boolean isInserted = myDb.insertData(defaultName, triesTaken, difficultyText, success);
                            if (isInserted) {
                                Toast.makeText(MainActivityGame.this, "Your name was submitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivityGame.this, "Error, your name wasn't submitted", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(MainActivityGame.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                    guessResponder.setText(userInputInt + " is too low");
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    guessResponder.startAnimation(fadeIn);
                    fadeIn.setFillAfter(true);
                    fadeIn.setDuration(2000);
                    triesTaken += 1;
                    remainingTries -= 1;
                    remainingTriesText.setText("Remaining tries: " + remainingTries);
                    guessButton.setAllCaps(false);
                }
                if (userInputInt > randNum) {
                    guessResponder.setText("");
                    guessResponder.setText(userInputInt + " is too high");
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    guessResponder.startAnimation(fadeIn);
                    fadeIn.setFillAfter(true);
                    fadeIn.setDuration(2000);
                    triesTaken += 1;
                    remainingTries -= 1;
                    remainingTriesText.setText("Remaining tries: " + remainingTries);
                    guessButton.setAllCaps(false);
                }

                //User guesses the number
                if (userInputInt == randNum) {
                    success = 1;
                    triesTaken += 1;
                    remainingTries -= 1;
                    // Check if no view has focus:
                    View keyboard = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    remainingTriesText.setVisibility(View.GONE);
                    guessResponder.setText("");
                    guessResponder.setText("Congratulations! You guessed the number in " + triesTaken + " tries.\n\nThe number WAS " + randNum + "!");
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
                            Cursor c = myDb.getDefaultNameData();
                            c.moveToFirst();
                            if (c.getCount() != 0) {
                                defaultName = c.getString(0);
                            }
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(MainActivityGame.this, R.style.AlertDialogCustom);
                            builder.setTitle("Name");
                            if (defaultName == null) {
                                builder.setMessage("You haven't set up your default name. After submitting your name go to Settings/Default name.");
                                builder.setCancelable(false);
                                builder.setNeutralButton("Type in name", new DialogInterface.OnClickListener() {
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
                            if (defaultName != null) {
                                builder.setMessage("Are you " + defaultName + "?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Submit default name, go home
                                        boolean isInserted = myDb.insertData(defaultName, triesTaken, difficultyText, success);
                                        if (isInserted) {
                                            Toast.makeText(MainActivityGame.this, "Your name was submitted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivityGame.this, "Error, your name wasn't submitted", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent(MainActivityGame.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
            guessButton.setAllCaps(false);
            submitName = (Button) findViewById(R.id.submitName);
            userName = (EditText) findViewById(R.id.user_name);
            remainingTriesText.setVisibility(View.GONE);
            userInputNumber.setVisibility(View.GONE);
            guessButton.setVisibility(View.GONE);
            guessResponder.setText("");
            guessResponder.setText("Sorry, I'm afraid you didn't\nGuess the Number\nThe number was...");
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

    public void next(View view){
        submitName = (Button) findViewById(R.id.submitName);
        userName = (EditText) findViewById(R.id.user_name);
        if (userName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityGame.this, "There's never been a name \" \", lets keep it that way. Try again. ", Toast.LENGTH_LONG).show();
        }
        if (userName.getText().toString().length() <= 8 & userName.getText().toString().length() > 0) {
            boolean isInserted = myDb.insertData(userName.getText().toString(), triesTaken, difficultyText, success);
            if (isInserted) {
                Toast.makeText(MainActivityGame.this, "Your name was submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivityGame.this, "Error, your name wasn't submitted", Toast.LENGTH_SHORT).show();
            }
            submitName.setAllCaps(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } if (userName.getText().toString().length() > 8) {
            Toast.makeText(MainActivityGame.this, "Your name is too long. It has to be under 8 characters.", Toast.LENGTH_LONG).show();
        }
    }


}