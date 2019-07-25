package com.example.bses;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private TextView latlon;
    private GpsTracker gpsTracker;
    private String path;
    private File dir;
    private File file;
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


        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BSES APP/PDF Files";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        gpsTracker = new GpsTracker(meter.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latlon = findViewById(R.id.latlon);
            latlon.setText("LATITUDE : "+latitude +"\nLONGITUDE : "+ longitude);
            //Toast.makeText(meter.this, "LATITUDE : "+String.valueOf(latitude)+"\nLONGITUDE : "+ String.valueOf(longitude), Toast.LENGTH_LONG).show();
        }else{
            gpsTracker.showSettingsAlert();
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
                    try{
                        createPDF();
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch (DocumentException e) {
                        e.printStackTrace();
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

    public void createPDF() throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String name= "Meter PDF " + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            //create table
            PdfPTable pt = new PdfPTable(2);
            pt.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = meter.this.getResources().getDrawable(R.mipmap.bses);

            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            Bitmap bitmap1 = ((BitmapDrawable)imgkwh.getDrawable()).getBitmap();
            Bitmap bitmap2 = ((BitmapDrawable)imgkvah.getDrawable()).getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);

            byte[] bitmapdata = stream.toByteArray();
            byte[] bitmapdata1 = stream1.toByteArray();
            byte[] bitmapdata2 = stream2.toByteArray();

            try {
                Image bgImage = Image.getInstance(bitmapdata);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("METER DETAILS\n"));
                cell.addElement(new Paragraph("\n Meter number: "+ meterno.getText().toString()));
                cell.addElement(new Paragraph("KWH reading: "+ kwh.getText().toString()+"\n"));
                Image bgImage1 = Image.getInstance(bitmapdata1);
                cell.addElement(bgImage1);

                cell.addElement(new Paragraph("\n KVAH reading: "+ kvah.getText().toString()+"\n"));
                Image bgImage2 = Image.getInstance(bitmapdata2);
                cell.addElement(bgImage2);

                cell.addElement(new Paragraph(latlon.getText().toString()));
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);
                doc.add(pt);

                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Intent pdfOpenintent = new Intent(this, second.class);

                /*File file = new File(Environment.getExternalStorageDirectory(), "/storage/emulated/0/BSES APP/PDF Files/"+name);
                Uri path = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/pdf");*/

                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), pdfOpenintent, 0);

                // Build notification
                // Actions are just fake
                Notification noti = new Notification.Builder(this).setContentTitle("PDF Downloaded Successfully ")
                        .setSmallIcon(R.mipmap.bses).setContentIntent(pIntent).addAction(R.drawable.ic_open_in_new, "Open", pIntent).build();
                // hide the notification after its selected
                notificationManager.notify(0, noti);

                Toast.makeText(getApplicationContext(), "PDF created...\n Downloaded at Home/BSES App/Pdf Files"  , Toast.LENGTH_LONG).show();

            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {   switch(item.getItemId()) {
        case R.id.logout:
            Intent i = new Intent(this, Homepage.class);
            startActivity(i);
    }
        return(super.onOptionsItemSelected(item));
    }
}








