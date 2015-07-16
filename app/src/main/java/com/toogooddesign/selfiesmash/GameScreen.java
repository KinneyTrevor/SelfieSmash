package com.toogooddesign.selfiesmash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import java.util.Random;

public class GameScreen extends Activity {
    private int userScore;
    TextView timerText;
    TextView scoreText;
    int timerValue = 30000;
    final Context context = this;
    CountDownTimer clockTimer;
    CountDownTimer coinTimer;
    boolean isRunning = false;
    Drawable x;
    String value;
    Vibrator vibrate;
    Boolean vibrateEnabled;
    Boolean soundEnabled;
    Boolean ispaused;
    MediaPlayer mySound, coinSound, bombSound, splatSound,lostlife;
    SharedPreferences mypreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ispaused = false;

        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        scoreText = (TextView) findViewById(R.id.player1score);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/scoreFont.otf");
        scoreText.setTypeface(typeFace);
        ImageButton charButton = (ImageButton) findViewById(R.id.goodIcon);
        x = getResources().getDrawable(R.drawable.playerIcon,null);
        charButton.setImageDrawable(x);
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        soundEnabled = mypreferences.getBoolean("sound", true);
        vibrateEnabled = mypreferences.getBoolean("vibrateenabled", true);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        CountDownTimer createCoin = new CountDownTimer(600000, 5000) { //change me back 30,000/750 to make time reasonable
            public void onTick(long millisUntilFinished) {
                if (timerValue > 0) {
                    createCoin();
                    //moveCoin();
                }
            }

            public void onFinish() {
            }
        }.start();
        //If this got started from activity_camera.java grab the photo that was passed with it
        if (getIntent().hasExtra("image")) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getString("image");
            }
        }

        createCountDown(timerValue);
        createBadTimer(timerValue);
        timerText = (TextView) findViewById(R.id.timerText);
        ImageButton stupidButton = (ImageButton) findViewById(R.id.coinButton);
        //TIMER FOR MOVING THE BUTTON AUTOMATICALLY
        CountDownTimer z = new CountDownTimer(60000, 650) { //change me back 30,000/750 to make time reasonable
            public void onTick(long millisUntilFinished) {
                if (timerValue > 0) {
                    moveButton();
                    //moveCoin();
                }
            }

            public void onFinish() {
            }
        }.start();
    }
    //This is the main time count down at the top of the screen
    public void createCountDown(int timerVal){
        clockTimer = new CountDownTimer(timerVal, 1000) {
            public void onTick(long millisUntilFinished) {
                //if(!ispaused) {
                timerText.setText("" + millisUntilFinished / 1000);
                String timerTextVal = timerText.getText().toString();
                timerValue = (Integer.parseInt(timerTextVal)) * 1000;
                //  }
            }

            public void onFinish() {
                timerText.setText("" + 0);
                updateHS(userScore);
            }
        }.start();
    }

    public void createBadTimer(int timerVal){
        clockTimer = new CountDownTimer(timerVal, 2500) {
            public void onTick(long millisUntilFinished) {
                badCreate();
            }
            public void onFinish() {
            }
        }.start();
    }//1
    public void badCreate(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        ImageButton altbutton = (ImageButton) findViewById(R.id.badIcon);
        altbutton.setVisibility(View.VISIBLE);
        altbutton.setClickable(true);
        Random r2 = new Random();
        int Button2H = r2.nextInt(width - 400);
        int Button2W = r2.nextInt(height - 400);
        altbutton.setX(Button2H);
        altbutton.setY(Button2W);
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                destroyBad();
            }
        }.start();
    }
    //Creates the coin
    public void createCoin() {
        if (isRunning) { coinTimer.cancel();}
        ImageButton coinButton = (ImageButton) findViewById(R.id.coinButton);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        Random r3 = new Random();
        int Button3H = r3.nextInt(width - 400);
        int Button3W = r3.nextInt(height - 400);
        coinButton.setX(Button3H);
        coinButton.setY(Button3W);
        coinButton.setVisibility(View.VISIBLE);
        coinButton.setClickable(true);
        coinTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                isRunning = true;
            }
            public void onFinish() {
                destroyCoin();
            }
        }.start();
    }

    //Called at the end of the game, grabs the highscore and compares it to the current highscore, if bigger it updates, if less just goes to quit
    public void updateHS(int x) {
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        int currentHS = mypreferences.getInt("highscore", 0);
        if (userScore > currentHS) {
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putInt("highscore", x);
            editor.commit();
            Toast.makeText(getApplicationContext(), "New High Score!", Toast.LENGTH_LONG).show();
            endGame();
        } else {
            endGame();
        }
    }

    //Increments user score.
    public void btnClick(View v) {
        Drawable z = getResources().getDrawable(R.mipmap.splat, null);
        ImageButton charButton = (ImageButton) findViewById(R.id.goodIcon);
        charButton.setImageDrawable(z);
        scoreText = (TextView) findViewById(R.id.player1score);
        userScore++;
        scoreText.setText(String.valueOf(userScore));
    }

    public void destroyBad(){

        ImageButton altbutton = (ImageButton) findViewById(R.id.badIcon);
        altbutton.setVisibility(View.GONE);
        altbutton.setClickable(false);
    }

    public void destroyCoin(){
        ImageButton coinButton = (ImageButton) findViewById(R.id.badIcon);
        coinButton.setVisibility(View.GONE);
        coinButton.setClickable(false);
    }

    //Lowers score, time, flashes red
    public void badClick(View v){
        if(vibrateEnabled) {
            Vibrator asdf = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrate.vibrate(400);
        }
        ImageButton altbutton = (ImageButton) findViewById(R.id.badIcon);
        altbutton.setVisibility(View.GONE);
        altbutton.setClickable(false);
        userScore=userScore-3;
        scoreText = (TextView) findViewById(R.id.player1score);
        scoreText.setText(String.valueOf(userScore));
        timerValue = timerValue-2000;
    }
    public void coinClick(View v){
        ImageButton coin = (ImageButton) findViewById(R.id.coinButton);
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        int coinAmt = mypreferences.getInt("coinCount", 0);
        coinAmt++;
        SharedPreferences.Editor editor = mypreferences.edit();
        editor.putInt("coinCount", coinAmt);
        editor.commit();
        coin.setVisibility(View.GONE);
    }
    //Moves the actual button
    public void moveButton() {
        if (!ispaused){
            ImageButton charButton = (ImageButton) findViewById(R.id.goodIcon);
            RelativeLayout gameLayout = (RelativeLayout) findViewById(R.id.gameLayout);
            int width  = gameLayout.getWidth();
            int height = gameLayout.getHeight();
            charButton.setImageDrawable(x);
            Random buttonPlace = new Random();
            int buttonY = buttonPlace.nextInt(height-100)+100;
            int buttonX = buttonPlace.nextInt(width-50)+50;
            charButton.setX(buttonX);
            charButton.setY(buttonY);
        }

    }

    //Called if user presses menu button or back button
    public void onPause(){
        super.onPause();
        ispaused = true;
        if (clockTimer != null) {
            clockTimer.cancel();
        }

        if (mySound != null) {
            mySound.stop();
            if (isFinishing()) {
                mySound.stop();
                mySound.release();
            }
        }
        pauseDialog();
    }

    public void pauseClick(View a) {
        super.onPause();
        clockTimer.cancel();
        if (clockTimer != null) {
            clockTimer.cancel();
        }
        ispaused = true;
        pauseDialog();
    }

    public void pauseDialog(){
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        ImageButton yesButton = (ImageButton) dialog.findViewById(R.id.btn_yes);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog cdDialog = new Dialog(context);

                cdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //cdDialog.getWindow().setBackgroundDrawable(new ColorDrawableResource(R.color.transparent));
                cdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cdDialog.setContentView(R.layout.pausecountdown);
                cdDialog.show();
                cdDialog.setCancelable(false);
                CountDownTimer pauseCountDown = new CountDownTimer(4000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        TextView ayyy = (TextView) cdDialog.findViewById(R.id.cdlabel);
                        ayyy.setText("" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        cdDialog.dismiss();
                    }
                }.start();

                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    //Allows user to restart the game or go to main menu
    public void endGame(){
        clockTimer.cancel();
        timerValue = 0;
        updateHS(userScore);
        mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        int ayylmao = mypreferences.getInt("coinCount", 0);
        int hs = mypreferences.getInt("highscore",userScore);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.endgamedialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView gameover = (TextView) dialog.findViewById(R.id.gameover);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/gameover.ttf");
        gameover.setTypeface(typeFace);
        TextView scoreText = (TextView) dialog.findViewById(R.id.scoreText);
        TextView hsText = (TextView) dialog.findViewById(R.id.highscoreText);
        TextView coinText = (TextView) dialog.findViewById(R.id.coinsText);
        coinText.setText(""+ayylmao);
        scoreText.setText(""+userScore);
        hsText.setText("" + hs);

        ImageButton yesButton = (ImageButton) dialog.findViewById(R.id.btn_yes);
        ImageButton noButton  = (ImageButton) dialog.findViewById(R.id.btn_no);
        ImageButton shareButton = (ImageButton) dialog.findViewById(R.id.btn_share);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hs = new Intent(getApplicationContext(), GameScreen.class);
                hs.putExtra("image", value);
                startActivity(hs);
                finish();
                dialog.dismiss();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check out my score on Face Smash!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Face Smash!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                finish();
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hs = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(hs);
                finish();
                dialog.cancel();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }
    public void onBackPressed() {
        Intent main = new Intent(getApplicationContext(), MainMenu.class);
        main.addCategory(Intent.CATEGORY_HOME);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
    }
}