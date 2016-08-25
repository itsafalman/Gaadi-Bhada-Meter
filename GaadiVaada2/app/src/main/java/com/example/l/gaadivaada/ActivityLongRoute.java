package com.example.l.gaadivaada;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLongRoute extends AppCompatActivity {
    private  DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_long_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mydb  = new DBHelper(getApplicationContext());
        Cursor rs = mydb.getStartingPoint();
        rs.moveToFirst();
        int value = rs.getCount();
        String[] countries = new String[value];
       for(int i=0; i<value;i++){
           countries[i] = rs.getString(rs.getColumnIndex(DBHelper.STARTING_PLACE));
           rs.moveToNext();
           //Toast.makeText(getApplicationContext(), countries[i], Toast.LENGTH_SHORT).show();
       }
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.initial_position);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);
        final AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.final_position);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().toString().equals("")){
                    //Toast.makeText(getApplicationContext(), "text called", Toast.LENGTH_SHORT).show();
                    Cursor myrs = mydb.getData();
                    myrs.moveToFirst();
                    int count = myrs.getCount();
                    String[] fpoint = new String[count];
                    for(int i=0; i<count;i++){
                        fpoint[i] = myrs.getString(myrs.getColumnIndex(DBHelper.FINAL_DESTINATION));
                        myrs.moveToNext();
                        //Toast.makeText(getApplicationContext(), countries[i], Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, fpoint);
                    textView1.setAdapter(adapter);
                }
                else{
                   // Toast.makeText(getApplicationContext(), "text called 1", Toast.LENGTH_SHORT).show();
                    String spoint = textView.getText().toString();
                    Cursor myrs = mydb.getDestination(spoint);
                    myrs.moveToFirst();
                    int count = myrs.getCount();
                    String[] fpoint = new String[count];
                    for(int i=0; i<count;i++){
                        fpoint[i] = myrs.getString(myrs.getColumnIndex(DBHelper.FINAL_DESTINATION));
                        myrs.moveToNext();
                        //Toast.makeText(getApplicationContext(), countries[i], Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, fpoint);
                    textView1.setAdapter(adapter);

                }
            }
        });
        TextView getfare = (TextView) findViewById(R.id.calculatefare);
        final TextView distance = (TextView) findViewById(R.id.distancemeter);
        final TextView price = (TextView) findViewById(R.id.vaadameter);
        getfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "please enter the initial position", Toast.LENGTH_SHORT).show();
                }
                else if(textView1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter the final position", Toast.LENGTH_SHORT).show();
                }
                else{
                    String starting_point = textView.getText().toString();
                    String final_point = textView1.getText().toString();
                    Cursor rs = mydb.getFare(starting_point,final_point);
                    rs.moveToFirst();
                    float dist = rs.getFloat(rs.getColumnIndex(DBHelper.TOTAL_DISTANCE));
                    float fare = rs.getFloat(rs.getColumnIndex(DBHelper.TOTAL_PRICE));
                    distance.setText(dist + "km" );
                    price.setText("Rs."+fare);
                }
            }
        });
    }
}
