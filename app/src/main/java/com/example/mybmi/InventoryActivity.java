package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class InventoryActivity extends Activity {
    String textway;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initViews();
        setListenserqrcode();
    }
    private Button button_inventory;
    private EditText num_year;
    private EditText num_month;
    private EditText num_day;
    private EditText num_hour;
    private EditText num_minute;
    private EditText str_name;
    private Spinner str_way;
    private Spinner str_place;
    private Spinner str_ampm;
    private void initViews() {
        button_inventory = (Button) findViewById(R.id.inventoryokbutton);
        num_year = (EditText) findViewById(R.id.inventory_year);
        num_month = (EditText) findViewById(R.id.inventory_month);
        num_day = (EditText) findViewById(R.id.inventory_day);
        num_hour = (EditText) findViewById(R.id.inventory_hour);
        num_minute = (EditText) findViewById(R.id.inventory_minute);
        str_name = (EditText) findViewById(R.id.inventory_name);
        str_way = (Spinner) findViewById(R.id.inventory_way);
        str_place = (Spinner) findViewById(R.id.inventory_place);
        str_ampm = (Spinner) findViewById(R.id.inventory_ampm);
        str_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                textway = parent.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    private void setListenserqrcode()
    {
        button_inventory.setOnClickListener(gotoqrcode);
    }
    private View.OnClickListener gotoqrcode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(textway.equals("QR code")) {
                Intent intent = new Intent();
                intent.setClass(InventoryActivity.this, QRcodeAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", str_ampm.getSelectedItem().toString());
                bundle.putString("KEY_place", str_place.getSelectedItem().toString());
                bundle.putString("KEY_year", num_year.getText().toString());
                bundle.putString("KEY_month", num_month.getText().toString());
                bundle.putString("KEY_day", num_day.getText().toString());
                bundle.putString("KEY_hour", num_hour.getText().toString());
                bundle.putString("KEY_minute", num_minute.getText().toString());
                bundle.putString("KEY_name", str_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent();
                intent.setClass(InventoryActivity.this, RFIDActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", str_ampm.getSelectedItem().toString());
                bundle.putString("KEY_place", str_place.getSelectedItem().toString());
                bundle.putString("KEY_year", num_year.getText().toString());
                bundle.putString("KEY_month", num_month.getText().toString());
                bundle.putString("KEY_day", num_day.getText().toString());
                bundle.putString("KEY_hour", num_hour.getText().toString());
                bundle.putString("KEY_minute", num_minute.getText().toString());
                bundle.putString("KEY_name", str_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
}