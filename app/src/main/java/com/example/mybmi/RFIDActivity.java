package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class RFIDActivity extends Activity {
    String name,ampm;
    double year ,month,day,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidactivity);
        initViews();
        try
        {
            DecimalFormat nf = new DecimalFormat("0");
            Bundle bundle = this.getIntent().getExtras();

            name = bundle.getString("KEY_name");
            ampm = bundle.getString("KEY_ampm");
            year = Double.parseDouble(bundle.getString("KEY_year"));
            month = Double.parseDouble(bundle.getString("KEY_month"));
            day = Double.parseDouble(bundle.getString("KEY_day"));
            hour = Double.parseDouble(bundle.getString("KEY_hour"));
            minute = Double.parseDouble(bundle.getString("KEY_minute"));

        }
        catch(Exception obj) {
            Toast.makeText(this, "要先輸入完整資料喔!", Toast.LENGTH_SHORT).show();
        }
    }
    private TextView inventoryway;
    private void initViews() {
        inventoryway = (TextView) findViewById(R.id.rfidtextView);
    }
}