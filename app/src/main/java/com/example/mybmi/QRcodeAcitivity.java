package com.example.mybmi;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.DecimalFormat;

public class QRcodeAcitivity extends Activity {
    String name,ampm,place,qrcontent = "無"+"\n"+"無"+"\n"+"無",qritemnumber,qritemname,qrplace;
    double year ,month,day,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_acitivity);
        getCameraPermission();
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

        }
        catch(Exception obj) {
            Toast.makeText(this, "要先輸入完整資料喔!", Toast.LENGTH_SHORT).show();
        }

        scene.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(surfaceHolder);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder,int i,int i1,int i2){

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }

        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){
            @Override
            public void release(){

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    context.post(new Runnable() {
                        @Override
                        public void run() {

                            qrcontent = qrCodes.valueAt(0).displayValue;
                            stringSpilt(qrcontent);
                        }

                    });
                }
            }
        });


    }
    private TextView inventoryway;
    private TextView context;
    private SurfaceView scene;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    private void initViews() {
        inventoryway = (TextView) findViewById(R.id.qrcodetextView);
        context = (TextView) findViewById(R.id.QRcodecontext);
        scene = (SurfaceView) findViewById(R.id.camerascene);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(300,300).build();
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setAutoFocusEnabled(true).build();
    }
    public  void  getCameraPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(QRcodeAcitivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setCancelable(false).setTitle("需要相機權限").setMessage("需要啟動相機才能使用QR code功能").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(QRcodeAcitivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }).show();
            }
            else{
                ActivityCompat.requestPermissions(QRcodeAcitivity.this,new String[]{Manifest.permission.CAMERA},1);
            }
        }
    }
    public void stringSpilt(String args){
        String[] arrSplit = args.split("\n");
        for (int i=0; i < arrSplit.length; i++)
            System.out.println(arrSplit[i]);
        qritemnumber = arrSplit[0];
        qritemname = arrSplit[1];
        qrplace = arrSplit[2];
        context.setText("產品代號:"+qritemnumber+"\n"+"產品名稱:"+qritemname+"\n"+"產品代號:"+qrplace);
    }

}