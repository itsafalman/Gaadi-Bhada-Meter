package com.example.l.gaadivaada;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class RouteSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_summary);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        float distance_covered = getIntent().getExtras().getFloat("distance");
        float total_charge = getIntent().getExtras().getFloat("charge");

        TextView distanceText = (TextView) findViewById(R.id.dist);

        TextView chargeText = (TextView) findViewById(R.id.chargetext);

        distanceText.setText(new DecimalFormat("##.##").format(distance_covered)+ "KM");
        chargeText.setText("Rs." + new DecimalFormat("##.##").format(total_charge));
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
