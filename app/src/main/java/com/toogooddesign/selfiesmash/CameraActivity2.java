package com.toogooddesign.selfiesmash;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;

public class CameraActivity2 extends Activity {
    static final int request_image_capture = 1;
    Bitmap photo;
    BitmapDrawable finalPhoto;
    final Context context = this;
   // public ImageView theImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activity2);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, request_image_capture);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == request_image_capture && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            BitmapDrawable bdrawable = new BitmapDrawable(photo);
            ImageButton theImage = (ImageButton) findViewById(R.id.theimage);
            //theImage = (ImageView) findViewById(R.id.theimage);
            finalPhoto = bdrawable;
            theImage.setBackgroundDrawable(finalPhoto);

        }
    }
    public void ready(){
        Intent play = new Intent(this, GameScreen.class);
        //ByteArrayOutputStream bs = new ByteArrayOutputStream();
        //photo.compress(Bitmap.CompressFormat.PNG, 50, bs);
        play.putExtra("picture", photo);
        startActivity(play);
    }
    public void readyUp(View z){
        Intent i = new Intent(getApplicationContext(), GameScreen.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 50, bs);
        i.putExtra("image", bs.toByteArray());
        startActivity(i);
    }
}