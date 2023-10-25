package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class IteminfoActivity extends Activity {
    String itemnumber,itemname,itemplace,place,Accountstr,name,ampm,mode;
    double year ,month,day,hour,minute;
    int rent;//租借狀態
    int reused;//耗材非耗材
    String result;
    String maintain_date,actualmaintain_date,discard1,rent1,reuseable1,CheckStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminfo);
        initViews();

        DecimalFormat nf = new DecimalFormat("0");
        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");
        itemnumber = bundle.getString("KEY_itemnumber");
        itemname = bundle.getString("KEY_itemname");
        itemplace = bundle.getString("KEY_scanplace");
        place = bundle.getString("KEY_place");
        ampm = bundle.getString("KEY_ampm");
        mode = bundle.getString("KEY_mode");
        year = Double.parseDouble(bundle.getString("KEY_year"));
        month = Double.parseDouble(bundle.getString("KEY_month"));
        day = Double.parseDouble(bundle.getString("KEY_day"));
        hour = Double.parseDouble(bundle.getString("KEY_hour"));
        minute = Double.parseDouble(bundle.getString("KEY_minute"));
        name = bundle.getString("KEY_name");
        actualmaintain_date = bundle.getString("KEY_getActualmatainDate");
        discard1 = bundle.getString("KEY_getDiscard");
        rent1 = bundle.getString("KEY_getRent");
        reuseable1 = bundle.getString("KEY_getReuseable");
        CheckStates = bundle.getString("KEY_getCheckStates");

        rent = Integer.parseInt(rent1);
        reused = Integer.parseInt(reuseable1);


        context.setText("資產代號:"+itemnumber+"\n"+"資產名稱:"+itemname+"\n"+"使用單位:"+itemplace+"\n");

        if(reuseable1.equals("0")){
            context.append("是否耗材:否"+"\n");
        } else{
            context.append("是否耗材:是"+"\n");
        }
        if(rent1.equals("0")){
            context.append("是否被租借/使用:否"+"\n");
        } else{
            context.append("是否被租借/使用:是"+"\n");
        }
        if(discard1.equals("0")){
            context.append("是否已被報銷:否"+"\n");
        } else{
            context.append("是否已被報銷:是"+"\n");
        }
        if(CheckStates.equals("0")){
            context.append("盤點狀態:未盤點"+"\n");
        } else{
            context.append("盤點狀態:已盤點"+"\n");
        }


        setListenseroperate();
        setInventory();
        setDiscard();
    }

    private Button button_scan;
    private Button button_finish;
    private Button button_inventory;
    private Button button_discard;
    private TextView context;

    private void initViews() {
        button_scan = (Button) findViewById(R.id.scan);
        button_finish = (Button) findViewById(R.id.finish);
        button_inventory = (Button) findViewById(R.id.inventory);
        button_discard = (Button) findViewById(R.id.discard);
        context = (TextView) findViewById(R.id.itemcontext);
    }

    private void setListenseroperate()
    {
        button_scan.setOnClickListener(gotoscan);
        button_finish.setOnClickListener(gotofinish);
    }
    private  void setInventory(){
        button_inventory.setOnClickListener(gotoinventory);
    }
    private  void setDiscard(){
        button_discard.setOnClickListener(gotodiscard);
    }

    private View.OnClickListener gotoscan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mode.equals("0")) {
                Intent intent = new Intent();
                intent.setClass(IteminfoActivity.this, QRcodeAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", ampm);
                bundle.putString("KEY_place", place);
                bundle.putString("KEY_year", String.valueOf(year));
                bundle.putString("KEY_month", String.valueOf(month));
                bundle.putString("KEY_day", String.valueOf(day));
                bundle.putString("KEY_hour", String.valueOf(hour));
                bundle.putString("KEY_minute", String.valueOf(minute));
                bundle.putString("KEY_name", name);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent();
                intent.setClass(IteminfoActivity.this, RFIDActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", ampm);
                bundle.putString("KEY_place", place);
                bundle.putString("KEY_year", String.valueOf(year));
                bundle.putString("KEY_month", String.valueOf(month));
                bundle.putString("KEY_day", String.valueOf(day));
                bundle.putString("KEY_hour", String.valueOf(hour));
                bundle.putString("KEY_minute", String.valueOf(minute));
                bundle.putString("KEY_name", name);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener gotofinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(IteminfoActivity.this, FinishActivtiy.class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_ampm", ampm);
            bundle.putString("KEY_place", place);
            bundle.putString("KEY_year", String.valueOf(year));
            bundle.putString("KEY_month", String.valueOf(month));
            bundle.putString("KEY_day", String.valueOf(day));
            bundle.putString("KEY_hour", String.valueOf(hour));
            bundle.putString("KEY_minute", String.valueOf(minute));
            bundle.putString("KEY_name", name);
            bundle.putString("KEY_userinfo", Accountstr);
            bundle.putString("KEY_mode", mode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private View.OnClickListener gotoinventory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(place.equals(itemplace)) {
                //盤點insert php

                if (((rent == 1 && reused == 0) || (rent == 0 && reused == 0) || (rent == 0 && reused == 1))) {
                    String url = "http://140.136.151.67/harCheckThing.php";//要修改！！！！
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.trim();

                            if (result.equals("成功")) {
                                Toast.makeText(IteminfoActivity.this, "盤點成功", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent();
                            //intent.setClass(LogingActivity.this, InitialActivity.class);
                            //Toast.makeText(LogingActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("KEY_userinfo", Accountstr);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
                            } else  {
                                Toast.makeText(IteminfoActivity.this, "成功(盤點)", Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(IteminfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("LoginName", itemname);//和php的參數做連結
                            params.put("LoginPassword", itemnumber);
                            return params;

                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(IteminfoActivity.this);
                    requestQueue.add(stringRequest);
                }
                else if (rent == 1 && reused == 1){
                    Toast.makeText(IteminfoActivity.this, "應該使用報廢請確認後決定", Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(IteminfoActivity.this, "此資產不屬於此使用單位", Toast.LENGTH_LONG).show();
            }

        }
    };


    private View.OnClickListener gotodiscard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(place.equals(itemplace)) {
                //報銷 delete php
                if(rent ==1 && reused ==1) {
                    String url = "http://140.136.151.67/harScrappedThing.php";//要修改！！！！

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.trim();
                            if (result.equals("成功")) {
                                Toast.makeText(IteminfoActivity.this, "報廢成功", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent();
                            //intent.setClass(LogingActivity.this, InitialActivity.class);
                            //Toast.makeText(LogingActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("KEY_userinfo", Accountstr);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
                            } else {
                                Toast.makeText(IteminfoActivity.this, "成功(報廢)", Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(IteminfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("LoginName", itemname);//和php的參數做連結
                            params.put("LoginPassword", itemnumber);
                            return params;

                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(IteminfoActivity.this);
                    requestQueue.add(stringRequest);
                }
                else {
                    Toast.makeText(IteminfoActivity.this, "應該使用盤點請確認後決定", Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(IteminfoActivity.this, "此資產不屬於此使用單位", Toast.LENGTH_LONG).show();
            }

        }
    };
}