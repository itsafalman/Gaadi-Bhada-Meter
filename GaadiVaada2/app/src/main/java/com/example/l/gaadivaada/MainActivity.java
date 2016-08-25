package com.example.l.gaadivaada;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public RadioGroup radioSexGroup;
    public Button okButton;
    public Button reportPolice;
    public RadioButton radioSexButton;

    //@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//            Toast.makeText(getApplicationContext(),"Please select the type of vehicle above", Toast.LENGTH_LONG).show();
//            // do what you want with the power button
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        okButton = (Button) findViewById(R.id.submit_button);
        reportPolice = (Button) findViewById(R.id.policereport);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
               if(selectedId == R.id.microbus){
                   Intent i = new Intent(getApplicationContext(),MicroBus_Activity.class);
                   startActivity(i);
               }
               else if(selectedId == R.id.taxi ){
                   Intent i = new Intent(getApplicationContext(),TaxiActivity.class);
                   startActivity(i);
               }
               else if(selectedId == R.id.nightbus){
                   Intent i = new Intent(getApplicationContext(),ActivityLongRoute.class);
                   startActivity(i);
               }
               else {
                    Toast.makeText(getApplicationContext(),"Please select the type of vehicle above", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reportPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "112233";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                try {
                    startActivity(callIntent);
                } catch(SecurityException e){

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
