package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DeleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        initViews();
        setListensers();
    }
    private Button button_delete;
    private EditText str_number;
    private Spinner str_unit;
    private void initViews() {
        button_delete = (Button) findViewById(R.id.deletebutton);
        str_number = (EditText) findViewById(R.id.delete_itemnumber);
        str_unit = (Spinner) findViewById(R.id.delete_place);

    }
    private void setListensers()
    {
        button_delete.setOnClickListener(gotoInt);
    }
    private View.OnClickListener gotoInt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(DeleteActivity.this,InitialActivity.class);
            startActivity(intent);
        }
    };
}