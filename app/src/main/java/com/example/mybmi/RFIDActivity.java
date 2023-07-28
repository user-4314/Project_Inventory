package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.slice.SliceItem;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RFIDActivity extends Activity {
    String name,ampm,place,nfcitemnumber,nfcitemname,nfcplace,appearance,maintain,function;;
    double year ,month,day,hour,minute;
    String Accountstr;
    String result;
    NfcAdapter nfcAdapter;
    Tag myTag;
    PendingIntent pendingIntent;
    IntentFilter readFilters[];

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
            place = bundle.getString("KEY_place");
            year = Double.parseDouble(bundle.getString("KEY_year"));
            month = Double.parseDouble(bundle.getString("KEY_month"));
            day = Double.parseDouble(bundle.getString("KEY_day"));
            hour = Double.parseDouble(bundle.getString("KEY_hour"));
            minute = Double.parseDouble(bundle.getString("KEY_minute"));
            Accountstr = bundle.getString("KEY_userinfo");

        }
        catch(Exception obj) {
            Toast.makeText(this, "要先輸入完整資料喔!", Toast.LENGTH_SHORT).show();
        }
        inventoryway.setText(place);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter ==null){
            Toast.makeText(RFIDActivity.this, "此裝置無NFC掃描功能", Toast.LENGTH_SHORT).show();
            finish();//不給轉跳頁直接結束
        }

        readFromIntent(getIntent());

        setListenseroperate();
    }
    private TextView inventoryway;
    private Button button_sure;
    private Button button_finish;
    private TextView content;
    private Spinner str_appearance;
    private Spinner str_maintain;
    private Spinner str_function;
    private void initViews() {
        inventoryway = (TextView) findViewById(R.id.rfidtextView);
        button_sure = (Button) findViewById(R.id.nfcsure);
        button_finish = (Button) findViewById(R.id.nfcfinish);
        content = (TextView) findViewById(R.id.NFCcontext);
        str_appearance = (Spinner) findViewById(R.id.nfcappearance);
        str_maintain = (Spinner) findViewById(R.id.nfcmaintain);
        str_function = (Spinner) findViewById(R.id.nfcfunction);
    }

    private  void enableRead(){
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,pendingIntent,readFilters,null);
    }
    private  void disableRead(){
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        enableRead();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableRead();
    }*/

   @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       setIntent(intent);
       readFromIntent(intent);
       if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
           myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
       }
    }
    //NFC reader 函式
    private void readFromIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsqs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msqs = null;
            if(rawMsqs != null){
                msqs = new NdefMessage[rawMsqs.length];
                for(int i=0;i<rawMsqs.length;i++){
                    msqs[i] = (NdefMessage) rawMsqs[i];
                }
            }
            buildTagViews(msqs);
        }
    }
    private void buildTagViews(NdefMessage[] msqs){
        if(msqs == null || msqs.length == 0) return;;
        String text = "";
        byte[] payload = msqs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] &128) == 0) ? "UTF-8" : "UTF-16";
        int codeLength = payload[0] & 0063;

        try{
            text = new String(payload,codeLength+1,payload.length-codeLength-1,textEncoding);
        }catch (UnsupportedEncodingException e){
            Log.e("輸入字串無法編碼",e.toString());
        }
        stringSpilt(text);//這邊之後要字串切割
    }

    public void stringSpilt(String args){
        String[] arrSplit = args.split("\n");
        for (int i=0; i < arrSplit.length; i++)
            System.out.println(arrSplit[i]);
        if(arrSplit.length>=3) {
            nfcitemnumber = arrSplit[0];
            nfcitemname = arrSplit[1];
            nfcplace = arrSplit[2];
            content.setText("資產代號:"+nfcitemnumber+"\n"+"資產名稱:"+nfcitemname+"\n"+"使用單位:"+nfcplace);
        }
        else {
            nfcitemnumber = "";
            nfcitemname = "";
            nfcplace = "";
            content.setText("資產代號:暫無資料"+"\n"+"資產名稱:暫無資料"+"\n"+"使用單位:暫無資料");
            Toast.makeText(RFIDActivity.this, "此NFC tag非盤點標籤", Toast.LENGTH_SHORT).show();
        }

    }

    private void setListenseroperate()
    {
        button_sure.setOnClickListener(gotosure);
        button_finish.setOnClickListener(gotofinish);
    }
    private View.OnClickListener gotosure = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(place.equals(nfcplace)){
                appearance = str_appearance.getSelectedItem().toString();
                maintain = str_maintain.getSelectedItem().toString();
                function = str_function.getSelectedItem().toString();
                //加php
                String url = "http://140.136.151.67/hsQRcodeScanData.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = response.trim();
                        if (result.equals("success")) {
                            Toast.makeText(RFIDActivity.this, "掃描成功", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(RFIDActivity.this, "此物品已掃描過", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RFIDActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map getParams() {
                        Map params = new HashMap();
                        params.put("name", name);//和php的參數做連結
                        params.put("ampm", ampm);
                        params.put("itemnumber",nfcitemnumber);
                        params.put("itemname",nfcitemname);
                        params.put("apperance",appearance);
                        params.put("maintain",maintain);
                        params.put("place",place);
                        params.put("function",function);
                        params.put("year",String.valueOf(year));
                        params.put("month",String.valueOf(month));
                        params.put("day",String.valueOf(day));
                        params.put("hour",String.valueOf(hour));
                        params.put("minute",String.valueOf(minute));
                        return params;

                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(RFIDActivity.this);
                requestQueue.add(stringRequest);

                //Toast.makeText(QRcodeMainActivity.this, "盤點成功", Toast.LENGTH_SHORT).show();

            }

            else {
                Toast.makeText(RFIDActivity.this, "此資產不屬於此使用單位", Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener gotofinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(RFIDActivity.this, InitialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}
