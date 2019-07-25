package com.example.bses;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Homepage extends AppCompatActivity{

    String reply;

    private Button loginbtn;
    private TextView forget;
    private EditText userid, password;
    private int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

       userid = findViewById(R.id.userid);
       password = findViewById(R.id.password);
       loginbtn = findViewById(R.id.loginbtn);
       forget = findViewById(R.id.forget);

       loginbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //loginBtn(view);

               //validate(userid.getText().toString(), password.getText().toString());
               Intent second = new Intent(Homepage.this, second.class);
               startActivity(second);

           }
       });
       forget.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               forget.setPaintFlags(forget.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
               Intent forgetpass = new Intent(Homepage.this, forgetpass.class);
               startActivity(forgetpass);
               //forget.setPaintFlags(forget.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
           }
       });

    }
    private void validate(String userid, String password){
        if(userid.isEmpty() || password.isEmpty()){
            Toast.makeText(Homepage.this, "Fields cannot be empty !!", Toast.LENGTH_SHORT).show();
        }
        else if (userid.equals("admin") && password.equals("1234")){
            Intent i = new Intent(Homepage.this, second.class);
            startActivity(i);
        }
        else {
            Toast.makeText(Homepage.this, "Incorrect username or password !!", Toast.LENGTH_SHORT).show();
            counter--;
            if(counter == 0){
                loginbtn.setEnabled(false);
                Toast.makeText(Homepage.this, "No more Attempts!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void loginBtn(View view){
        String user = userid.getText().toString();
        String pass = password.getText().toString();
        background bg = new background(this,Homepage.this);
        bg.execute(user,pass);
     }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}
