package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class InitialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        initViews();
        setListensInventory();
        setListensInsert();
        setListensDelete();
    }
    private Button button_inventory;
    private Button button_insert;
    private Button button_delete;
    private void initViews() {
        button_inventory = (Button) findViewById(R.id.buttoninventory);
        button_insert = (Button) findViewById(R.id.buttoninsert);
        button_delete = (Button) findViewById(R.id.buttondelete);
    }
    private void setListensInventory()
    {
        button_inventory.setOnClickListener(gotoinven);
    }
    private View.OnClickListener gotoinven = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InitialActivity.this,InventoryActivity .class);
            startActivity(intent);
        }
    };
    private void setListensInsert()
    {
        button_insert.setOnClickListener(gotoinsert);
    }
    private View.OnClickListener gotoinsert = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InitialActivity.this,InsertActivity .class);
            startActivity(intent);
        }
    };
    private void setListensDelete()
    {
        button_delete.setOnClickListener(gotodel);
    }
    private View.OnClickListener gotodel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InitialActivity.this,DeleteActivity .class);
            startActivity(intent);
        }
    };
}