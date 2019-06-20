package com.example.bses;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class meter extends AppCompatActivity {

    private ImageButton btnkwh, btnkvah;
    private ImageView imgkwh, imgkvah;
    private EditText meterno, kwh, kvah;
    private String pathtofile;
    private Button savebutton;
    private GpsTracker gpsTracker;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        meterno = findViewById(R.id.meterno);
        kwh = findViewById(R.id.kwh);
        kvah = findViewById(R.id.kvah);
        btnkwh = findViewById(R.id.btnkwh);
        btnkvah = findViewById(R.id.btnkvah);
        imgkwh = findViewById(R.id.imgkwh);
        imgkvah = findViewById(R.id.imgkvah);
        savebutton = findViewById(R.id.savebutton);

        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        btnkwh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                flag = 1;
                if(intent.resolveActivity(getPackageManager()) != null){
                    File photofile = null;
                    photofile = createphotofile();
                    if(photofile!=null){
                        pathtofile = photofile.getAbsolutePath();
                        Uri photouri = FileProvider.getUriForFile(meter.this, "com.example.bses.fileprovider", photofile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });
        btnkvah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                flag = 2;
                if(intent.resolveActivity(getPackageManager()) != null){
                    File photofile = null;
                    photofile = createphotofile();
                    if(photofile!=null){
                        pathtofile = photofile.getAbsolutePath();
                        Uri photouri = FileProvider.getUriForFile(meter.this, "com.example.bses.fileprovider", photofile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meterno.getText().toString().isEmpty() || kwh.getText().toString().isEmpty() || kvah.getText().toString().isEmpty() || imgkvah.getDrawable()== null || imgkwh.getDrawable() == null){
                    Toast.makeText(meter.this, "Fields cannot be empty !!", Toast.LENGTH_SHORT).show();
                }
                else{
                    gpsTracker = new GpsTracker(meter.this);
                    if(gpsTracker.canGetLocation()){
                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                        Toast.makeText(meter.this, "LATITUDE : "+String.valueOf(latitude)+"\nLONGITUDE : "+ String.valueOf(longitude), Toast.LENGTH_LONG).show();
                    }else{
                        gpsTracker.showSettingsAlert();
                    }
                }
            }
        });
    }
    private File createphotofile(){
        String name = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name, ".jpg",storageDir);
        }
        catch(IOException e){
            Log.d("mylog","Excep: "+e.toString());
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathtofile);
                Intent CA = getIntent();
                String wm = CA.getStringExtra("CA");


                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = tf.format(c);


                Point p = new Point();
                p.set(100, 400);
                Bitmap b= waterMark(bitmap,"CA number: " + wm ,"Date: "+ formattedDate, "Time: " + formattedTime ,p, Color.BLACK,150,230,false);
                if(flag == 1)
                    imgkwh.setImageBitmap(Bitmap.createScaledBitmap(b, 800, 700, false));
                if(flag == 2){
                    imgkvah.setImageBitmap(Bitmap.createScaledBitmap(b, 800, 700, false));
                }
            }
        }
    }
    public  Bitmap waterMark(Bitmap src, String CAnum,String dt, String time, Point location, int color, int alpha, int size, boolean underline) {
        //get source image width and height
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        //create canvas object
        Canvas canvas = new Canvas(result);
        //draw bitmap on canvas
        canvas.drawBitmap(src, 0, 0, null);
        //create paint object
        Paint paint = new Paint();
        //apply color
        paint.setColor(color);
        //set transparency
        paint.setAlpha(alpha);
        //set text size
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        //set should be underlined or not
        paint.setUnderlineText(underline);
        //draw text on given location
        canvas.drawText(CAnum, location.x, location.y, paint);
        canvas.drawText(dt, location.x, 650, paint);
        canvas.drawText(time, location.x, 900, paint);

        return result;
    }



}








