package com.example.bses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    }
}
