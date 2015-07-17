package com.toogooddesign.selfiesmash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class GameScreen extends Activity {
    private int userScore;
    TextView timerText;
    TextView scoreText;
    int timerValue = 900000;
    final Context context = this;
    CountDownTimer moveTimer;
    CountDownTimer mCountDownTimer;
    CountDownTimer lifeTimer;
    CountDownTimer destroyBomb;
    ImageButton charButton;
    int levelCount;
    int timefactor = 1500 ;
    int lifeCount = 3;
    Bitmap photo;
    boolean isRunning = false;
    boolean gameOver;
    Drawable x;
    ImageView heart;
    Boolean soundEnabled;
    Boolean vibrateEnabled;
    Boolean musicEnabled;
    MediaPlayer mySound, coinSound, bombSound, splatSound,lostlife;
    ShareActionProvider mShareActionProvider;
    String value;
    Vibrator vibrate;

    SharedPreferences mypreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameOver = false;
        AdView mAdView = (AdView) findViewById(R.id.adView);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        charButton = (ImageButton) findViewById(R.id.goodIcon);
        x = getResources().getDrawable(R.mipmap.playericon);
        charButton.setImageDrawable(x);
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        soundEnabled = mypreferences.getBoolean("sound", true);
        vibrateEnabled = mypreferences.getBoolean("vibrateenabled", true);
        musicEnabled = mypreferences.getBoolean("musicenabled",true);

        if(musicEnabled) {
            mySound = MediaPlayer.create(this, R.raw.gamesound2);
            mySound.setLooping(true);
            mySound.start();
        }

        //If this got started from camera activity grab the string that was passed with it
        if (getIntent().hasExtra("image")) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                photo = (Bitmap) extras.get("data");
                charButton = (ImageButton) findViewById(R.id.goodIcon);
                charButton.setImageBitmap(photo);
            }
        }
        createBadTimer(timerValue);
        //sets the countdown to track lives
        lifeTimer = new CountDownTimer(timefactor,1){
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                loseLife();
            }
        }.start();
        //TIMER FOR MOVING THE BUTTON AUTOMATICALLY
        moveTimer = new CountDownTimer(1260000, timefactor) {
            public void onTick(long millisUntilFinished) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        moveButton();

                    }
                }, 1000);
            }
            public void onFinish() {
            }
        }.start();
        scoreText = (TextView) findViewById(R.id.player1score);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/scoreFont.otf");
        scoreText.setTypeface(typeFace);
    }

    public void loseLife(){
        if (!gameOver) {
            if (soundEnabled) {
                lostlife = MediaPlayer.create(this, R.raw.loselife);
                lostlife.start();
            }
            lifeCount--;
            if (lifeCount == 2) {

            }
            if (lifeCount == 1) {

            }
            if (lifeCount == 0) {
                endGame();
            } else {
                lifeTimer = new CountDownTimer(timefactor, 1) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        loseLife();
                    }
                }.start();
            }
        }
    }

    public void updateTimeFactor(){
        lifeTimer.cancel();
        lifeTimer = new CountDownTimer(timefactor,1){
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                loseLife();
            }
        }.start();
        moveTimer.cancel();
        moveTimer = new CountDownTimer(60000, timefactor) {
            public void onTick(long millisUntilFinished) {
                if (timerValue > 0) {
                    moveButton();
                }
            }
            public void onFinish() {
            }
        }.start();
    }

    public void createBadTimer(int timerVal){
        mCountDownTimer = new CountDownTimer(timerVal, 2500) {
            public void onTick(long millisUntilFinished) {
                badCreate();
            }
            public void onFinish() {
            }
        }.start();
    }

    public void badCreate(){
        if (destroyBomb != null){
            destroyBomb.cancel();
        }
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
        destroyBomb = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                destroyBad();
            }
        }.start();
    }


    public void updateHS(int x) {
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        int currentHS = mypreferences.getInt("highscore", 0);
        if (userScore > currentHS) {
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putInt("highscore", x);
            editor.apply();
        }
    }

    //Increments user score.
    public void btnClick(View v) {
        if(soundEnabled) {
            splatSound = MediaPlayer.create(this, R.raw.altsplateffect);
            splatSound.start();
            splatSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
        Drawable z = getResources().getDrawable(R.mipmap.splat);
        ImageButton charButton = (ImageButton) findViewById(R.id.goodIcon);
        charButton.setImageDrawable(z);
        scoreText = (TextView) findViewById(R.id.player1score);
        userScore++;
        scoreText.setText(String.valueOf(userScore));
        if (timefactor >= 700) {timefactor =  timefactor - 10;}
        if (timefactor <700){ timefactor = timefactor - 5; }
        updateTimeFactor();
    }


    public void destroyBad(){
        ImageButton altbutton = (ImageButton) findViewById(R.id.badIcon);
        altbutton.setVisibility(View.GONE);
        altbutton.setClickable(false);
    }

    //Lowers score, time, flashes red
    public void badClick(View v){
        if(vibrateEnabled) {
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

    //Moves the actual button
    public void moveButton() {
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

    //Called if user presses menu button or back button
    public void onPause(){
        super.onPause();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (lifeTimer != null){
            lifeTimer.cancel();
        }
        if (mySound != null) {
            mySound.stop();
            if (isFinishing()) {
                mySound.stop();
                mySound.release();
            }
        }
        if (!gameOver) {
            pauseDialog();
        }
    }


    public void pauseClick(View a) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        moveTimer.cancel();
        lifeTimer.cancel();

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
        //getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
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
        gameOver = true;
        mCountDownTimer.cancel();
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
        scoreText.setText(""+userScore);
        hsText.setText(""+hs);

        if (mySound != null) {
            mySound.stop();
            if (isFinishing()) {
                mySound.stop();
                mySound.release();
            }
        }

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
                //Intent hs = new Intent(getApplicationContext(), MainMenu.class);
                startActivityForResult(sharingIntent,1);

                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hs = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(hs);
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void onActivityResult(int request, int result, Intent intent)
    {
        if (1 == request && result == Activity.RESULT_OK)
        {
            Intent main = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(main);
        }
    }

}
