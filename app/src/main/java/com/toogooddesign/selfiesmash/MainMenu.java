package com.toogooddesign.selfiesmash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        TextView myTextView=(TextView)findViewById(R.id.titleView);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/another.ttf");
        myTextView.setTypeface(typeFace);
        super.onCreate(savedInstanceState);
    }
    public void optionsClick(View ayy) {
        Intent options = new Intent(ayy.getContext(),Options.class);
        startActivity(options);
    }

    public void playButton(View view){

    }
    public void storeClick(View view){

    }

    public void highScores(View view){
        //Intent hs = new Intent(view.getContext(),HighScores.class);
        //  startActivity(hs);
    }



}