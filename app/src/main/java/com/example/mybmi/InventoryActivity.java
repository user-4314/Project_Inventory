package com.example.mybmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.navigation.NavigationView;

public class InventoryActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    String textway,time,Name,Place;
    String Accountstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initViews();
        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        time = formatter.format(curDate);

        setListenserqrcode();

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);
    }
    private Button button_inventory;
    private Spinner str_way;
    private Spinner str_place;

    private void initViews() {
        button_inventory = (Button) findViewById(R.id.inventoryokButton);
        str_way = (Spinner) findViewById(R.id.inventory_way);
        str_place = (Spinner) findViewById(R.id.inventory_place);
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
            Place = str_place.getSelectedItem().toString();


            if (textway.equals("QR code")) {
                    Intent intent = new Intent();
                    intent.setClass(InventoryActivity.this, QRcodeAcitivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_time", time);
                    bundle.putString("KEY_place", Place);
                    bundle.putString("KEY_userinfo", Accountstr);
                    intent.putExtras(bundle);
                    startActivity(intent);
            } else {
                    Intent intent = new Intent();
                    intent.setClass(InventoryActivity.this, RFIDActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_place", Place);
                    bundle.putString("KEY_time", time);
                    bundle.putString("KEY_userinfo", Accountstr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
    };

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.menuManagement:
                Toast.makeText(InventoryActivity.this, "已位於此頁面", Toast.LENGTH_LONG).show();
                break;

            case R.id.menuHome:
                intent.setClass(InventoryActivity.this,InitialActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menuNFC:
                intent.setClass(InventoryActivity.this,NFCScanActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menuQRcode:
                intent.setClass(InventoryActivity.this,QRcodeScanActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
