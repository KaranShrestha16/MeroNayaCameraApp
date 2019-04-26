package com.meronayacameraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
 private ImageView imageView;
 private Button button_image;
 private Bitmap bitmapimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageview);
        button_image=findViewById(R.id.button_image);

        checkPermission();
        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCamera();
            }
        });
        saveImage();


    }



    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

    }

    private void loadCamera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap imageBitmap=(Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

        }
    }

    void saveImage(){
        File filename;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            Log.i("in save()", "after mkdir");
            new File(path + "/mvc/mvc").mkdir();
            filename = new File(path + "/mvc/mvc/var3.jpg");
            Log.i("in save()", "after file");
            FileOutputStream out = new FileOutputStream(filename);
            Log.i("in save()", "after outputstream");
            bitmapimage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i("in save()", "after outputstream closed");
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    filename.getAbsolutePath(), filename.getName(),
                    filename.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
