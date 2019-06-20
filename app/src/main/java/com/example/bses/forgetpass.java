package com.example.bses;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class forgetpass extends AppCompatActivity {

    private EditText phoneno;
    private Button submitbtn;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        phoneno = findViewById(R.id.phoneno);
        submitbtn = findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                //msg = String.format("%04d", random.nextInt(10000));

                Intent intent=new Intent(getApplicationContext(),forgetpass.class);
                PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage("9205005951",null,"OTP: ",pi,null);
                Toast.makeText(forgetpass.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();


                /*
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(phoneno.getText().toString(),null,"OTP: "+msg,null,null);
                    Toast.makeText(forgetpass.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(forgetpass.this, "OTP Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
                */

            }
        });


    }
}
