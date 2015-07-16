package com.toogooddesign.selfiesmash;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class CameraActivity2 extends Activity {
    static final int request_image_capture = 1;
    Bitmap photo;
    final Context context = this;
    public ImageView theImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activity2);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, request_image_capture);
    }
    public void launchCamera(View x){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, request_image_capture);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == request_image_capture && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            theImage = (ImageView) findViewById(R.id.theimage);
            theImage.setImageBitmap(photo);
            Intent i = new Intent(this, GameScreen.class);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 50, bs);
            i.putExtra("byteArray", bs.toByteArray());
            startActivity(i);
        }
    }

    public void readyUp(View z){
        Intent i = new Intent(this, GameScreen.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 50, bs);
        i.putExtra("byteArray", bs.toByteArray());
        startActivity(i);
    }
}