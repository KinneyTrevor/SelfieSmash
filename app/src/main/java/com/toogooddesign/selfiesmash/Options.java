package com.toogooddesign.selfiesmash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Options extends Activity {
    boolean soundEnabled;
    boolean hasVibrate;
    boolean vibrateactive;
    boolean musicEnabled;
    SharedPreferences mypreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        soundEnabled = mypreferences.getBoolean("sound", true);
        vibrateactive = mypreferences.getBoolean("vibrateenabled",true);
        musicEnabled = mypreferences.getBoolean("musicenabled",true);
        hasVibrate = mypreferences.getBoolean("hasvibrate",true);
        if (hasVibrate){
            ImageButton vibrateButton = (ImageButton) findViewById(R.id.vibrateButton);
            vibrateButton.setClickable(true);
            if (vibrateactive){
                Drawable z = getResources().getDrawable(R.drawable.vibrateon);
                vibrateButton.setImageDrawable(z);
            }
            else {
                Drawable z = getResources().getDrawable(R.drawable.vibrateoff);
                vibrateButton.setImageDrawable(z);
            }
        }
        if (soundEnabled){
            Drawable z = getResources().getDrawable(R.drawable.soundon);
            ImageButton speakerButton = (ImageButton) findViewById(R.id.speaker);
            speakerButton.setImageDrawable(z);
        }
        else{
            Drawable z = getResources().getDrawable(R.drawable.soundoff);
            ImageButton speakerButton = (ImageButton) findViewById(R.id.speaker);
            speakerButton.setImageDrawable(z);
        }
        if (musicEnabled){
            Drawable z = getResources().getDrawable(R.drawable.musicon);
            ImageButton musicButton = (ImageButton) findViewById(R.id.musicButton);
            musicButton.setImageDrawable(z);
        }
        else{
            Drawable z = getResources().getDrawable(R.drawable.musicoff);
            ImageButton musicButton = (ImageButton) findViewById(R.id.musicButton);
            musicButton.setImageDrawable(z);
        }
    }


    public void toggleSound(View v){
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        soundEnabled = mypreferences.getBoolean("sound", true);
        if (soundEnabled){
            Drawable z = getResources().getDrawable(R.drawable.soundoff);
            ImageButton speakerButton = (ImageButton) findViewById(R.id.speaker);
            speakerButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Sound Disabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("sound", false);
            editor.apply();
        }
        if(!soundEnabled){
            Drawable z = getResources().getDrawable(R.drawable.soundon);
            ImageButton speakerButton = (ImageButton) findViewById(R.id.speaker);
            speakerButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Sound Enabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("sound", true);
            editor.apply();
        }
    }
    public void toggleVibrate(View v){
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        vibrateactive = mypreferences.getBoolean("vibrateenabled",true);
        if (vibrateactive){
            Drawable z = getResources().getDrawable(R.drawable.vibrateoff);
            ImageButton vibrateButton = (ImageButton) findViewById(R.id.vibrateButton);
            vibrateButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Vibrate Disabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("vibrateenabled", false);
            editor.apply();
        }
        if(!vibrateactive){
            Drawable z = getResources().getDrawable(R.drawable.vibrateon);
            ImageButton vibrateButton = (ImageButton) findViewById(R.id.vibrateButton);
            vibrateButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Vibrate Enabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("vibrateenabled", true);
            editor.apply();
        }
    }

    public void toggleMusic(View y){
        SharedPreferences mypreferences = getSharedPreferences("App_preferences_file", Context.MODE_PRIVATE);
        musicEnabled = mypreferences.getBoolean("musicenabled",true);
        if (musicEnabled){
            Drawable z = getResources().getDrawable(R.drawable.musicoff);
            ImageButton musicButton = (ImageButton) findViewById(R.id.musicButton);
            musicButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Music Disabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("musicenabled", false);
            editor.apply();
        }
        if(!musicEnabled){
            Drawable z = getResources().getDrawable(R.drawable.musicon);
            ImageButton musicButton = (ImageButton) findViewById(R.id.musicButton);
            musicButton.setImageDrawable(z);
            Toast.makeText(getApplicationContext(), "Music Enabled!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = mypreferences.edit();
            editor.putBoolean("musicenabled", true);
            editor.apply();
        }
    }

    public void backClick(View a){
        Intent main = new Intent (getApplicationContext(), MainMenu.class);
        startActivity(main);
    }

}