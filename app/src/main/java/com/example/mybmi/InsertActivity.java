package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InsertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        initViews();
        setListensers();
    }
    private Button button_insert;
    private EditText str_itemnumber;
    private EditText str_itemsubnumber;
    private EditText str_itemname;
    private Spinner str_unit;
    private EditText str_unitnumber;

    private void initViews() {
        button_insert = (Button) findViewById(R.id.insertbutton);
        str_itemnumber = (EditText) findViewById(R.id.insert_itemnumber);
        str_itemsubnumber = (EditText) findViewById(R.id.insert_itemsubnumber);
        str_itemname = (EditText) findViewById(R.id.insert_itemname);
        str_unit = (Spinner) findViewById(R.id.insert_place);
        str_unitnumber = (EditText) findViewById(R.id.insert_unitnumber);
    }
    private void setListensers()
    {
        button_insert.setOnClickListener(gotoInt);
    }
    private View.OnClickListener gotoInt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InsertActivity.this,InitialActivity.class);
            startActivity(intent);
        }
    };
}