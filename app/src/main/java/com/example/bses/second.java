package com.example.bses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class second extends AppCompatActivity {

    EditText customerid;
    ImageButton enterbtn;
    Button continuebtn;
    LinearLayout cidlayout;
    GridView customergrid;
    Integer[] icon = {R.drawable.ic_person_red, R.drawable.ic_phone_red, R.drawable.ic_location, R.drawable.ic_lightbulb};
    String[] data = {"ABCD", "9876543210","A-19, abc street, xyz, India","30 kwh"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        customerid = findViewById(R.id.customerid);
        enterbtn = findViewById(R.id.enterbtn);
        continuebtn = findViewById(R.id.continuebtn);

        customergrid = findViewById(R.id.customer_grid);
        CustomAdapterGrid customAdapter = new CustomAdapterGrid(getApplicationContext(), icon,data);
        customergrid.setAdapter(customAdapter);

        customergrid.setVisibility(View.INVISIBLE);
        continuebtn.setVisibility(View.INVISIBLE);

        enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(customerid.getText().toString().isEmpty()) {
                    Toast.makeText(second.this, "CA Number cannot be empty !!", Toast.LENGTH_SHORT).show();
                }
                else if(customerid.getText().toString().equals("1")){

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    cidlayout = findViewById(R.id.cidlayout);
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
                    cidlayout.startAnimation(animation1);
                    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                    customergrid.startAnimation(animation2);
                    Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                    continuebtn.startAnimation(animation3);

                }
                else{
                    Toast.makeText(second.this, "Enter valid CA Number !!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meter = new Intent(second.this, meter.class);
                meter.putExtra("CA",customerid.getText().toString());
                startActivity(meter);
            }
        });
        /*customergrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id){
                if(pos == 0){
                    Intent i = new Intent(second.this, Homepage.class);
                    startActivity(i);
                }
                if(pos == 1){
                    Intent i = new Intent(second.this, test.class);
                    startActivity(i);
                }
                if(pos == 2){
                    Intent i = new Intent(second.this, Homepage.class);
                    startActivity(i);
                }
                if(pos == 3){
                    Intent i = new Intent(second.this, test.class);
                    startActivity(i);
                }
                if(pos == 4){
                    Intent i = new Intent(second.this, test.class);
                    startActivity(i);
                }

            }
        });*/

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
